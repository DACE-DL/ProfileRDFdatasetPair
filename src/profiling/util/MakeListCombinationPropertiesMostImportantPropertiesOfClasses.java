package profiling.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MakeListCombinationPropertiesMostImportantPropertiesOfClasses {
	
	// Création du modèle de description
	public static ArrayList<UriListAndUriAndUriListList>  makeList(ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships,ArrayList<UriListAndUriList2> listCombinationPropertiesWithNewClass, ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listMostUsedPropertyWithDatatypeAndClassRange) {
		//Instant start0 = Instant.now();	
		ArrayList<UriListAndUriAndUriListList> listCombinationPropertiesMostImportantPropertiesOfClasses = new ArrayList<UriListAndUriAndUriListList>();
		// On recupére toutes les combinaisons de classes qui sont en relation
		Set<UriList> setMostImportantListClasses = new HashSet<>();
		for (UriListAndUriAndUriListAndNumber uriListAndUriAndUriListAndNumber : listCombinationPropertiesClassRelationships) {
			setMostImportantListClasses.add(new UriList(uriListAndUriAndUriListAndNumber.getUriList1()));
			setMostImportantListClasses.add(new UriList(uriListAndUriAndUriListAndNumber.getUriList2()));
		}
		ArrayList<UriList> listMostImportantListClasses = new ArrayList<>(setMostImportantListClasses);	
		
		for (UriList treatedListClasses : listMostImportantListClasses) {
			ArrayList<UriAndUriList> uriAndUriListList = new ArrayList<UriAndUriList>();
			// Pour rappel la liste listCombinationPropertiesWithNewClass à été nettoyée pour obtenir les propriétés communes !  
			// En effet quand plusieurs combinaisons de propriétés conrespondait à une même combinaison de classes seulles les propriétés
			//  communes ont été retenues.
			for (UriListAndUriList2 propertiesOfClass : listCombinationPropertiesWithNewClass) {
				if(propertiesOfClass.getUriList1().toString().equals(treatedListClasses.toString()) ) {
					for (Uri property : propertiesOfClass.getUriList2()) {
						for (UriAndUriListAndNumberListAndUriListAndNumberListAndNumber propertiesDatatypes : listMostUsedPropertyWithDatatypeAndClassRange) {
							if(propertiesDatatypes.getUri().equals(property)) {
								UriAndUriList uriAndUriList = new UriAndUriList();
								uriAndUriList.setUri(property.getUri());
								ArrayList<Uri> uriList = new ArrayList<Uri>();
								for (UriListAndNumber datatypeListAndNumber : propertiesDatatypes.getUriListAndNumberList1()) {
									// Pour les datatypes on considère qu'il ne peux y avoir q'un seul datatype dans la liste
									Uri datatype = new Uri(datatypeListAndNumber.getUriList().get(0).getUri());	
									uriList.add(datatype);
								}
								if (uriList.size() > 0) {
									// On s'assure contre les mauvaises suprises : cas ou dans le range de la propriété on ais
									//  à la fois des datatypes et des classes 
									Boolean objectProperty = false;
									for (UriListAndUriAndUriListAndNumber uriListAndUriAndUriListAndNumber : listCombinationPropertiesClassRelationships) {
										if (uriListAndUriAndUriListAndNumber.getUri().toString().equals(uriAndUriList.getUri().toString())) {
											objectProperty = true;
											break;
										}
									}	
									if (!objectProperty) {
										uriAndUriList.setUriList(uriList);
										uriAndUriListList.add(uriAndUriList);
									}
								} else {
									// Si la liste est vide on ne selectionne la propriété que si aucune classe
									//  n'est declarée dans le domaine.
									if (propertiesDatatypes.getUriListAndNumberList2().size() == 0) {
										// on s'assure aussi que la relation n'à pas été utilisé pour une object property
										// dans le cas de la création de nouvelle classes 
										Boolean objectProperty = false;
										for (UriListAndUriAndUriListAndNumber uriListAndUriAndUriListAndNumber : listCombinationPropertiesClassRelationships) {
											if (uriListAndUriAndUriListAndNumber.getUri().toString().equals(uriAndUriList.getUri().toString())) {
												objectProperty = true;
												break;
											}
										}	
										if (!objectProperty) {
											uriAndUriList.setUriList(uriList);
											uriAndUriListList.add(uriAndUriList);
										}
									}
								}
								
							}	
						}
					}

					UriListAndUriAndUriListList uriListAndUriAndUriListList = new UriListAndUriAndUriListList();
					uriListAndUriAndUriListList.setUriList(treatedListClasses);
					uriListAndUriAndUriListList.setUriAndUriListList(uriAndUriListList);
					listCombinationPropertiesMostImportantPropertiesOfClasses.add(uriListAndUriAndUriListList);
				}
			}
		}
		//Instant end0 = Instant.now();
		//System.out.println("Running time for listCombinationPropertiesMostImportantPropertiesOfClasses: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return listCombinationPropertiesMostImportantPropertiesOfClasses;
	}
}

