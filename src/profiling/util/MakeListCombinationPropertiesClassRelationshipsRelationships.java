package profiling.util;

import java.util.ArrayList;

public class MakeListCombinationPropertiesClassRelationshipsRelationships {
	
	// Création du modèle de description
	public static ArrayList<UriListAndUriAndUriList>  makeList(ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships) {
		
		ArrayList<UriListAndUriAndUriList> listCombinationPropertiesClassRelationshipsRelationships = new ArrayList<UriListAndUriAndUriList>();
		
		for (UriListAndUriAndUriListAndNumber resource : listCombinationPropertiesClassRelationships) {
			UriListAndUriAndUriList uriListAndUriAndUriList = new UriListAndUriAndUriList(resource.getUriList1(), resource.getUri(), resource.getUriList2());
			listCombinationPropertiesClassRelationshipsRelationships.add(uriListAndUriAndUriList);
		}

		return listCombinationPropertiesClassRelationshipsRelationships;
	}
}

