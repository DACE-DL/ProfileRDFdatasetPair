package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class VerifyResourceDeprecated {
	
	// Création liste des types des propriétés les plus utilisées.
	public static ArrayList<UriAndUri> makeList(Model model) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
	
		Query query = QueryFactory.create(prefix + 
			"SELECT ?resource ?deprecatedValue WHERE { " +
			" ?resource owl:deprecated ?deprecatedValue ." +
			" } ORDER BY ?resource "
		);
		
 		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				if ( querySolution.get("deprecatedValue").isResource() ) {
					ListResources.add(new UriAndUri(querySolution.getResource("resource").toString(), querySolution.getResource("deprecatedValue").toString()));
				}
				if ( querySolution.get("deprecatedValue").isLiteral() ) {
					ListResources.add(new UriAndUri(querySolution.getResource("resource").toString(), querySolution.getLiteral("deprecatedValue").toString()));
				}
			}
		}
		
		return ListResources;
	}
}