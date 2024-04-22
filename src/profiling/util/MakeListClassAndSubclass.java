package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListClassAndSubclass {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUri> makeList(Model model) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
		
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
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new UriAndUri(querySolution.getResource("class").toString(), querySolution.getResource("subclass").toString())) ;
			}
		}

		
		return ListResources;
	}
}