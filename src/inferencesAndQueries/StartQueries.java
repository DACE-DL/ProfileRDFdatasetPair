package inferencesAndQueries;

import java.time.Duration;
import java.time.Instant;
// Import log4j classes.
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class StartQueries {

	// Lancement initial pour création modèle inferé et traitement des requêtes
	// Les fichiers déposés sur le serveur (\var\www\profiling) servent à paramétrer le modéle
	//  à inférer (choix des ontologies, choix des régles à appliquer...) et les requêtes
	//  à executer

    // Define a static logger variable so that it references the
    // Logger instance named "MyApp".
    private static final Logger logger = LogManager.getLogger(StartQueries.class);

	public static void main(String[] args) throws Exception {
		
		System.out.println("log4j.configurationFile :: " + System.getProperty("log4j.configurationFile"));
		
		logger.trace("Entering application.");
		
		Instant start0 = Instant.now();
		
		CreateInferredModelAndRunQueries.InferencesAndQuery();
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}