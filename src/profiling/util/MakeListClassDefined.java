package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListClassDefined {
	
	// Création d'une liste des classes definies.
	public static ArrayList<Uri> makeList(Model model) {
		
		String prefix = ProfilingConf.queryPrefix;
		
		ArrayList<Uri> ListResources = new ArrayList<Uri>();
		
		Query query = QueryFactory.create(prefix + 
				"SELECT DISTINCT (?s AS ?class) " +
				" WHERE { " +
				" ?s rdf:type ?o ." +
				" FILTER isIRI(?o) ." +
				" FILTER (?o=rdfs:Class||?o=owl:Class) ." +
				" FILTER (?s != <http://www.w3.org/2002/07/owl#Class>) " +
				" FILTER (?s != <http://www.w3.org/2000/01/rdf-schema#Class>) " +
				" FILTER (?s != <http://www.w3.org/2004/02/skos/core#Concept>) " +
				" FILTER (?s != <http://www.w3.org/2002/07/owl#Thing>) " +
				" FILTER (?s != <http://www.w3.org/2002/07/owl#Nothing>) " +
				" FILTER (?s != <http://www.w3.org/1999/02/22-rdf-syntax-ns#List>) " +  
				" } " );			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new Uri(querySolution.getResource("class").toString())) ;
			}
		}

		return ListResources;
	}
}