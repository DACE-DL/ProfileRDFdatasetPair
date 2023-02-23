package queriesForWebService;

import java.time.Duration;
import java.time.Instant;

import profiling.util.ProfilingConf;


public class ProfilingQueryMain {

	public static void main(String[] args) throws Exception {
		Instant start = Instant.now();
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf();
		
		ProfilingQueryCreationInfModel.creationModel();

		Instant end = Instant.now();
		System.out.println("Durée d'exécution : " + Duration.between(start, end).getSeconds() + " secondes");
		
		ProfilingQueryInfModel.main(null);
		ProfilingQueryInfModel.main(null);
		
		ProfilingQueryCreationInfModel.creationModel();
		
		ProfilingQueryInfModel.main(null);
		ProfilingQueryInfModel.main(null);
		
	}
}		