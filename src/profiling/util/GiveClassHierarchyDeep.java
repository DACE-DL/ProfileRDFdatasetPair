package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class GiveClassHierarchyDeep {

    public static HierarchyDeepAndLoop giveHierarchyDeepAndLoop(ArrayList<UriAndUri> listClassAndSubclass) {
        Instant start0 = Instant.now();
        HierarchyDeepAndLoop hierarchyDeepAndLoop = new HierarchyDeepAndLoop();
        Integer maxDeep = 0;
        Boolean infiniteLoop = false;
        Integer n = 0;
        Integer nMax = 100;        
        if (listClassAndSubclass.size() > nMax) {
            System.out.println("Warning: Due to the large size of the list of classes and their subclasses (" + listClassAndSubclass.size() + "), the hierarchy depth search and infinite loop processing will be limited..");
        }
        if (!listClassAndSubclass.isEmpty()) {
            HashSet<UriAndUri> setClassAndSubclass = new HashSet<>(listClassAndSubclass);
            for (UriAndUri resource : listClassAndSubclass) {
                n++;
                String className = resource.getUri1();
                String subclassName = resource.getUri2();
                Integer maxDeepTemp = getMaxDeep(setClassAndSubclass, className, subclassName);
                if (maxDeepTemp > maxDeep) {
                    maxDeep = maxDeepTemp;
                }
                if (maxDeepTemp > 1 && subclassName.equals(className)) {
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

    private static Integer getMaxDeep(HashSet<UriAndUri> setClassAndSubclass, String className, String subclassName) {
        Integer maxDeepTemp = 1;
        HashSet<UriAndUri> tempSet = new HashSet<>(setClassAndSubclass);
        tempSet.removeIf(resource -> !resource.getUri1().equals(className));
        while (!tempSet.isEmpty()) {
            HashSet<UriAndUri> nextSet = new HashSet<>();
            for (UriAndUri resource : tempSet) {
                if (resource.getUri2().equals(className)) {
                    return Integer.MAX_VALUE; // Infinite loop
                }
                nextSet.addAll(setClassAndSubclass.stream()
                        .filter(r -> r.getUri1().equals(resource.getUri2()))
                        .collect(Collectors.toSet()));
            }
            tempSet = nextSet;
            maxDeepTemp++;
        }
        return maxDeepTemp;
    }
}
