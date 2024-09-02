package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListMaxPerProperty {

	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndValue> makeList(Model model) {

		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriAndValue> ListResources = new ArrayList<UriAndUriAndValue>();

		Query query = QueryFactory.create(prefix +
			"SELECT ?property ?datatype (Max(?o) AS ?usage) " +
			" WHERE { " +
			" ?s ?property ?o ." +
			"  FILTER ( " +
    		"    (datatype(?o) = xsd:integer || datatype(?o) = xsd:float || datatype(?o) = xsd:double || datatype(?o) = xsd:decimal || datatype(?o) = xsd:dateTime) " +
    		"    && str(?o) != '' " +  // Ajout de cette condition pour exclure les valeurs vides
    		"  ) " +
			" } GROUP BY ?property (datatype(?o) AS ?datatype) ORDER BY DESC (?usage)"
		);
			
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while (result.hasNext()) {
				QuerySolution querySolution = result.next();
				// System.out.println("datatype : " + querySolution.get("datatype"));
				// System.out.println("usage : " + querySolution.getLiteral("usage"));
				if (querySolution.getLiteral("usage") != null) {
					ListResources.add(new UriAndUriAndValue(new Uri(querySolution.get("property").toString()),
					new Uri(querySolution.get("datatype").toString()), querySolution.getLiteral("usage").getValue().toString()));
				}
			}
		}
		return ListResources;
	}

}