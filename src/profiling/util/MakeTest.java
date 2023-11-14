package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeTest {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndUri> make(Model model) {
		
		new ProfilingConf();
		
		String prefix = ProfilingConf.queryPrefix;
		
		ArrayList<UriAndUriAndUri> ListResources = new ArrayList<UriAndUriAndUri>();
		
		Query query = QueryFactory.create(prefix + 
				" SELECT ?propertyGroup (COUNT(*) AS ?usageCount) " +
				" WHERE {      " +
				"  { " +
				"    SELECT DISTINCT ?subject (GROUP_CONCAT(?property; separator=\" | \") AS ?propertyList) " +	
				"	 WHERE { " +	
				" 	 	?subject ?property ?object ." +	
				" 	 } " +	
				"	 GROUP BY ?subject " +	
				"   } " +	
				"   BIND (CONCAT(?propertyList) AS ?propertyGroup) " +	
				" } " +		
				" GROUP BY ?propertyGroup " +	
				" ORDER BY DESC(COUNT(*)) " 	  
					
					
					  
					
					
				  
				  
				
				
				
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				System.out.println(querySolution.toString()) ;
			}
		}
		
		return ListResources;
	}
}