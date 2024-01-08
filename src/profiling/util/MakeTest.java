package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeTest {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndUri> make(Model model) {
		
		new ProfilingConf();
	
		String prefix = ProfilingConf.queryPrefix;
		
		ArrayList<UriAndUriAndUri> ListResources = new ArrayList<UriAndUriAndUri>();
		Instant start3 = Instant.now();
		Query query = QueryFactory.create(prefix + 	  
		"SELECT ?property (SUM(?usage) AS ?propertyUsage)  (GROUP_CONCAT(DISTINCT ?classSubjectclassObjectAndCount ; separator=\"|\") AS ?classSubjectclassObjectAndCountList) " +
		" WHERE { " +
			"SELECT ?property (COUNT(?s) AS ?usage) (CONCAT(STR( ?classSubject ), ';', STR(COUNT(?classSubject)), '|' ,STR( ?classObject ), ';', STR(COUNT(?classObject))) AS ?classSubjectclassObjectAndCount)" +
			" WHERE { " +
				" ?s ?property ?o ." +
				" ?s rdf:type ?classSubject ." +	
				" ?o rdf:type ?classObject " +		
				" FILTER EXISTS { " +
				" dsp:listPropertyMostUsed rdf:rest*/rdf:first ?element ." +
				" ?element dsp:asURI ?property ." +
				" } " +	
				" " +
			" } GROUP BY ?property ?classSubject ?classObject ORDER BY DESC (?usage) " +
		" } GROUP BY ?property ORDER BY DESC (?propertyUsage) LIMIT 100"
		
 

		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				System.out.println(querySolution.toString()) ;
			}
		}
		Instant end3 = Instant.now();
		System.out.println("test running time : " + Duration.between(start3, end3).getSeconds() + " secondes");
		
		return ListResources;
	}
}

// "SELECT *  " +
// 		" WHERE { " +
// 		"   ?s <http://www.w3.org/2000/01/rdf-schema#label> ?o . " +
// 		"   datatype(?o) = <http://www.w3.org/1999/02/22-rdf-syntax-ns#langString> ." +
// 		" } "


//"SELECT ?property (SUM(?usage) AS ?propertyUsage)  (GROUP_CONCAT(DISTINCT ?classObjectAndCount; separator=\"|\") AS ?classObjectAndCountList) (GROUP_CONCAT(DISTINCT ?classSubjectAndCount; separator=\"|\") AS ?classSubjectAndCountList)" +
		// " WHERE { " +
		// 	"SELECT ?property (COUNT(?s) AS ?usage) (CONCAT(STR( ?classObject ), ';', STR(COUNT(?classObject))) AS ?classObjectAndCount) (CONCAT(STR( ?classSubject ), ';', STR(COUNT(?classSubject))) AS ?classSubjectAndCount) " +
		// 	" WHERE { " +
		// 		" ?s ?property ?o ." +
		// 		" ?s rdf:type ?classSubject ." +	
		// 		" ?o rdf:type ?classObject " +		
		// 		" FILTER EXISTS { " +
		// 		" dsp:listPropertyMostUsed rdf:rest*/rdf:first ?element ." +
		// 		" ?element dsp:asURI ?property ." +
		// 		" } " +	
		// 		" " +
		// 	" } GROUP BY ?property ?classSubject ?classObject ORDER BY DESC (?usage) " +
		// " } GROUP BY ?property ORDER BY DESC (?propertyUsage) LIMIT 100"


// " SELECT ?propertyList (GROUP_CONCAT(DISTINCT ?classAndCount; separator=\"|\") AS ?classAndCountList) (SUM(?usageCount) AS ?instanceCombinaisonPropertyCount) " +
		// " WHERE {      " +
		// "  	 { " +
		// "	 SELECT ?propertyList (COUNT(?subject) AS ?usageCount)  (CONCAT(STR( ?class ), ';', STR(COUNT(?class))) AS ?classAndCount)" +
		// "	 WHERE {      " +
		// "	  	 { " +
		// "	    SELECT ?subject ?class (GROUP_CONCAT(DISTINCT ?property; separator=\"|\") AS ?propertyList)  { " +	
		// "	    	SELECT ?subject ?property ?class" +	
		// "		 	WHERE { " +	
		// "	 	 		?subject ?property ?object ." +	
		// "	           	OPTIONAL { ?subject rdf:type ?class } " +
		// "				FILTER ( !STRSTARTS(str(?property),\"" + dsp + "\") && " + 
		// "	 				?property NOT IN ( <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>, <http://www.w3.org/1999/02/22-rdf-syntax-ns#first>, <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> ) " +
		// "	     		) " +
		// "	 	 	} " +	
		// "		 	ORDER BY ?property ?class " +
		// "		 } " +
		// "	    GROUP BY ?subject ?class " +	
		// "	    } " +	
		// "	 } " +		
		// "	 GROUP BY ?propertyList ?class ?classAndCount" +	
		// "	 ORDER BY DESC(?usageCount) " +
		// "    } " +	
		// " } " +		
		// " GROUP BY ?propertyList " +	
		// " ORDER BY DESC(?instanceCombinaisonPropertyCount) " +
		// " LIMIT 10 " 	  
// " SELECT COUNT(DISTINCT ?instance) " +
// 				" WHERE {      " +
// 				"  ?instance <http://www.w3.org/2004/02/skos/core#broader> ?object1 ; " + 
// 				"    <http://www.w3.org/2000/01/rdf-schema#label> ?object2 ; " + 
// 				"    <http://www.w3.org/2004/02/skos/core#prefLabel> ?object3 . " +
// 				"	 MINUS { " +	
// 				"	  ?instance ?otherProperty ?otherObject . " +	
// 				"	  FILTER (?otherProperty NOT IN (<http://www.w3.org/2004/02/skos/core#broader>, " +
// 				"                                    <http://www.w3.org/2000/01/rdf-schema#label>, " +
// 				"                                    <http://www.w3.org/2004/02/skos/core#prefLabel>, " +
// 				"                                    <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>, " +
// 				"                                    <http://www.w3.org/1999/02/22-rdf-syntax-ns#first>, " +
// 				"                                    <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> " +
// 				"     ))" +
// 				"    } " +
// 				" } " 		