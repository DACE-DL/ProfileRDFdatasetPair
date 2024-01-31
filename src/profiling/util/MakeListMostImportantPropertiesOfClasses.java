package profiling.util;

import java.util.ArrayList;

public class MakeListMostImportantPropertiesOfClasses {
	
	// Création du modèle de description
	public static ArrayList<UriAndUriList>  makeList(ArrayList<UriListAndUriList> listOfRelationshipsDomain, ArrayList<UriListAndUriList> listOfRelationshipsRange) {
		
		ArrayList<String> listMostImportantPropertiesOfClasses = new ArrayList<String>();
		
		for (UriListAndUriList resource : listOfRelationshipsDomain) {
			for (String classe : resource.getUriList2()) {
				// Si propriété pas encore traitée
				if (!listMostImportantClasses.contains(classe)) {
					listMostImportantClasses.add(classe);
				}
			}
		}

		for (UriListAndUriList resource : listOfRelationshipsRange) {
			for (String classe : resource.getUriList2()) {
				// Si propriété pas encore traitée
				if (!listMostImportantClasses.contains(classe)) {
					listMostImportantClasses.add(classe);
				}
			}
		}

		return listMostImportantPropertiesOfClasses;
	}
}

