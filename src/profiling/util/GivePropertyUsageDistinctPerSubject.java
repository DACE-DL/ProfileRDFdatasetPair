package profiling.util;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class GivePropertyUsageDistinctPerSubject {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static Usage giveUsage(Model model) {
		Usage usage = new Usage();
		
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;
		
		int nSubjectCount = 0;
		int nUsageSum = 0;
		double nUsageMean = 0.0 ;
		int nUsageMax = 0;
		int nUsageMin = 0;

		Query query = QueryFactory.create(prefix + 
			"SELECT DISTINCT (COUNT(?subject) AS ?subjectCount) (SUM(?usage) AS ?usageSum) (MIN(?usage) AS ?usageMin) (MAX(?usage) AS ?usageMax)" +
				" WHERE { " +
					"{ SELECT DISTINCT (?s AS ?subject) (COUNT(DISTINCT ?property) AS ?usage) " +
					" WHERE { " +
					"dsp:listPropertyUsageCount rdf:rest*/rdf:first ?element ." +
					"?element dsp:asURI ?property ." +
					" ?s ?property ?o ." +
					" FILTER isIRI(?s) " +
					" } GROUP BY ?s }" +
				" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			QuerySolution querySolution = result.next() ;
			nSubjectCount = querySolution.getLiteral("subjectCount").getInt();
			nUsageSum = querySolution.getLiteral("usageSum").getInt();
			nUsageMin = querySolution.getLiteral("usageMin").getInt();
			nUsageMax = querySolution.getLiteral("usageMax").getInt();	
		}
		usage.setResourceCount(nSubjectCount);
		usage.setUsageSum(nUsageSum);
		nUsageMean = (double) nUsageSum / (double) nSubjectCount;	
		usage.setUsageMean(nUsageMean);
		usage.setUsageMin(nUsageMin);
		usage.setUsageMax(nUsageMax);

		return usage;
	}	
}