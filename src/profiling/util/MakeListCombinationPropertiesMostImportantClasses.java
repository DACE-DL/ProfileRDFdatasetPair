package profiling.util;

import java.util.ArrayList;

public class MakeListCombinationPropertiesMostImportantClasses {
	
	// Création du liste pour les classes reliées entre elles
	public static ArrayList<String>  makeList(ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships) {
		
		ArrayList<String> listCombinationPropertiesMostImportantClasses = new ArrayList<String>();
		
		for (UriListAndUriAndUriListAndNumber resource : listCombinationPropertiesClassRelationships) {
			for (Uri uri : resource.getUriList1()) {
				String classe = uri.getUri();
				// Si classe pas encore traitée
				if (!listCombinationPropertiesMostImportantClasses.contains(classe)) {
					listCombinationPropertiesMostImportantClasses.add(classe);
				}
			}
			for (Uri uri : resource.getUriList2()) {
				String classe = uri.getUri();
				// Si classe pas encore traitée
				if (!listCombinationPropertiesMostImportantClasses.contains(classe)) {
					listCombinationPropertiesMostImportantClasses.add(classe);
				}
			}
		}
		return listCombinationPropertiesMostImportantClasses;
	}
}

