package queriesForWebService;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;

import profiling.util.ProfilingConf;


public class ProfilingQueryInit {

	public static void init() throws Exception {
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;

		////////////////////////////////
		// Recupèration des parcelles //
		////////////////////////////////
		
		
		
		InfModel infModel = ProfilingSingletonInfModel.getModel();
		// showModelSize( infModel );

		String stringQuery = prefix + "SELECT (?plot AS ?id) ?prefLabel  " + 
				"WHERE {" +
				" ?plot res:isPlot ?true ." +
				" ?plot skos:prefLabel ?prefLabel ." +
				" }" +
				" ORDER BY ?plot " ;
		
		Query query = QueryFactory.create(stringQuery);
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);		
		ResultSet resultSet = qe.execSelect();
		while (resultSet.hasNext()) {
			@SuppressWarnings("unused")
			QuerySolution result = resultSet.next();
		} 
	}
}