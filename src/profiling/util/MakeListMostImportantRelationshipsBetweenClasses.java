package profiling.util;

import java.util.ArrayList;

public class MakeListMostImportantRelationshipsBetweenClasses {
	
	// Création du modèle de description
	public static ArrayList<UriAndUriAndUri>  makeList(ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClasses) {
		
		ArrayList<UriAndUriAndUri> listMostImportantRelationshipsBetweenClasses = new ArrayList<UriAndUriAndUri>();
		
		for (UriAndUriAndUriAndNumber resource : listOfRelationshipsBetweenNewClasses) {
			UriAndUriAndUri uriAndUriAndUri = new UriAndUriAndUri(resource.getUri1(), resource.getUri2(), resource.getUri3());
			listMostImportantRelationshipsBetweenClasses.add(uriAndUriAndUri);
		}

		return listMostImportantRelationshipsBetweenClasses;
	}
}

