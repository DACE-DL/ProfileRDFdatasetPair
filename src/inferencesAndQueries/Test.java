package inferencesAndQueries;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

import profiling.util.ProfilingConf;
import profiling.util.ProfilingUtil;

public class Test {

	public static void main(String[] args) throws Exception {
		
		
		Instant start0 = Instant.now();
		
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf(); 

		// Récupération du nom du fichier contenant les paramètres.
		Path pathOfTheParameters = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameParameters);

		String trueInString = "true";
		String okInString = "Ok";

		String consoleOutput = ProfilingUtil.extractParameter(pathOfTheParameters.toString(), "consoleOutput");
		System.out.println("console output : " + consoleOutput); 
		System.out.println("Equal ? : " + consoleOutput.equalsIgnoreCase("true"));
		System.out.println("Equal ? : " + consoleOutput.toString().equalsIgnoreCase("true"));

		System.out.println("true en string : " + trueInString );
		System.out.println("true en string : " + trueInString.toString());

		System.out.println("Ok en string : " + okInString );
		System.out.println("Ok en string : " + okInString.toString());
		
		System.out.println("Equal ? : " + consoleOutput.toString().equalsIgnoreCase(trueInString));
		System.out.println("Equal ? : " + consoleOutput.toString().equalsIgnoreCase(trueInString.toString()));
		
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}