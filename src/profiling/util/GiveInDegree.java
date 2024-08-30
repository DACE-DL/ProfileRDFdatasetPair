package profiling.util;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class GiveInDegree {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static double giveDegree(Model model) {
		double indegree = 0.0;
		
		String prefix = ProfilingConf.queryPrefix;

		Query query = QueryFactory.create(prefix + 
			"SELECT (COUNT(?o)/COUNT(DISTINCT ?o) AS ?usage) " +
			" WHERE { " +
			//" ?p rdf:type <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> ." +
			" ?s ?p ?o ." +
			" FILTER isIRI(?p) " +
			" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			QuerySolution querySolution = result.next();
			indegree = querySolution.getLiteral("usage").getDouble();
		}
		return indegree;
	}	
}