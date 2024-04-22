package profiling.util;

import java.util.ArrayList;

public class MakeListClassNotDefined {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<Uri> makeList(ArrayList<UriAndNumber> listClassUsageCount, ArrayList<Uri> listOfClassDefined) {
		
		ArrayList<Uri> ListResources = new ArrayList<Uri>();

		for (UriAndNumber resource : listClassUsageCount) {
			if (!listOfClassDefined.contains(new Uri(resource.getUri()))) {
				ListResources.add(new Uri(resource.getUri()));
			}
		}
		return ListResources;
	}
}