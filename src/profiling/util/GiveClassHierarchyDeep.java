package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GiveClassHierarchyDeep {

    public static HierarchyDeepAndLoop giveHierarchyDeepAndLoop(ArrayList<UriAndUri> listClassAndSubclass) {
        Instant start0 = Instant.now();
        HierarchyDeepAndLoop hierarchyDeepAndLoop = new HierarchyDeepAndLoop();
        int maxDeep = 0;
        boolean infiniteLoop = false;
        int n = 0;
        int nMax = 100000;
        ArrayList<UriAndUri> listClassAndSubclassTemp = new ArrayList<UriAndUri>();
        
        if (listClassAndSubclass.size() > nMax) {
            System.out.println("Warning: Due to the large size of the list of classes and their subclasses (" + listClassAndSubclass.size() + "), the hierarchy depth search and infinite loop processing will be limited.");
            List<UriAndUri> subList = listClassAndSubclass.subList(1, nMax);
            listClassAndSubclassTemp = new ArrayList<UriAndUri>(subList);
        } else {
            listClassAndSubclassTemp = new ArrayList<UriAndUri>(listClassAndSubclass);
        }
    
        if (!listClassAndSubclassTemp.isEmpty()) {
            HashSet<UriAndUri> setClassAndSubclass = new HashSet<>(listClassAndSubclassTemp);
            Map<String, Integer> memo = new HashMap<>(); // Pour mémoriser les profondeurs calculées des classes et éviter de recalculer pour les mêmes sous-arbres.
            Set<String> visiting = new HashSet<>();  // Pour détecter les cycles pendant la visite des noeuds.
            for (UriAndUri resource : listClassAndSubclassTemp) {
                n++;
                String className = resource.getUri1();
                int maxDeepTemp = getMaxDeep(setClassAndSubclass, className, memo, visiting);
                if (maxDeepTemp > maxDeep) {
                    maxDeep = maxDeepTemp;
                }
                if (maxDeepTemp == Integer.MAX_VALUE) {
                    infiniteLoop = true;
                    break;
                }
                if (n > nMax) {
                    break;
                }
            }
        }
        hierarchyDeepAndLoop.setHierarchyDeep(maxDeep);
        hierarchyDeepAndLoop.setLoop(infiniteLoop);
        Instant end0 = Instant.now();
        System.out.println("Running time for Classes Hierarchy Deep: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
        return hierarchyDeepAndLoop;
    }
    
    private static int getMaxDeep(HashSet<UriAndUri> setClassAndSubclass, String className, Map<String, Integer> memo, Set<String> visiting) {
        if (memo.containsKey(className)) {
            return memo.get(className);
        }
        if (visiting.contains(className)) {
            return Integer.MAX_VALUE; // Detected a cycle
        }
        visiting.add(className);
        int maxDepth = 1;
        for (UriAndUri resource : setClassAndSubclass) {
            if (resource.getUri1().equals(className)) {
                int depth = getMaxDeep(setClassAndSubclass, resource.getUri2(), memo, visiting);
                if (depth == Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE; // Propagate cycle detection
                }
                maxDepth = Math.max(maxDepth, depth + 1);
            }
        }
        visiting.remove(className);
        memo.put(className, maxDepth);
        return maxDepth;
    }
}
