package profiling.util;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class GivePropertyUsageDistinctPerObject {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static Usage giveUsage(Model model) {
		Usage usage = new Usage();
		
		String prefix = ProfilingConf.queryPrefix;
		
		int nObjectCount = 0;
		int nUsageSum = 0;
		double nUsageMean = 0.0 ;
		int nUsageMax = 0;
		int nUsageMin = 0;

		Query query = QueryFactory.create(prefix + 
			"SELECT DISTINCT (COUNT(?object) AS ?objectCount) (SUM(?usage) AS ?usageSum) (MIN(?usage) AS ?usageMin) (MAX(?usage) AS ?usageMax)" +
			" WHERE { " +
				"{ SELECT DISTINCT (?o AS ?object) (COUNT(DISTINCT ?property) AS ?usage) " +
				" WHERE { " +
				" ?s ?property ?o ." +
				" FILTER isIRI(?s) " +
				" } GROUP BY ?o }" +
			" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			QuerySolution querySolution = result.next() ;
			nObjectCount = querySolution.getLiteral("objectCount").getInt();
			nUsageSum = querySolution.getLiteral("usageSum").getInt();
			nUsageMin = querySolution.getLiteral("usageMin").getInt();
			nUsageMax = querySolution.getLiteral("usageMax").getInt();	
		}
		usage.setResourceCount(nObjectCount);
		usage.setUsageSum(nUsageSum);
		nUsageMean = (double) nUsageSum / (double) nObjectCount;	
		usage.setUsageMean(nUsageMean);
		usage.setUsageMin(nUsageMin);
		usage.setUsageMax(nUsageMax);

		return usage;
	}	
}