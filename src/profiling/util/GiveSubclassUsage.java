package profiling.util;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class GiveSubclassUsage {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static Integer giveUsage(Model model) {
		Integer nNumber = 0;
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;

		Query query = QueryFactory.create(prefix + 
			"SELECT (COUNT(*) AS ?usage) " +
			" WHERE { " +
			" ?s rdfs:subClassOf ?o ." +
				" FILTER isIRI(?o) " +
				" FILTER isIRI(?s) " +
			" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			QuerySolution querySolution = result.next();	
			nNumber = querySolution.getLiteral("usage").getInt();
		}
		return nNumber;
	}	
}