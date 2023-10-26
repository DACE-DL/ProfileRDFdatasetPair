package profiling.util;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class GiveClassHierarchyDeep {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static HierarchyDeepAndLoop giveHierarchyDeepAndLoop(Model model, String nameOfList) {
		HierarchyDeepAndLoop hierarchyDeepAndLoop = new HierarchyDeepAndLoop();
		
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;

		Query query = QueryFactory.create(prefix + 
		"SELECT DISTINCT (?o AS ?class) (?s AS ?subclass) " +
		" WHERE { " +
		" ?s rdfs:subClassOf ?o ." +
		" FILTER isIRI(?o) " +
		" FILTER isIRI(?s) " +
		" FILTER (?o != <http://www.w3.org/2002/07/owl#Thing>)" +
		" FILTER (?s != <http://www.w3.org/2002/07/owl#Nothing>)" +
		" } ORDER BY ?s ?o "
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
			String className ="";
			String subclassName ="";
			String classNameTemp ="";
			Boolean infiniteLoopTemp = false;
			
			if (result.hasNext()) {
				// If there is a list, it means that there is already a first level of hierarchy
				maxDeep = 1;
				while( result.hasNext() ) {
					QuerySolution querySolution = result.next() ;
					ListResources.add(new UriAndUri(querySolution.getResource("class").toString(), querySolution.getResource("Subclass").toString())) ;
				}
				// Finding the maximum hierarchical depth
				
				// Duplication of the list for searches
				ListResourcesTemp = ListResources ;
				// Each line of the list of classes and their subclasses is processed
				for (UriAndUri resource : ListResources) {
				    maxDeepTemp = 1;
					className = resource.getUri1();
					subclassName = resource.getUri2();
					classNameTemp = subclassName;
					
					// For a subclass, we want to know if it is in the original list as a class
					ListResourcesTemp1.clear();
					for (UriAndUri resourceTemp : ListResourcesTemp) {
						// We get all the lines where it appears as a class
						if (resourceTemp.getUri1().equals(classNameTemp)) {
							ListResourcesTemp1.add(new UriAndUri(resourceTemp.getUri1(), resourceTemp.getUri2())) ;
						}	
					}
					
					while (!ListResourcesTemp1.isEmpty()) {	
						maxDeepTemp = maxDeepTemp + 1;
						ListResourcesTemp2.clear();
						for (UriAndUri resourceTemp1 : ListResourcesTemp1) {
							// We are looking for an Infinite Loop
							if (resourceTemp1.getUri2().equals(className)) {
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