package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class MakeListClassSubjectCleanedAndReduced {
	static boolean first = false; 
	// On nettoie et on réduit listClassSubjectByPropertyCombination
	public static ArrayList<UriListAndUriAndNumberListAndNumber> makeList(ArrayList<UriListAndUriAndNumberListAndNumber> listClassSubjectByPropertyCombination) {
		
		ArrayList<String> listTreatedClasses =  new ArrayList<String>();
		ArrayList<UriListAndUriAndNumberListAndNumber> CleanedListResources =  new ArrayList<UriListAndUriAndNumberListAndNumber>();
		ArrayList<UriListAndUriAndNumberListAndNumber> CleanedListResources2 =  new ArrayList<UriListAndUriAndNumberListAndNumber>();
		ArrayList<UriListAndUriAndNumberListAndNumber> CleanedAndReducedListResources =  new ArrayList<UriListAndUriAndNumberListAndNumber>();
		ArrayList<UriAndNumber> ListUriAndNumber =  new ArrayList<UriAndNumber>();
		
		Integer n = 0;
		
		Instant start0 = Instant.now();	
		// On commence par nettoyer listClassSubjectByPropertyCombination en suprimant dans la liste des classes
		//  les classes qui représentes moins de 1% de l'ensemble des instances des classes pour une combinaison données 
		for (UriListAndUriAndNumberListAndNumber resource : listClassSubjectByPropertyCombination) {
			ArrayList<UriAndNumber> ListUriAndNumberTemp =  new ArrayList<UriAndNumber>();
			if (resource.getUriAndNumberList().size()<= 1) {
        		CleanedListResources.add(resource);
			} else {
				ListUriAndNumber = resource.getUriAndNumberList();
				first = true;
				Integer number = resource.getNumber();
				Integer numberToSubtract = 0;
				for (UriAndNumber uriAndNumber : ListUriAndNumber) {
					if (first) {
						ListUriAndNumberTemp.add(uriAndNumber);
						// number = uriAndNumber.getNumber();
						first = false;
					} else {
						// Si le nombre d'instances de la classe suivante représente plus 
						//  de 1% de l'ensemble des instances des classes pour une combinaison données.
						if (((uriAndNumber.getNumber() * 100) / number) > 1) {
							ListUriAndNumberTemp.add(uriAndNumber);
						} else {
							numberToSubtract = numberToSubtract + uriAndNumber.getNumber();
						}
					}
				}
				if (numberToSubtract == 0) {
					CleanedListResources.add(resource);
				} else {
					UriListAndUriAndNumberListAndNumber resourceTemp = 
					new UriListAndUriAndNumberListAndNumber(resource.getUriList(), ListUriAndNumberTemp, resource.getNumber() - numberToSubtract);
					CleanedListResources.add(resourceTemp);
				}	
			}
		}

		// Tri de la liste pour que les combinaisons les plus utilisées en priorité
		// L'ordre a pu être changé lors de la suppression de certaines classe pour une combinaison donnée
		Collections.sort(CleanedListResources, new UriListAndUriAndNumberListAndNumberComparator()); 	

		ArrayList<UriListAndUriAndNumberListAndNumber> CleanedListResourcesTemp = new ArrayList<UriListAndUriAndNumberListAndNumber>();
		CleanedListResourcesTemp.addAll(CleanedListResources);

		// On cherche maintenant, dans le cas ou plusieurs combinaisons de propriétés sont caractéristiques d'une
		//  seule et même classe, à ne retenir que la combinaison minimal commune.
        for (UriListAndUriAndNumberListAndNumber resource : CleanedListResources) {
			if (resource.getUriAndNumberList().size()==1 && !resource.getUriAndNumberList().get(0).getUri().equals("")) {
				String treatedClass = resource.getUriAndNumberList().get(0).getUri();
				Integer number = 0;
				ArrayList<Uri> listResultProperties =  new ArrayList<Uri>();
				if (!listTreatedClasses.contains(treatedClass)) {
					listTreatedClasses.add(treatedClass);
					ArrayList<Uri> listProperties =  new ArrayList<Uri>();
					listProperties.addAll(resource.getUriList());
					for (UriListAndUriAndNumberListAndNumber resourceTemp : CleanedListResourcesTemp) {
						if (resourceTemp.getUriAndNumberList().size()==1 && resourceTemp.getUriAndNumberList().get(0).getUri().equals(treatedClass)) {
							// Si le nombre d'instances de la classe suivante représente plus 
							//  de 1% de l'ensemble des instances déjà traitées.
							if (number <= 0) { // Premier passage (forcément obligatoire)
								number = number + resourceTemp.getUriAndNumberList().get(0).getNumber();
								listResultProperties.addAll(listProperties);
							} else { // Passages suivants
								if (((resourceTemp.getUriAndNumberList().get(0).getNumber() * 100) / number) > 1) {
									// if (treatedClass.equals("http://dbkwik.webdatacommons.org/swtor.wikia.com/class/character")) {
									// 	System.out.println("number : " + number); 
									// 	System.out.println("+ number : " + resourceTemp.getUriAndNumberList().get(0).getNumber()); 
									// }
									number = number + resourceTemp.getUriAndNumberList().get(0).getNumber();
									ArrayList<Uri> listPropertiesTemp =  new ArrayList<Uri>();
									listPropertiesTemp.addAll(resourceTemp.getUriList());
									// Extraction des éléments communs
									ArrayList<Uri> commonElements = getCommonElements(listProperties, listPropertiesTemp);
									listResultProperties.clear();
									listResultProperties.addAll(commonElements);
									listProperties.clear();
									listProperties.addAll(commonElements);
								} else {
									break;
								}
							}
						}
					}
					ArrayList<Uri> uriList =  new ArrayList<Uri>();
					uriList.addAll(listResultProperties);
					UriAndNumber uriAndNumber =  new UriAndNumber(treatedClass, number);
					ArrayList<UriAndNumber> uriAndNumberList =  new ArrayList<UriAndNumber>();
					uriAndNumberList.add(uriAndNumber);
					CleanedListResources2.add(new UriListAndUriAndNumberListAndNumber(uriList,uriAndNumberList,number));
				}
			} else {
				CleanedListResources2.add(resource);
			}
		}


		// Tri de la liste pour que les combinaisons les plus utilisées en priorité
		Collections.sort(CleanedListResources2, new UriListAndUriAndNumberListAndNumberComparator()); 	

		// On réduit la taille de la liste
		for (UriListAndUriAndNumberListAndNumber cleanedResource2 : CleanedListResources2) {
			n++;
			CleanedAndReducedListResources.add(cleanedResource2);
			if (n>39) {
				break;
		   	}		
		}

		// Tri de la liste pour que les combinaisons les plus utilisées en priorité
		Collections.sort(CleanedAndReducedListResources, new UriListAndUriAndNumberListAndNumberComparator()); 	

		Instant end0 = Instant.now();
		System.out.println("Running time for ListClassSubjectCleanedAndReduced: " + Duration.between(start0, end0).toMillis() + " milliseconds");
		return CleanedAndReducedListResources;
	}

    
	static class UriListAndUriAndNumberListAndNumberComparator implements java.util.Comparator<UriListAndUriAndNumberListAndNumber> {
		@Override
		public int compare(UriListAndUriAndNumberListAndNumber a, UriListAndUriAndNumberListAndNumber b) {
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

