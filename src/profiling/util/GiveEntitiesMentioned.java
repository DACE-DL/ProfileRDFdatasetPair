package profiling.util;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class GiveEntitiesMentioned {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static Integer giveNumber(Model model) {
		
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;
		
		Integer nNumber = 0;
		
		Query query = QueryFactory.create(prefix + 
			" SELECT (COUNT(*) AS ?triplesCount) " +
				" WHERE { " +
				" ?s ?property ?o ." +
				" FILTER isIRI(?s) " +
				" FILTER isIRI(?o) " +
			" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			QuerySolution querySolution = result.next() ;
			nNumber = querySolution.getLiteral("triplesCount").getInt();
			
		}
		return nNumber;
	}	
}