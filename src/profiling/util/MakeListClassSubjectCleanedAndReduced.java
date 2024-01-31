package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

public class MakeListClassSubjectCleanedAndReduced {
	static boolean first = false; 
	// Création de Classes pour chaque combinaison de propriétés
	public static ArrayList<UriListAndUriAndNumberListAndNumber> makeList(ArrayList<UriListAndUriAndNumberListAndNumber> ListResources) {
		
		ArrayList<UriListAndUriAndNumberListAndNumber> CleanedListResources =  new ArrayList<UriListAndUriAndNumberListAndNumber>();
		ArrayList<UriListAndUriAndNumberListAndNumber> CleanedAndReducedListResources =  new ArrayList<UriListAndUriAndNumberListAndNumber>();
		ArrayList<UriAndNumber> ListUriAndNumber =  new ArrayList<UriAndNumber>();
		
		Integer n = 0;
		
		Instant start0 = Instant.now();	
		
		for (UriListAndUriAndNumberListAndNumber resource : ListResources) {
			ArrayList<UriAndNumber> ListUriAndNumberTemp =  new ArrayList<UriAndNumber>();
			if (resource.getUriAndNumberList().size()<= 1) {
        		CleanedListResources.add(resource);
			} else {
				ListUriAndNumber = resource.getUriAndNumberList();
				first = true;
				Integer number = 0;
				Integer numberToSubtract = 0;
				for (UriAndNumber uriAndNumber : ListUriAndNumber) {
					if (first) {
						ListUriAndNumberTemp.add(uriAndNumber);
						number = uriAndNumber.getNumber();
						first = false;
					} else {
						// Si le nombre d'instances de la classe suivante représente plus 
						//  de 5% de la première.
						if (((uriAndNumber.getNumber() * 100) / number) > 5) {
							ListUriAndNumberTemp.add(uriAndNumber);
						} else {
							numberToSubtract = numberToSubtract + uriAndNumber.getNumber();
						}
					}
				}
				if (numberToSubtract == 0) {
					CleanedListResources.add(resource);
				} else {
					UriListAndUriAndNumberListAndNumber resourceTemp = 
					new UriListAndUriAndNumberListAndNumber(resource.getUriList(), ListUriAndNumberTemp, resource.getNumber() - numberToSubtract);
					CleanedListResources.add(resourceTemp);
				}	
			}
		}

		// Tri de la liste pour que les combinaisons les plus utilisées en priorité
		Collections.sort(CleanedListResources, new UriListAndUriAndNumberListAndNumberComparator()); 	

		// On réduit la taille de la liste
		for (UriListAndUriAndNumberListAndNumber cleanedResource : CleanedListResources) {
			n++;
			CleanedAndReducedListResources.add(cleanedResource);
			if (n>39) {
				break;
		   	}		
		}

		// Tri de la liste pour que les combinaisons les plus utilisées en priorité
		Collections.sort(CleanedAndReducedListResources, new UriListAndUriAndNumberListAndNumberComparator()); 	

		Instant end0 = Instant.now();
		System.out.println("Running time for clean properties combinaisons : " + Duration.between(start0, end0).getSeconds() + " secondes");
		return CleanedAndReducedListResources;
	}

    
	static class UriListAndUriAndNumberListAndNumberComparator implements java.util.Comparator<UriListAndUriAndNumberListAndNumber> {
		@Override
		public int compare(UriListAndUriAndNumberListAndNumber a, UriListAndUriAndNumberListAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}

