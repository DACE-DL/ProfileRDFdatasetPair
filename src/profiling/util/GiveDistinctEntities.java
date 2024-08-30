package profiling.util;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class GiveDistinctEntities {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static Integer giveNumber(Model model) {
		
		String prefix = ProfilingConf.queryPrefix;
		
		Integer nNumber = 0;
		
		Query query = QueryFactory.create(prefix + 
			" SELECT (COUNT(DISTINCT ?iri) AS ?i) " +
			" WHERE { " +
			"  { " +
			"    SELECT ?iri " +
			"    WHERE { " +
			"      { SELECT (?s AS ?iri) WHERE { ?s ?p ?o . FILTER(isIRI(?s)) } } " +
			"      UNION " +
			"      { SELECT (?p AS ?iri) WHERE { ?s ?p ?o . FILTER(isIRI(?p)) } } " +
			"      UNION " +
			"      { SELECT (?o AS ?iri) WHERE { ?s ?p ?o . FILTER(isIRI(?o)) } } " +
			"    } " +
			"  } " +
			" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			QuerySolution querySolution = result.next() ;
			nNumber = querySolution.getLiteral("i").getInt();
			
		}
		return nNumber;
	}	
}