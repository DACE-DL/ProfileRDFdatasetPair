package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GivePropertyHierarchyDeep {

    public static HierarchyDeepAndLoop giveHierarchyDeepAndLoop(ArrayList<UriAndUri> listPropertyAndSubproperty) {
        Instant start0 = Instant.now();
        HierarchyDeepAndLoop hierarchyDeepAndLoop = new HierarchyDeepAndLoop();
        int maxDeep = 0;
        boolean infiniteLoop = false;
        int n = 0;
        int nMax = 100000;
        ArrayList<UriAndUri> listPropertyAndSubpropertyTemp = new ArrayList<UriAndUri>();
    
        if (listPropertyAndSubproperty.size() > nMax) {
            System.out.println("Warning: Due to the large size of the list of properties and their subproperties (" + listPropertyAndSubproperty.size() + "), the hierarchy depth search and infinite loop processing will be limited.");
            List<UriAndUri> subList = listPropertyAndSubproperty.subList(1, nMax);
            listPropertyAndSubpropertyTemp = new ArrayList<UriAndUri>(subList);
        } else {
            listPropertyAndSubpropertyTemp = new ArrayList<UriAndUri>(listPropertyAndSubproperty);
        }
    
        if (!listPropertyAndSubpropertyTemp.isEmpty()) {
            HashSet<UriAndUri> setPropertyAndSubproperty = new HashSet<>(listPropertyAndSubpropertyTemp);
            Map<String, Integer> memo = new HashMap<>();
            Set<String> visiting = new HashSet<>();
    
            for (UriAndUri resource : listPropertyAndSubpropertyTemp) {
                n++;
                String propertyName = resource.getUri1();
                int maxDeepTemp = getMaxDeep(setPropertyAndSubproperty, propertyName, memo, visiting);
    
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
        System.out.println("Running time for Propertyes Hierarchy Deep: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
        return hierarchyDeepAndLoop;
    }
    
    private static int getMaxDeep(HashSet<UriAndUri> setPropertyAndSubproperty, String propertyName, Map<String, Integer> memo, Set<String> visiting) {
        if (memo.containsKey(propertyName)) {
            return memo.get(propertyName);
        }
    
        if (visiting.contains(propertyName)) {
            return Integer.MAX_VALUE; // Detected a cycle
        }
    
        visiting.add(propertyName);
        int maxDepth = 1;
    
        for (UriAndUri resource : setPropertyAndSubproperty) {
            if (resource.getUri1().equals(propertyName)) {
                int depth = getMaxDeep(setPropertyAndSubproperty, resource.getUri2(), memo, visiting);
                if (depth == Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE; // Propagate cycle detection
                }
                maxDepth = Math.max(maxDepth, depth + 1);
            }
        }
    
        visiting.remove(propertyName);
        memo.put(propertyName, maxDepth);
        return maxDepth;
    }
}
