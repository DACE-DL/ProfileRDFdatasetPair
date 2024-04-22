package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListClassUsageCount {
	
	// Création d'une liste des clases et de leur usage dans un triplet
	public static ArrayList<UriAndNumber> makeList(Model model) {
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndNumber> ListResources = new ArrayList<UriAndNumber>();
		
		Query query = QueryFactory.create(prefix + 
				"SELECT DISTINCT (?o AS ?class) (COUNT(?s) AS ?usage) " +
				" WHERE { " +
				" ?s rdf:type ?o ." +
				" FILTER isIRI(?o) " +
				" FILTER (?o != <http://www.w3.org/2002/07/owl#Class>) " +
				" FILTER (?o != <http://www.w3.org/2000/01/rdf-schema#Class>) " +
				" FILTER (?o != <http://www.w3.org/2004/02/skos/core#Concept>) " +
				" FILTER (?o != <http://www.w3.org/2002/07/owl#Thing>) " +
				" FILTER (?o != <http://www.w3.org/2002/07/owl#Nothing>) " +
				" FILTER (?o != <http://www.w3.org/1999/02/22-rdf-syntax-ns#List>) " +  
				" FILTER (?o != <http://www.w3.org/2002/07/owl#AnnotationProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#ObjectProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#DatatypeProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#FunctionalProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#InverseFunctionalProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#TransitiveProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#SymmetricProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#AsymmetricProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#ReflexiveProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#IrreflexiveProperty>) " + 
				" FILTER (?o != <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#Ontology>) " + 
				" } GROUP BY ?o ORDER BY DESC (?usage)"
		);			
		
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new UriAndNumber(querySolution.getResource("class").toString(), querySolution.getLiteral("usage").getInt())) ;
			}
		}
		Collections.sort(ListResources, new UriAndNumberComparator());
		return ListResources;
	}

	static class UriAndNumberComparator implements java.util.Comparator<UriAndNumber> {
		@Override
		public int compare(UriAndNumber a, UriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}