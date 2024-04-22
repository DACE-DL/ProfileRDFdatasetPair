package profiling.util;

import java.util.ArrayList;
import java.util.Collections;

public class MakeListClassMostUsed {
	
	// Création d'une liste
	public static ArrayList<UriAndNumber> makeList(ArrayList<UriAndNumber> listClassUsageCount, double dQuantile) {
		
		ArrayList<UriAndNumber> ListResources = new ArrayList<UriAndNumber>();
		
		for (UriAndNumber resource : listClassUsageCount) {
			if (resource.getNumber() >= dQuantile) {
				ListResources.add(resource) ;
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