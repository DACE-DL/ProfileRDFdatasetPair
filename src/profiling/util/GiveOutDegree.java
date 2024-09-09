package profiling.util;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class GiveOutDegree {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static double giveDegree(Model model) {
		double outdegree = 0.0;
		
		String prefix = ProfilingConf.queryPrefix;

		Query query = QueryFactory.create(prefix + 
			"SELECT (COUNT(?s)/COUNT(DISTINCT ?s) AS ?usage) " +
			" WHERE { " +
			" ?s ?p ?o ." +
			" FILTER isIRI(?s) " +
			" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			QuerySolution querySolution = result.next();
			outdegree = querySolution.getLiteral("usage").getDouble();
		}
		return outdegree;
	}	
}