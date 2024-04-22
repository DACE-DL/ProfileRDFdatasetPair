package profiling.util;

import java.util.ArrayList;
import org.apache.jena.rdf.model.Model;

public class GivePropertyHierarchyDeep {
	
	public static HierarchyDeepAndLoop giveHierarchyDeepAndLoop(Model model, ArrayList<UriAndUri> listPropertyAndSubproperty ) {
		HierarchyDeepAndLoop hierarchyDeepAndLoop = new HierarchyDeepAndLoop();
		

			Integer nNumber = 0;
			ArrayList<UriAndUri> ListResourcesTemp = new ArrayList<UriAndUri>();
			ArrayList<UriAndUri> ListResourcesTemp1 = new ArrayList<UriAndUri>();
			ArrayList<UriAndUri> ListResourcesTemp2 = new ArrayList<UriAndUri>();
			Integer maxDeep = 0;
			Integer maxDeepTemp = 0;
			String propertyName ="";
			String subpropertyName ="";
			String propertyNameTemp ="";
			Boolean infiniteLoop = false;
			Boolean infiniteLoopTemp = false;
			Integer n = 0;
        	Integer nMax = 100;        
        	if (listPropertyAndSubproperty.size() > nMax) {
            	System.out.println("Due to the large size of the list of properties and their subproperties (" + listPropertyAndSubproperty.size() + "), the hierarchy depth search and infinite loop processing will be limited.");
        	}
			if (listPropertyAndSubproperty.size() > 0) {
				// If there is a list, it means that there is already a first level of hierarchy
				maxDeep = 1;
				// Finding the maximum hierarchical depth
				
				// Duplication of the list for searches
				ListResourcesTemp = listPropertyAndSubproperty ;
				// Each line of the list of propertyes and their subpropertyes is processed
				for (UriAndUri resource : listPropertyAndSubproperty) {
				    maxDeepTemp = 1;
					propertyName = resource.getUri1();
					subpropertyName = resource.getUri2();
					propertyNameTemp = subpropertyName;
					
					// For a subproperty, we want to know if it is in the original list as a property
					ListResourcesTemp1.clear();
					for (UriAndUri resourceTemp : ListResourcesTemp) {
						// We get all the lines where it appears as a property
						if (resourceTemp.getUri1().equals(propertyNameTemp)) {
							ListResourcesTemp1.add(new UriAndUri(resourceTemp.getUri1(), resourceTemp.getUri2())) ;
						}	
					}
					
					// Create a temporary list to store items to be deleted
					ArrayList<UriAndUri> elementsToDelete = new ArrayList<>();

					while (!ListResourcesTemp1.isEmpty()) {	
						maxDeepTemp = maxDeepTemp + 1;
						ListResourcesTemp2.clear();
						for (UriAndUri resourceTemp1 : ListResourcesTemp1) {
							// We are looking for an Infinite Loop
							if (resourceTemp1.getUri2().equals(propertyName)) {
								infiniteLoopTemp = true;
								// Ajoutez l'élément à supprimer à la liste temporaire
								elementsToDelete.add(resourceTemp1);
							} 

							for (UriAndUri resourceTemp : ListResourcesTemp) {
								if (resourceTemp.getUri1().equals(resourceTemp1.getUri2())) {
									ListResourcesTemp2.add(new UriAndUri(resourceTemp.getUri1(), resourceTemp.getUri2())) ;
								}	
							}
						}	
						
						if (!ListResourcesTemp2.isEmpty()) {
							if (!infiniteLoopTemp) {
								ListResourcesTemp1.addAll(ListResourcesTemp2);
							} else {
								ListResourcesTemp1.clear();
							}
						} else {
							ListResourcesTemp1.clear();
						}
					}
					// Delete items from the main list
					ListResourcesTemp1.removeAll(elementsToDelete);

					if (infiniteLoopTemp) {
						infiniteLoop = true;
						infiniteLoopTemp = false;
					}
					if (maxDeepTemp > maxDeep) {
						maxDeep = maxDeepTemp;
					}
					if (n > nMax) {
						break;
					}	
				}	
				nNumber = maxDeep;
			}	
			// The output parameter
			hierarchyDeepAndLoop.setHierarchyDeep(nNumber);
			hierarchyDeepAndLoop.setLoop(infiniteLoop);

		return hierarchyDeepAndLoop;
	}	
}