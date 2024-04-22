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
			if (n < 100) {
				ListResources.add(resource) ;
				n++;
			} else {
				break;
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