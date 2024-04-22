package Asupprimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import profiling.util.UriAndNumber;
import profiling.util.UriAndUriAndUriAndNumber;

public class MakeListObjectClassOfInterest {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndNumber> makeList(ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestMostUsed) {
		
		Map<String, Integer> uri3CountMap = new HashMap<>();
	
			// Compter le nombre d'apparitions de chaque URI1 dans la liste
			for (UriAndUriAndUriAndNumber resource : listClassAndPropertyOfInterestMostUsed) {
				String uri3 = resource.getUri3().toString();
				uri3CountMap.put(uri3, uri3CountMap.getOrDefault(uri3, 0) + 1);
			}
	
			// Créer la liste des ressources avec leur nombre d'apparitions
			ArrayList<UriAndNumber> listResources = new ArrayList<>();
			for (Map.Entry<String, Integer> entry : uri3CountMap.entrySet()) {
				listResources.add(new UriAndNumber(entry.getKey(), entry.getValue()));
			}
			Collections.sort(listResources, new UriAndNumberComparator());
			return listResources;
		}

	static class UriAndNumberComparator implements java.util.Comparator<UriAndNumber> {
		@Override
		public int compare(UriAndNumber a, UriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}