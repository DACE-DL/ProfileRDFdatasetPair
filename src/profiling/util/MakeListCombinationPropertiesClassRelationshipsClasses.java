package profiling.util;

import java.util.ArrayList;

public class MakeListCombinationPropertiesClassRelationshipsClasses {
	
	// Création du liste pour les classes reliées entre elles
	public static ArrayList<String>  makeList(ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships) {
		
		ArrayList<String> listCombinationPropertiesClassRelationshipsClasses = new ArrayList<String>();
		
		for (UriListAndUriAndUriListAndNumber resource : listCombinationPropertiesClassRelationships) {
			for (Uri uri : resource.getUriList1()) {
				String classe = uri.getUri();
				// Si classe pas encore traitée
				if (!listCombinationPropertiesClassRelationshipsClasses.contains(classe)) {
					listCombinationPropertiesClassRelationshipsClasses.add(classe);
				}
			}
			for (Uri uri : resource.getUriList2()) {
				String classe = uri.getUri();
				// Si classe pas encore traitée
				if (!listCombinationPropertiesClassRelationshipsClasses.contains(classe)) {
					listCombinationPropertiesClassRelationshipsClasses.add(classe);
				}
			}
		}
		return listCombinationPropertiesClassRelationshipsClasses;
	}
}

