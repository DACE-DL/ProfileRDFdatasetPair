package Asupprimer;

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

import profiling.util.ProfilingConf;
import profiling.util.ProfilingUtil;
import profiling.util.UriAndUriAndUriAndNumber;


public class MakeListClassAndPropertyOfInterestCount {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndUriAndNumber> makeList(Model model) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriAndUriAndNumber> ListResources = new ArrayList<UriAndUriAndUriAndNumber>();
		
		Instant start0 = Instant.now();

		Query query = QueryFactory.create(prefix + 
		"SELECT ?property (SUM(?usage) AS ?propertyUsage)  (GROUP_CONCAT(DISTINCT ?classSubjectclassObjectAndCount ; separator=\"|\") AS ?classSubjectclassObjectAndCountList) " +
		" WHERE { " +
			"SELECT ?property (COUNT(?s) AS ?usage) (CONCAT(STR( ?classSubject ), ';', STR(COUNT(?classSubject)), '*' ,STR( ?classObject ), ';', STR(COUNT(?classObject))) AS ?classSubjectclassObjectAndCount)" +
			" WHERE { " +
				" ?s ?property ?o ." +
				" ?s rdf:type ?classSubject ." +	
				" ?o rdf:type ?classObject " +		
				" " +
			" } GROUP BY ?property ?classSubject ?classObject ORDER BY DESC (?usage) " +
		" } GROUP BY ?property ORDER BY DESC (?propertyUsage) LIMIT 100"
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				String property = querySolution.getResource("property").toString();
				if (!(querySolution.getLiteral("classSubjectclassObjectAndCountList")==null)) {
					String[] classSubjectclassObjectAndCounts = querySolution.getLiteral("classSubjectclassObjectAndCountList").getString().split("\\|");
					for (String classSubjectclassObjectAndCount : classSubjectclassObjectAndCounts) {
						String[] classSubjectclassObjectCount = classSubjectclassObjectAndCount.split("\\*");
						String[] classSubjectCount = classSubjectclassObjectCount[0].split("\\;");
						String[] classObjectCount = classSubjectclassObjectCount[1].split("\\;");
						ListResources.add(new UriAndUriAndUriAndNumber(
							classSubjectCount[0],
							property,
							classObjectCount[0],
							Integer.valueOf(classSubjectCount[1])
						));
					}
				}
			}
		}
							
		Instant end0 = Instant.now();
		System.out.println("All Class Subjet and Object and properties running time : " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));

		return ListResources;
	}

	static class UriAndUriAndUriAndNumberComparator implements java.util.Comparator<UriAndUriAndUriAndNumber> {
		@Override
		public int compare(UriAndUriAndUriAndNumber a, UriAndUriAndUriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}