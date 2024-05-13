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
		
		// Pour éviter la modification de la liste d'origine
		ArrayList<UriListAndUriListAndNumberListAndNumber> listCombinationPropertiesPerSubjectTemp = new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		for (UriListAndUriListAndNumberListAndNumber obj : listCombinationPropertiesPerSubject) {
            UriListAndUriListAndNumberListAndNumber newObj = new UriListAndUriListAndNumberListAndNumber(obj); // Créez un nouvel objet en copiant les valeurs de obj
            listCombinationPropertiesPerSubjectTemp.add(newObj);
        }
		ArrayList<String> listTreatedListClassListString =  new ArrayList<String>();
		ArrayList<UriListAndUriListAndNumberListAndNumber> CleanedListResources =  new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		ArrayList<UriListAndUriListAndNumberListAndNumber> CleanedListResources2 =  new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		ArrayList<UriListAndUriListAndNumberListAndNumber> CleanedAndReducedListResources =  new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		ArrayList<UriListAndNumber> ListUriListAndNumber =  new ArrayList<UriListAndNumber>();
		
		Integer n = 0;
		
		Instant start0 = Instant.now();	
		// On commence par nettoyer listCombinationPropertiesPerSubject en suprimant dans la liste des listes de classes
		//  les listes de classes qui représentes moins de 1% de l'ensemble des instances des classes pour une combinaison données 
		for (UriListAndUriListAndNumberListAndNumber resource : listCombinationPropertiesPerSubjectTemp) {
			ArrayList<UriListAndNumber> ListUriListAndNumberTemp =  new ArrayList<UriListAndNumber>();
			if (resource.getUriListAndNumberList().size()<= 1) { // Il n'y a qu'une liste de classes pour la combinaison de propriétés, donc on sélectione 
        		CleanedListResources.add(new UriListAndUriListAndNumberListAndNumber(resource));
			} else {
				ListUriListAndNumber = new ArrayList<UriListAndNumber>(resource.getUriListAndNumberList());
				first = true;
				Integer number = resource.getNumber();
				Integer totalNumber = resource.getNumber();
				Integer numberToSubtract = 0;
				for (UriListAndNumber uriListAndNumber : ListUriListAndNumber) {
					if (first) {
						ListUriListAndNumberTemp.add(new UriListAndNumber(uriListAndNumber));
						// number = uriAndNumber.getNumber();
						first = false;
					} else {
						// Si le nombre d'instances de la liste de classes suivante représente plus 
						//  de 1% de l'ensemble des instances des liste de classes déjà selectionnées pour une combinaison données.
						if (((uriListAndNumber.getNumber() * 100) / totalNumber) > 1) {
							ListUriListAndNumberTemp.add(new UriListAndNumber(uriListAndNumber));
							totalNumber = totalNumber + uriListAndNumber.getNumber();
						} else {
							numberToSubtract = numberToSubtract + uriListAndNumber.getNumber();
						}
					}
				}
				if (numberToSubtract == 0) {
					CleanedListResources.add(new UriListAndUriListAndNumberListAndNumber(resource));
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

		
		// On cherche maintenant, dans le cas ou plusieurs combinaisons de propriétés sont caractéristiques d'une
		//  liste de listes de classes, à ne retenir que la combinaison minimal commune.
		
		// On commence par trier la liste de liste de classe pour pouvoir les comparées (elle est triée par nombre d'occurence de combinaisons) 
		ArrayList<UriListAndUriListAndNumberListAndNumber> CleanedListResourcesTemp = new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		for (UriListAndUriListAndNumberListAndNumber resource : CleanedListResources) {
			ArrayList<UriListAndNumber> listOflistOfClassAndNumber = resource.getUriListAndNumberList();
			Collections.sort(listOflistOfClassAndNumber, new UriListAndNumberComparator()); 	
			CleanedListResourcesTemp.add(new UriListAndUriListAndNumberListAndNumber(resource.getUriList(), listOflistOfClassAndNumber ,resource.getNumber()));
			// System.out.println("Old list: ");
			// for (UriListAndNumber resource2 : resource.getUriListAndNumberList()) {
			// 	System.out.println(resource2.getUriList().toString());
			// }
			// System.out.println("New list: ");	
			// for (UriListAndNumber resource3 : listOflistOfClassAndNumber) {
			// 	System.out.println(resource3.getUriList().toString());
			// }	
		}
		ArrayList<UriListAndUriListAndNumberListAndNumber> CleanedListResourcesTemp2 = new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		for (UriListAndUriListAndNumberListAndNumber obj : CleanedListResourcesTemp) {
            UriListAndUriListAndNumberListAndNumber newObj = new UriListAndUriListAndNumberListAndNumber(obj); // Créez un nouvel objet en copiant les valeurs de obj
            CleanedListResourcesTemp2.add(newObj);
        }

		// Traitement des combinaisons de propriétés avec des listes de classes
        for (UriListAndUriListAndNumberListAndNumber resource : CleanedListResourcesTemp) {
			if (!resource.getUriListAndNumberList().get(0).getUriList().toString().equals("[]")) { // on ne traite pas les combinaisons de propriétés sans classes
				ArrayList<UriListAndNumber> treatedListClassAndNumberList = new ArrayList<UriListAndNumber>();
				treatedListClassAndNumberList = resource.getUriListAndNumberList();
				String treatedListClassListString = "";
				for (UriListAndNumber classListAndNumber :resource.getUriListAndNumberList()){ 
					treatedListClassListString = treatedListClassListString + classListAndNumber.getUriList().toString();
				}

				// System.out.println("treatedListClassListString :" + treatedListClassListString);					
				Integer number = 0;
				ArrayList<Uri> listResultProperties =  new ArrayList<Uri>();
				if (!listTreatedListClassListString.contains(treatedListClassListString)) {
					listTreatedListClassListString.add(treatedListClassListString);
					// System.out.println("treatement");	
					ArrayList<Uri> listProperties =  new ArrayList<Uri>();
					listProperties.addAll(resource.getUriList());
					for (UriListAndUriListAndNumberListAndNumber resourceTemp : CleanedListResourcesTemp2) {
						String treatedListClassListStringTemp = "";
						for (UriListAndNumber classListAndNumberTemp :resourceTemp.getUriListAndNumberList()){ 
							treatedListClassListStringTemp = treatedListClassListStringTemp + classListAndNumberTemp.getUriList().toString();
						}
						if (treatedListClassListStringTemp.equals(treatedListClassListString)) {
							// Si le nombre d'instances de la combinaison suivante représente plus 
							//  de 1% de l'ensemble des instances déjà traitées.
							if (number <= 0) { // Premier passage (forcément obligatoire car CleanedListResourcesTemp2 est une copie de CleanedListResourcesTemp)
								number = number + resourceTemp.getNumber();
								listResultProperties.addAll(listProperties);
							} else { // Passages suivants
								if (((resourceTemp.getNumber() * 100) / number) > 1) {
									ArrayList<Uri> listPropertiesTemp =  new ArrayList<Uri>();
									listPropertiesTemp.addAll(resourceTemp.getUriList());
									// Extraction des éléments communs
									Integer listPropertiesSize = listProperties.size();
									Integer listPropertiesTempSize = listPropertiesTemp.size();
									Integer listPropertiesSizeMax = 0;
									if (listPropertiesSize >= listPropertiesTempSize) {
										listPropertiesSizeMax = listPropertiesSize;
									} else {
										listPropertiesSizeMax = listPropertiesTempSize;
									}
									ArrayList<Uri> commonElements = getCommonElements(listProperties, listPropertiesTemp);
									if (commonElements.size()>0) {
										number = number + resourceTemp.getNumber();
										listResultProperties.clear();
										listResultProperties.addAll(commonElements);
										listProperties.clear();
										listProperties.addAll(commonElements);
										// On met à jour le nombre d'instances de chaque liste de classes
										ArrayList<UriListAndNumber> treatedListClassAndNumberListTemp = new ArrayList<UriListAndNumber>();
										for (int i = 0; i < treatedListClassAndNumberList.size(); i++) {
											treatedListClassAndNumberListTemp.add(new UriListAndNumber(treatedListClassAndNumberList.get(i).getUriList(), 
											treatedListClassAndNumberList.get(i).getNumber() + resourceTemp.getUriListAndNumberList().get(i).getNumber()));
										}
										treatedListClassAndNumberList.clear();
										treatedListClassAndNumberList.addAll(treatedListClassAndNumberListTemp);

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
					CleanedListResources2.add(new UriListAndUriListAndNumberListAndNumber(uriList,treatedListClassAndNumberList,number));
				}
			} else {
				//CleanedListResources2.add(resource);
			}
		}
		// Traitement des combinaisons de propriétés sans listes de classes
		ArrayList<String> listCombinationTreated = new ArrayList<String>();
        for (UriListAndUriListAndNumberListAndNumber resource : CleanedListResourcesTemp) {
			if (resource.getUriListAndNumberList().get(0).getUriList().toString().equals("[]")) { // on ne traite que les combinaisons de propriétés sans classes
				String combinationTreated = resource.getUriList().toString();
				if (!listCombinationTreated.contains(combinationTreated)) {
					ArrayList<UriListAndNumber> treatedListClassAndNumberList = new ArrayList<UriListAndNumber>();
					treatedListClassAndNumberList = resource.getUriListAndNumberList();			
					Integer number = 0;
					ArrayList<Uri> listResultProperties =  new ArrayList<Uri>();
					ArrayList<Uri> listProperties =  new ArrayList<Uri>();
					listProperties.addAll(resource.getUriList());
					listResultProperties.addAll(resource.getUriList());
					ArrayList<String> listCombinationTreatedTemp = new ArrayList<String>();
					for (UriListAndUriListAndNumberListAndNumber resourceTemp : CleanedListResourcesTemp2) {
						if (resourceTemp.getUriListAndNumberList().get(0).getUriList().toString().equals("[]")) {
							// On vérifie que la combinaison n'a pas été traitée
							String combinationTreatedTemp = resourceTemp.getUriList().toString();
							if (!listCombinationTreated.contains(combinationTreatedTemp)) {
								// Si le nombre d'instances de la combinaison suivante représente plus 
								//  de 1% de l'ensemble des instances déjà traitées.
								if (number <= 0) { // Premier passage (forcément obligatoire car CleanedListResourcesTemp2 est une copie de CleanedListResourcesTemp)
									number = number + resourceTemp.getNumber();
								} else { // Passages suivants
									// On vérifie en premier lieu si le nombre total d'instances pour la combinaison représente plus 
								    //  de 1% de l'ensemble des instances déjà traitées.
									if (((resourceTemp.getNumber() * 100) / number) > 1) {
										ArrayList<Uri> listPropertiesTemp =  new ArrayList<Uri>();
										listPropertiesTemp.addAll(resourceTemp.getUriList());
										// Extraction des éléments communs
										Integer listPropertiesSize = listProperties.size();
										Integer listPropertiesTempSize = listPropertiesTemp.size();
										Integer listPropertiesSizeMax = 0;
										if (listPropertiesSize >= listPropertiesTempSize) {
											listPropertiesSizeMax = listPropertiesSize;
										} else {
											listPropertiesSizeMax = listPropertiesTempSize;
										}
										ArrayList<Uri> commonElements = getCommonElements(listProperties, listPropertiesTemp);
										if (commonElements.size()>0) {
											// On ne fait la selection que si le nombre d'éléments communs est au moins supérieur
											//  au 2/3 de la plus grande liste  
											if (commonElements.size() >= (listPropertiesSizeMax*2/3)) {
												number = number + resourceTemp.getNumber();
												listResultProperties.clear();
												listResultProperties.addAll(commonElements);
												listProperties.clear();
												listProperties.addAll(commonElements);
												// On met à jour le nombre d'instances de chaque liste de classes
												ArrayList<UriListAndNumber> treatedListClassAndNumberListTemp = new ArrayList<UriListAndNumber>();
												for (int i = 0; i < treatedListClassAndNumberList.size(); i++) {
													treatedListClassAndNumberListTemp.add(new UriListAndNumber(treatedListClassAndNumberList.get(i).getUriList(), 
													treatedListClassAndNumberList.get(i).getNumber() + resourceTemp.getUriListAndNumberList().get(i).getNumber()));
												}
												treatedListClassAndNumberList.clear();
												treatedListClassAndNumberList.addAll(treatedListClassAndNumberListTemp);
												listCombinationTreatedTemp.add(combinationTreatedTemp); // On considère la combinaison comme traitée
											}	
										} 
									} else {
										break;
									}
								}
							}
						}
					}
					
					
					ArrayList<Uri> uriList =  new ArrayList<Uri>();
					uriList.addAll(listResultProperties);
					UriListAndUriListAndNumberListAndNumber uriListAndUriListAndNumberListAndNumber = new UriListAndUriListAndNumberListAndNumber(uriList,
					treatedListClassAndNumberList,number);
				    CleanedListResources2.add(uriListAndUriListAndNumberListAndNumber);
					
					listCombinationTreated.add(combinationTreated);
					listCombinationTreated.addAll(listCombinationTreatedTemp);
				}
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

	static class UriListAndNumberComparator implements java.util.Comparator<UriListAndNumber> {
		@Override
		public int compare(UriListAndNumber a, UriListAndNumber b) {
			// Compare les tailles des listes
			int sizeComparison = Integer.compare(a.getUriList().size(), b.getUriList().size());
			if (sizeComparison != 0) {
				return sizeComparison;
			}
			// Compare les éléments de chaque liste
			for (int i = 0; i < a.getUriList().size(); i++) {
				Object objA = a.getUriList().get(i);
				Object objB = b.getUriList().get(i);
				if (objA == null && objB == null) {
					continue;
				} else if (objA == null) {
					return -1; // a est considéré plus petit car il contient un élément null
				} else if (objB == null) {
					return 1; // b est considéré plus petit car il contient un élément null
				}
				int elementComparison = objA.toString().compareTo(objB.toString());
				if (elementComparison != 0) {
					return elementComparison;
				}
			}
			return 0; // Les listes sont identiques
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