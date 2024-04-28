package profiling.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MakeListCombinationPropertiesClassRelationshipsPropertiesOfClasses {
	
	// Création du modèle de description
	public static ArrayList<UriListAndUriAndUriListList>  makeList(ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships,ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClass, ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listMostUsedPropertyWithDatatypeAndClassRange) {
		//Instant start0 = Instant.now();	
		ArrayList<UriListAndUriAndUriListList> listCombinationPropertiesClassRelationshipsPropertiesOfClasses = new ArrayList<UriListAndUriAndUriListList>();
		// On recupére toutes les combinaisons de classes qui sont en relation
		Set<UriList> setListClassesInvolvedInRelationships = new HashSet<>();
		for (UriListAndUriAndUriListAndNumber uriListAndUriAndUriListAndNumber : listCombinationPropertiesClassRelationships) {
			setListClassesInvolvedInRelationships.add(new UriList(uriListAndUriAndUriListAndNumber.getUriList1()));
			setListClassesInvolvedInRelationships.add(new UriList(uriListAndUriAndUriListAndNumber.getUriList2()));
		}
		ArrayList<UriList> listListClassesInvolvedInRelationships = new ArrayList<>(setListClassesInvolvedInRelationships);	
		
		for (UriList treatedListClasses : listListClassesInvolvedInRelationships) {
			ArrayList<UriAndUriList> uriAndUriListList = new ArrayList<UriAndUriList>();
			// Pour rappel la liste listCombinationPropertiesWithNewClass à été nettoyée pour obtenir les propriétés communes !  
			// En effet quand plusieurs combinaisons de propriétés conrespondait à une même combinaison de classes seulles les propriétés
			//  communes ont été retenues.
			for (UriListAndUriList propertiesOfClass : listCombinationPropertiesWithNewClass) {
				if(propertiesOfClass.getUriList1().toString().equals(treatedListClasses.toString()) ) {
					for (Uri property : propertiesOfClass.getUriList2()) {
						for (UriAndUriListAndNumberListAndUriListAndNumberListAndNumber propertyWithDatatypeAndClassRange : listMostUsedPropertyWithDatatypeAndClassRange) {
							if(propertyWithDatatypeAndClassRange.getUri().equals(property)) {
								UriAndUriList uriAndUriList = new UriAndUriList();
								uriAndUriList.setUri(property.getUri());
								ArrayList<Uri> uriList = new ArrayList<Uri>();
								for (UriListAndNumber datatypeListAndNumber : propertyWithDatatypeAndClassRange.getUriListAndNumberList1()) {
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
									if (propertyWithDatatypeAndClassRange.getUriListAndNumberList2().size() == 0) {
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
					listCombinationPropertiesClassRelationshipsPropertiesOfClasses.add(uriListAndUriAndUriListList);
				}
			}
		}
		//Instant end0 = Instant.now();
		//System.out.println("Running time for listCombinationPropertiesClassRelationshipsPropertiesOfClasses: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return listCombinationPropertiesClassRelationshipsPropertiesOfClasses;
	}
}

