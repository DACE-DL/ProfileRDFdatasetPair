package inferencesAndQueries;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import profiling.util.ProfilingConf;
import profiling.util.ProfilingUtil;

public class StartVerification {

	// Lancement initial pour la verification des paires
	// Les fichiers déposés sur le serveur (\var\www\profiling) servent à paramétrer le modéle
	//  à inférer (choix des ontologies, choix des régles à appliquer...) et les requêtes
	//  à executer

	public static void main(String[] args) throws Exception {
		new ProfilingConf();
		// org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StartVerification.class);
		// logger.info("This is an info message");
		Instant start0 = Instant.now();
		String formattedDate = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    	.withZone(ZoneId.systemDefault())
    	.format(Instant.now());
		System.out.println("Start time : " + formattedDate);

		CheckingGraphs.Checking();
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
	}  
}