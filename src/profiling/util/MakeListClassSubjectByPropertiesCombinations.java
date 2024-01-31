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

public class MakeListClassSubjectByPropertiesCombinations {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriListAndUriAndNumberListAndNumber> makeList(Model model, String nameOfList) {
		
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;
		String dsp = ProfilingConf.dsp;

		ArrayList<UriListAndUriAndNumberListAndNumber> ListResources = new ArrayList<UriListAndUriAndNumberListAndNumber>();

			Query query = QueryFactory.create(prefix + 
			" SELECT ?propertyList (GROUP_CONCAT(DISTINCT ?classAndCount; separator=\"|\") AS ?classAndCountList) (SUM(?usageCount) AS ?instanceCombinaisonPropertyCount) " +
			" WHERE {      " +
			"  	 { " +
			"	 SELECT ?propertyList (COUNT(?subject) AS ?usageCount)  (CONCAT(STR( ?class ), ';', STR(COUNT(?class))) AS ?classAndCount)" +
			"	 WHERE {      " +
			"	  	 { " +
			"	    SELECT ?subject ?class (GROUP_CONCAT(DISTINCT ?property; separator=\"|\") AS ?propertyList)  { " +	
			"	    	SELECT ?subject ?property ?class" +	
			"		 	WHERE { " +	
			"				BIND( '' AS ?default_class) " +
			"	 	 		?subject ?property ?object ." +	
			"	           	OPTIONAL { ?subject rdf:type ?subjectClass } " +
			"				BIND(COALESCE(?subjectClass, ?default_class) as ?class) " +
			"				FILTER ( !STRSTARTS(str(?property),\"" + dsp + "\") && " + 
			"	 				?property NOT IN ( <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>, <http://www.w3.org/1999/02/22-rdf-syntax-ns#first>, <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> ) " +
			"	     		) " +
			"	 	 	} " +	
			"		 	ORDER BY ?property ?class " +
			"		 } " +
			"	    GROUP BY ?subject ?class " +	
			"	    } " +	
			"	 } " +		
			"	 GROUP BY ?propertyList ?class ?classAndCount" +	
			"	 ORDER BY DESC(?usageCount) " +
			"    } " +	
			" } " +		
			" GROUP BY ?propertyList " +	
			" ORDER BY DESC(?instanceCombinaisonPropertyCount) " +
			" LIMIT 100 " 	  	  
			);
		
			//System.out.println("Query : " + query.toString()); 

			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			if (result.hasNext()) {
				while( result.hasNext() ) {
					QuerySolution querySolution = result.next() ;
					String[] elements = querySolution.getLiteral("propertyList").getString().split("\\|");
					ArrayList<Uri> ListProperty = new ArrayList<Uri>();
					for (String element : elements) {
						Uri uri = new Uri(element);
						ListProperty.add(uri);
					}
					Collections.sort(ListProperty, new UriComparator());
					ArrayList<UriAndNumber> ListClass = new ArrayList<UriAndNumber>();
					if (!(querySolution.getLiteral("classAndCountList")==null)) {
						String[] classAndCounts = querySolution.getLiteral("classAndCountList").getString().split("\\|");
						
						for (String classAndCount : classAndCounts) {
							String[] classCount = classAndCount.split("\\;");
							Integer number = Integer.valueOf(classCount[1]);
							UriAndNumber uriAndNumber = new UriAndNumber(classCount[0], number);
							ListClass.add(uriAndNumber);
						}
					}
			
					UriListAndUriAndNumberListAndNumber uriListAndUriAndNumberListAndNumber = new UriListAndUriAndNumberListAndNumber();
					uriListAndUriAndNumberListAndNumber.setUriList(ListProperty);
					uriListAndUriAndNumberListAndNumber.setUriAndNumberList(ListClass);
					uriListAndUriAndNumberListAndNumber.setNumber(querySolution.getLiteral("?instanceCombinaisonPropertyCount").getInt());
					ListResources.add(uriListAndUriAndNumberListAndNumber) ;
				}
			}
		
		return ListResources;
	}
	
	static class UriComparator implements java.util.Comparator<Uri> {
		@Override
		public int compare(Uri a, Uri b) {
			return GiveLocalname.giveName(a.getUri()).compareTo(GiveLocalname.giveName(b.getUri()));
		}
	}
	
}