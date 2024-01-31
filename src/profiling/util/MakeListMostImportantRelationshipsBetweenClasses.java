package profiling.util;

import java.util.ArrayList;

public class MakeListMostImportantRelationshipsBetweenClasses {
	
	// Création du modèle de description
	public static ArrayList<String>  makeList(ArrayList<UriListAndUriList> listOfRelationshipsDomain, ArrayList<UriListAndUriList> listOfRelationshipsRange) {
		
		ArrayList<String> listMostImportantRelationshipsBetweenClasses = new ArrayList<String>();
		
		for (UriListAndUriList resource : listOfRelationshipsDomain) {
			for (String relation : resource.getUriList1()) {
				// Si propriété pas encore traitée
				if (!listMostImportantRelationshipsBetweenClasses.contains(relation)) {
					listMostImportantRelationshipsBetweenClasses.add(relation);
				}
			}
		}

		for (UriListAndUriList resource : listOfRelationshipsRange) {
			for (String relation : resource.getUriList1()) {
				// Si propriété pas encore traitée
				if (!listMostImportantRelationshipsBetweenClasses.contains(relation)) {
					listMostImportantRelationshipsBetweenClasses.add(relation);
				}
			}
		}

		return listMostImportantRelationshipsBetweenClasses;
	}
}

