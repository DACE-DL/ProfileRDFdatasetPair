package Asupprimer;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.jena.rdf.model.Model;

import profiling.util.UriAndUriAndUriAndNumber;

public class MakeListClassAndPropertyOfInterestMostUsed {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndUriAndNumber> makeList(Model model, ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestCount, String nameOfListOut) {
		
		ArrayList<UriAndUriAndUriAndNumber> ListResources = new ArrayList<UriAndUriAndUriAndNumber>();
	
		Integer i = 0;
		
		Collections.sort(listClassAndPropertyOfInterestCount, new UriAndUriAndUriAndNumberComparator());

		for (UriAndUriAndUriAndNumber classAndPropertyOfInterestCount : listClassAndPropertyOfInterestCount) {
			ListResources.add(new UriAndUriAndUriAndNumber(classAndPropertyOfInterestCount.getUri1(), classAndPropertyOfInterestCount.getUri2(), classAndPropertyOfInterestCount.getUri3(), classAndPropertyOfInterestCount.getNumber()));		
			i++;
			if ( i > 99 ) break;
		};	

		return ListResources;
	}
	
	static class UriAndUriAndUriAndNumberComparator implements java.util.Comparator<UriAndUriAndUriAndNumber> {
		@Override
		public int compare(UriAndUriAndUriAndNumber a, UriAndUriAndUriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}