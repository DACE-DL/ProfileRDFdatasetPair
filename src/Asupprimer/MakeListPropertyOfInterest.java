package Asupprimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import profiling.util.UriAndNumber;
import profiling.util.UriAndUriAndUriAndNumber;

public class MakeListPropertyOfInterest {
    
    public static ArrayList<UriAndNumber> makeList(ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestMostUsed) {

        Map<String, Integer> uri2CountMap = new HashMap<>();

        // Compter le nombre d'apparitions de chaque URI2 dans la liste
        for (UriAndUriAndUriAndNumber resource : listClassAndPropertyOfInterestMostUsed) {
            String uri2 = resource.getUri2().toString();
            uri2CountMap.put(uri2, uri2CountMap.getOrDefault(uri2, 0) + 1);
        }

        // Créer la liste des ressources avec leur nombre d'apparitions
        ArrayList<UriAndNumber> listResources = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : uri2CountMap.entrySet()) {
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