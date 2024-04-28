package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.jena.rdf.model.Model;

public class MakeListMostUsedAnnotationProperty {
	
	// Création d'une liste des propriétés d'annotation
	public static ArrayList<UriAndNumber> makeList(Model model, ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listMostUsedPropertyWithDatatypeAndClassRange, ArrayList<UriAndUri> listMostUsedPropertyType) {

		ArrayList<UriAndNumber> ListResources = new ArrayList<UriAndNumber>();

		for (UriAndUriListAndNumberListAndUriListAndNumberListAndNumber resource : listMostUsedPropertyWithDatatypeAndClassRange) {
			Boolean annotationProperty = false;
			for (UriAndUri resource2 : listMostUsedPropertyType) {
				if (resource2.getUri1().toString().equals(resource.getUri().toString()) && 
					resource2.getUri2().toString().equals("http://www.w3.org/2002/07/owl#AnnotationProperty")) {
					annotationProperty = true;
					break;
				}
			}
			if (annotationProperty) {
				ListResources.add(new UriAndNumber(resource.getUri().toString(), resource.getNumber())) ;
			}
		}
		
		Collections.sort(ListResources, new UriAndNumberComparator());

		return ListResources;
	}

	static class UriAndNumberComparator implements java.util.Comparator<UriAndNumber> {
		@Override
		public int compare(UriAndNumber a, UriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}