package profiling.util;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class GivePropertyHierarchyDeep {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static HierarchyDeepAndLoop giveHierarchyDeepAndLoop(Model model, String nameOfList) {
		HierarchyDeepAndLoop hierarchyDeepAndLoop = new HierarchyDeepAndLoop();
		
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;

			Query query = QueryFactory.create(prefix + 
			"SELECT ?Property ?Subproperty WHERE { <" + nameOfList + "> rdf:rest*/rdf:first ?element ." +
			" ?element dsp:asProperty ?Property ." +
			" ?element dsp:asSubproperty ?Subproperty ." +		
			" } " 
			);

			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			// ResultSetFormatter.out(System.out, result);
			Integer nNumber = 0;
			ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
			ArrayList<UriAndUri> ListResourcesTemp = new ArrayList<UriAndUri>();
			ArrayList<UriAndUri> ListResourcesTemp1 = new ArrayList<UriAndUri>();
			ArrayList<UriAndUri> ListResourcesTemp2 = new ArrayList<UriAndUri>();
			Integer maxDeep = 0;
			Integer maxDeepTemp = 0;
			String propertyName ="";
			String subpropertyName ="";
			String propertyNameTemp ="";
			Boolean infiniteLoopTemp = false;
			
			if (result.hasNext()) {
				// If there is a list, it means that there is already a first level of hierarchy
				maxDeep = 1;
				while( result.hasNext() ) {
					QuerySolution querySolution = result.next() ;
					ListResources.add(new UriAndUri(querySolution.getResource("Property").toString(), querySolution.getResource("Subproperty").toString())) ;
				}
				// Finding the maximum hierarchical depth
				
				// Duplication of the list for searches
				ListResourcesTemp = ListResources ;
				// Each line of the list of propertyes and their subpropertyes is processed
				for (UriAndUri resource : ListResources) {
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
					
					while (!ListResourcesTemp1.isEmpty()) {	
						maxDeepTemp = maxDeepTemp + 1;
						ListResourcesTemp2.clear();
						for (UriAndUri resourceTemp1 : ListResourcesTemp1) {
							// We are looking for an Infinite Loop
							if (resourceTemp1.getUri2().equals(propertyName)) {
								infiniteLoopTemp = true;
								// We break the processing of the loop
								ListResourcesTemp1.clear();
							} 

							for (UriAndUri resourceTemp : ListResourcesTemp) {
								if (resourceTemp.getUri1().equals(resourceTemp1.getUri2())) {
									ListResourcesTemp2.add(new UriAndUri(resourceTemp.getUri1(), resourceTemp.getUri2())) ;
								}	
							}
						}	
						
						if (!ListResourcesTemp2.isEmpty()) {
							if (!infiniteLoopTemp) {
								ListResourcesTemp1 = ListResourcesTemp2;
							} else {
								ListResourcesTemp1.clear();
							}
						} else {
							ListResourcesTemp1.clear();
						}
					}
					if (infiniteLoopTemp) {
						infiniteLoopTemp = false;
					}
					if (maxDeepTemp > maxDeep) {
						maxDeep = maxDeepTemp;
					}	
				}	
				nNumber = maxDeep;
			}	
			// The output parameter
			hierarchyDeepAndLoop.setHierarchyDeep(nNumber);
			hierarchyDeepAndLoop.setLoop(infiniteLoopTemp);

		return hierarchyDeepAndLoop;
	}	
}