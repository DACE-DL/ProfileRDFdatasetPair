package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListPropertyUsageCount {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndNumber> makeList(Model model) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndNumber> ListResources = new ArrayList<UriAndNumber>();
		
		Query query = QueryFactory.create(prefix + 
		"SELECT ?property (COUNT(?property) AS ?propertyUsage) " +
		" WHERE { " +
			" ?s ?property ?o ." +
		" } GROUP BY ?property ORDER BY DESC (?propertyUsage) " 
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new UriAndNumber(querySolution.getResource("property").toString(), querySolution.getLiteral("propertyUsage").getInt())) ;
			}
		}
		return ListResources;
	}
}