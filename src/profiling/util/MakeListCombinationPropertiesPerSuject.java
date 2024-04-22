package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListCombinationPropertiesPerSuject {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriListAndUriListAndNumberListAndNumber> makeList(Model model) {
		Instant start0 = Instant.now();
		String prefix = ProfilingConf.queryPrefix;
		String dsp = ProfilingConf.dsp;

		ArrayList<UriListAndUriListAndNumberListAndNumber> ListCombinationPropertiesPerSuject = new ArrayList<UriListAndUriListAndNumberListAndNumber>();

		Query query = QueryFactory.create(prefix + 
		" SELECT ?propertyList " +
		" (GROUP_CONCAT(?classListAndCount; separator=\"*\") AS ?classListAndCountList)  "	+ 
		" (SUM(?usageCount) AS ?instanceCombinaisonPropertyCount) " +
		" WHERE {      " +
		"	  { " +
		" 		SELECT ?propertyList " +
		" 		((CONCAT(?classList, ';', STR(?usageCount))) AS ?classListAndCount)  "	+ 
		"       ?usageCount  " +
		" 		WHERE {      " +
		" 			{ " +
		"	 		  SELECT ?propertyList ?classList " +
		" 			  (COUNT(?subject) AS ?usageCount) " +
		"		   	  WHERE {      " +
		"			   	  { " +
		"				    SELECT ?subject (GROUP_CONCAT(DISTINCT ?property; separator=\"|\") AS ?propertyList) " +
		"       			(GROUP_CONCAT(DISTINCT ?class; separator=\"|\") AS ?classList) "	+ 
		"  			        { " +	
		"			    	SELECT ?subject ?property ?class" +	
		"				 	WHERE { " +	
		"						BIND( '' AS ?default_class) " +
		"			 	 		?subject ?property ?object ." +	
		"			           	OPTIONAL { ?subject rdf:type ?subjectClass } " +
		"						BIND(COALESCE(?subjectClass, ?default_class) as ?class) " +
		"						FILTER ( !STRSTARTS(str(?property),\"" + dsp + "\") && " + 
		"		 				?property NOT IN ( <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>, <http://www.w3.org/1999/02/22-rdf-syntax-ns#first>, <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> ) " +
		"			     		) " +
		"			 	 	} " +	
		"				 	ORDER BY ?subject ?property ?class " +
 		"				    } " +
		"				    GROUP BY ?subject " +	
		"	    		} " +	
		"	    		} " +		
		"	 			GROUP BY ?propertyList ?classList " +	
		"    		} " +	
		" 			} " 	+
		" 			ORDER BY ?propertyList DESC(?usageCount) " +
		" 		} " 	+
		" 		} " 	+
		" 		GROUP BY ?propertyList " +
		"       ORDER BY DESC(?instanceCombinaisonPropertyCount) "  	  
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
					ArrayList<UriListAndNumber> ListClass = new ArrayList<UriListAndNumber>();
					if (!(querySolution.getLiteral("classListAndCountList")==null)) {
						String[] classListAndCounts = querySolution.getLiteral("classListAndCountList").getString().split("\\*");
						
						for (String classListAndCount : classListAndCounts) {
							String[] classListCount = classListAndCount.split("\\;");
							Integer number = Integer.valueOf(classListCount[1]);
							String[] classList = classListCount[0].split("\\|");
							ArrayList<Uri> classUriList = new ArrayList<Uri>();
							for (String classString : classList) {
								Uri uri = new Uri(classString);
								classUriList.add(uri);
							}
							UriListAndNumber uriListAndNumber = new UriListAndNumber(classUriList, number);
							ListClass.add(uriListAndNumber);
						}
					}
			
					UriListAndUriListAndNumberListAndNumber uriListAndUriListAndNumberListAndNumber = new UriListAndUriListAndNumberListAndNumber();
					uriListAndUriListAndNumberListAndNumber.setUriList(ListProperty);
					uriListAndUriListAndNumberListAndNumber.setUriListAndNumberList(ListClass);
					uriListAndUriListAndNumberListAndNumber.setNumber(querySolution.getLiteral("?instanceCombinaisonPropertyCount").getInt());
					ListCombinationPropertiesPerSuject.add(uriListAndUriListAndNumberListAndNumber) ;
				}
			}

		Instant end0 = Instant.now();
		System.out.println("Running time for ListCombinationPropertiesPerSuject: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return ListCombinationPropertiesPerSuject;
	}
	
	static class UriComparator implements java.util.Comparator<Uri> {
		@Override
		public int compare(Uri a, Uri b) {
			return GiveLocalname.giveName(a.getUri()).compareTo(GiveLocalname.giveName(b.getUri()));
		}
	}
	
}