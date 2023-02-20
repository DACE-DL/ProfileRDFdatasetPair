package queriesForWebService;

import java.time.Duration;
import java.time.Instant;
import aobra.util.AobraConf;


public class AobraQueryMain {

	public static void main(String[] args) throws Exception {
		Instant start = Instant.now();
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new AobraConf();
		
		AobraQueryCreationInfModel.creationModel();

		Instant end = Instant.now();
		System.out.println("Durée d'exécution : " + Duration.between(start, end).getSeconds() + " secondes");
		
		AobraQueryInfModel.main(null);
		AobraQueryInfModel.main(null);
		
		AobraQueryCreationInfModel.creationModel();
		
		AobraQueryInfModel.main(null);
		AobraQueryInfModel.main(null);
		
	}
}		