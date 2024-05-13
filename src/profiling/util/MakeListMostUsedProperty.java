package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.jena.rdf.model.Model;

public class MakeListMostUsedProperty {
	
	// Création d'une liste des propriétés limité à 100
	public static ArrayList<UriAndNumber> makeList(Model model, ArrayList<UriAndNumber> listPropertyUsageCount) {

		ArrayList<UriAndNumber> ListResources = new ArrayList<UriAndNumber>();
		Integer n = 0;

		for (UriAndNumber resource : listPropertyUsageCount) {
			// On se débarasse de certaines propriétés de RDF pour les listes...
			if (! resource.getUri().toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#rest")
			&& ! resource.getUri().toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#nil")
			&& ! resource.getUri().toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#first")
			&& ! resource.getUri().toString().equals("http://www.w3.org/2002/07/owl#oneOf")
			) {
				if (n < 100) {
					ListResources.add(resource) ;
					n++;
				} else {
					break;
				}
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