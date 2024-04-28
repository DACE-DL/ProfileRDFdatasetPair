package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class MakeListCombinationPropertiesPerSubjectCleanedAndReduced {
	static boolean first = false; 
	// On nettoie et on réduit listCombinationPropertiesPerSubject
	public static ArrayList<UriListAndUriListAndNumberListAndNumber> makeList(ArrayList<UriListAndUriListAndNumberListAndNumber> listCombinationPropertiesPerSubject) {
		
		ArrayList<String> listTreatedClassList =  new ArrayList<String>();
		ArrayList<UriListAndUriListAndNumberListAndNumber> CleanedListResources =  new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		ArrayList<UriListAndUriListAndNumberListAndNumber> CleanedListResources2 =  new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		ArrayList<UriListAndUriListAndNumberListAndNumber> CleanedAndReducedListResources =  new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		ArrayList<UriListAndNumber> ListUriListAndNumber =  new ArrayList<UriListAndNumber>();
		
		Integer n = 0;
		
		Instant start0 = Instant.now();	
		// On commence par nettoyer listCombinationPropertiesPerSubject en suprimant dans la liste des listes de classes
		//  les listes de classes qui représentes moins de 1% de l'ensemble des instances des classes pour une combinaison données 
		for (UriListAndUriListAndNumberListAndNumber resource : listCombinationPropertiesPerSubject) {
			ArrayList<UriListAndNumber> ListUriListAndNumberTemp =  new ArrayList<UriListAndNumber>();
			if (resource.getUriListAndNumberList().size()<= 1) { // Il n'y a qu'une propriété dans la liste donc on sélectione 
        		CleanedListResources.add(resource);
			} else {
				ListUriListAndNumber = resource.getUriListAndNumberList();
				first = true;
				Integer number = resource.getNumber();
				Integer numberToSubtract = 0;
				for (UriListAndNumber uriListAndNumber : ListUriListAndNumber) {
					if (first) {
						ListUriListAndNumberTemp.add(uriListAndNumber);
						// number = uriAndNumber.getNumber();
						first = false;
					} else {
						// Si le nombre d'instances de la classe suivante représente plus 
						//  de 1% de l'ensemble des instances des classes pour une combinaison données.
						if (((uriListAndNumber.getNumber() * 100) / number) > 1) {
							ListUriListAndNumberTemp.add(uriListAndNumber);
						} else {
							numberToSubtract = numberToSubtract + uriListAndNumber.getNumber();
						}
					}
				}
				if (numberToSubtract == 0) {
					CleanedListResources.add(resource);
				} else {
					UriListAndUriListAndNumberListAndNumber resourceTemp = 
					new UriListAndUriListAndNumberListAndNumber(resource.getUriList(), ListUriListAndNumberTemp, resource.getNumber() - numberToSubtract);
					CleanedListResources.add(resourceTemp);
				}	
			}
		}

		// Tri de la liste pour que les combinaisons les plus utilisées en priorité
		// L'ordre a pu être changé lors de la suppression de certaines classe pour une combinaison donnée
		Collections.sort(CleanedListResources, new UriListAndUriListAndNumberListAndNumberComparator()); 	

		ArrayList<UriListAndUriListAndNumberListAndNumber> CleanedListResourcesTemp = new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		CleanedListResourcesTemp.addAll(CleanedListResources);

		// On cherche maintenant, dans le cas ou plusieurs combinaisons de propriétés sont caractéristiques d'une
		//  seule et même liste de classes, à ne retenir que la combinaison minimal commune.
        for (UriListAndUriListAndNumberListAndNumber resource : CleanedListResources) {
			if (resource.getUriListAndNumberList().size()==1 && resource.getUriListAndNumberList().get(0).getUriList().size() > 0) {
				if (resource.getUriListAndNumberList().get(0).getUriList().size() > 1 || 
				(resource.getUriListAndNumberList().get(0).getUriList().size() == 1 && !resource.getUriListAndNumberList().get(0).getUriList().get(0).getUri().equals(""))) {
					String treatedClassList = resource.getUriListAndNumberList().get(0).getUriList().toString();
					// System.out.println("treatedClassList :" + treatedClassList);
					// System.out.println("resource.getUriListAndNumberList().get(0).getUriList().size() :" + resource.getUriListAndNumberList().get(0).getUriList().size());
					// System.out.println("resource.getUriListAndNumberList().get(0).getUriList().get(0).getUri() :" + resource.getUriListAndNumberList().get(0).getUriList().get(0).getUri());
					// System.out.println(resource.getUriListAndNumberList().get(0).getUriList().get(0).getUri().equals(""));
					Integer number = 0;
					ArrayList<Uri> listResultProperties =  new ArrayList<Uri>();
					if (!listTreatedClassList.contains(treatedClassList)) {
						listTreatedClassList.add(treatedClassList);
						ArrayList<Uri> listProperties =  new ArrayList<Uri>();
						listProperties.addAll(resource.getUriList());
						for (UriListAndUriListAndNumberListAndNumber resourceTemp : CleanedListResourcesTemp) {
							if (resourceTemp.getUriListAndNumberList().size()==1 && resourceTemp.getUriListAndNumberList().get(0).getUriList().toString().equals(treatedClassList)) {
								// Si le nombre d'instances de la classe suivante représente plus 
								//  de 1% de l'ensemble des instances déjà traitées.
								if (number <= 0) { // Premier passage (forcément obligatoire)
									number = number + resourceTemp.getUriListAndNumberList().get(0).getNumber();
									listResultProperties.addAll(listProperties);
								} else { // Passages suivants
									if (((resourceTemp.getUriListAndNumberList().get(0).getNumber() * 100) / number) > 1) {
										// if (treatedClass.equals("http://dbkwik.webdatacommons.org/swtor.wikia.com/class/character")) {
										// 	System.out.println("number : " + number); 
										// 	System.out.println("+ number : " + resourceTemp.getUriAndNumberList().get(0).getNumber()); 
										// }
										ArrayList<Uri> listPropertiesTemp =  new ArrayList<Uri>();
										listPropertiesTemp.addAll(resourceTemp.getUriList());
										// Extraction des éléments communs
										ArrayList<Uri> commonElements = getCommonElements(listProperties, listPropertiesTemp);
										if (commonElements.size()>=1) {
											number = number + resourceTemp.getUriListAndNumberList().get(0).getNumber();
											listResultProperties.clear();
											listResultProperties.addAll(commonElements);
											listProperties.clear();
											listProperties.addAll(commonElements);
										} else {

										}
									} else {
										break;
									}
								}
							}
						}
						ArrayList<Uri> uriList =  new ArrayList<Uri>();
						uriList.addAll(listResultProperties);
						UriListAndNumber uriListAndNumber =  new UriListAndNumber(resource.getUriListAndNumberList().get(0).getUriList(), number);
						ArrayList<UriListAndNumber> uriListAndNumberList =  new ArrayList<UriListAndNumber>();
						uriListAndNumberList.add(uriListAndNumber);
						CleanedListResources2.add(new UriListAndUriListAndNumberListAndNumber(uriList,uriListAndNumberList,number));
					}
				} else {
					CleanedListResources2.add(resource);
				}
			} else {
				CleanedListResources2.add(resource);
			}
		}


		// Tri de la liste pour que les combinaisons les plus utilisées en priorité
		Collections.sort(CleanedListResources2, new UriListAndUriListAndNumberListAndNumberComparator()); 	

		// On réduit la taille de la liste
		for (UriListAndUriListAndNumberListAndNumber cleanedResource2 : CleanedListResources2) {
			n++;
			CleanedAndReducedListResources.add(cleanedResource2);
			if (n>39) {
				break;
		   	}		
		}

		// Tri de la liste pour que les combinaisons les plus utilisées en priorité
		Collections.sort(CleanedAndReducedListResources, new UriListAndUriListAndNumberListAndNumberComparator()); 	

		Instant end0 = Instant.now();
		System.out.println("Running time for ListCombinationPropertiesPerSubjectCleanedAndReduced: " + Duration.between(start0, end0).toMillis() + " milliseconds");
		return CleanedAndReducedListResources;
	}

    
	static class UriListAndUriListAndNumberListAndNumberComparator implements java.util.Comparator<UriListAndUriListAndNumberListAndNumber> {
		@Override
		public int compare(UriListAndUriListAndNumberListAndNumber a, UriListAndUriListAndNumberListAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}

	// Méthode pour extraire les éléments communs entre deux listes
    public static ArrayList<Uri> getCommonElements(ArrayList<Uri> list1, ArrayList<Uri> list2) {
        // Utilisation d'un HashSet pour une recherche plus rapide des éléments communs
        HashSet<Uri> set1 = new HashSet<>(list1);
        HashSet<Uri> set2 = new HashSet<>(list2);

        // Retenir seulement les éléments communs
        set1.retainAll(set2);

        // Conversion du HashSet en ArrayList pour la cohérence
        return new ArrayList<>(set1);
    }
}