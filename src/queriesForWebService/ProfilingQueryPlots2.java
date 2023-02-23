package queriesForWebService;

import java.time.Duration;
import java.time.Instant;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;

import profiling.util.ProfilingConf;


public class ProfilingQueryPlots2 {

	public static void main(String[] args) throws Exception {
		Instant start = Instant.now();
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf();
		String UserLanguage = ProfilingConf.preferredLanguage;
		
		ProfilingQueryCreationInfModel.creationModel();
		
		ResultSet resultSet = PlotsList(UserLanguage);
		
		Instant end2 = Instant.now();
		System.out.println("Durée d'exécution : " + Duration.between(start, end2).getSeconds() + " secondes");
		ResultSetFormatter.out(System.out, resultSet);
	}

	public static ResultSet PlotsList(String UserLanguage) throws Exception {
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;

		////////////////////////////////
		// Recupèration des parcelles //
		////////////////////////////////
		
		
		
		InfModel infModel = ProfilingSingletonInfModel.getModel();
		// showModelSize( infModel );

		String stringQuery = prefix + "SELECT  (?plot AS ?id) (?plot AS ?gps) ?prefLabel " + 
				"WHERE {" +
				" ?plot res:isPlot ?true ." +
				" ?plot skos:prefLabel ?prefLabel ." +
				" }" +
				" ORDER BY ?plot " ;

		Query query = QueryFactory.create(stringQuery);
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);		
		ResultSet resultSet = qe.execSelect();

		return resultSet;
	}
	
	
	protected static void showModelSize( InfModel m ) {
        System.out.println( String.format( "The model contains %d triples", m.size() ) );
    }
}