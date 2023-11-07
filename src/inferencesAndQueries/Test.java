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

		/* // Récupération du nom du fichier contenant les paramètres.
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
		System.out.println("Equal ? : " + consoleOutput.toString().equalsIgnoreCase(trueInString.toString())); */
		// ArrayList<UriAndNumber> listUriAndNumber = new ArrayList<UriAndNumber>();
		// listUriAndNumber.add(new UriAndNumber("www.bibi", 999));
		// listUriAndNumber.add(new UriAndNumber("www.bibi2", 888));
		// ProfilingUtil.makeJsonUriAndNumberFile(listUriAndNumber, "listeBibi");
		
		// ArrayList<UriAndNumber> listUriAndNumberReturn = new ArrayList<UriAndNumber>();
		// listUriAndNumberReturn = ProfilingUtil.makeArrayListUriAndNumber("listeBibi");
		// System.out.println("liste : " + listUriAndNumberReturn);
	
		//ProfilingUtil.ChangeDirectoryFiles(ProfilingConf.folderForTmp, ProfilingConf.mainFolderProfiling + "/" + "results/test");
		// Path pathOfTheDirectory = Paths.get(ProfilingConf.folderForDatasets);
		// Path pathFileDataset = Paths.get(pathOfTheDirectory.toString(), "reference.xml");
		// Dataset dataset = TDBUtil.CreateTDBDataset();
		// Model model = dataset.getDefaultModel();
		// dataset.begin(ReadWrite.WRITE);
		// model.enterCriticalSection(Lock.WRITE);
		// try {
    	// 	Reader messageReader = new StringReader(pathFileDataset.toString());
    	// 	model.read(messageReader, null);
    	// 	dataset.commit();
		// } catch (Exception e) {
    	// e.printStackTrace();
		// } finally {
    	// model.leaveCriticalSection() ;
    	// dataset.end();	
		// }
		// // Récupération du nom du fichier contenant la liste des ontologies à traiter pour le jeux de données source et target.
		// Path pathOfTheListPairDatasets = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameListPairDatasets);					
		// // Récupération du nom des fichiers d'ontologies dans listSourceDatasetsFileName
		// ArrayList<PairOfDatasets> listPairDatasetsFileName = new ArrayList<PairOfDatasets>();	
		// listPairDatasetsFileName = ProfilingUtil.makeListPairFileName(pathOfTheListPairDatasets.toString()); 

		// for(PairOfDatasets pairOfDatasets: listPairDatasetsFileName){
		// 	System.out.println(pairOfDatasets.getFilesSource().get(0).getName());
		// 	System.out.println(pairOfDatasets.getFilesTarget().get(0).getName());
		// }
		String idPair = "MyPair" ;
		Path pathForLptResults = Paths.get(ProfilingConf.folderForResults, idPair);
		Path pathForSourceResults = Paths.get(ProfilingConf.folderForResults, idPair, "source");
		Path pathForTargetResults = Paths.get(ProfilingConf.folderForResults, idPair, "target");
		ProfilingPostProcessing.makeTreatements(pathForSourceResults.toString(), pathForTargetResults.toString());
		ProfilingUtil.ChangeDirectoryFiles(ProfilingConf.folderForTmp, pathForLptResults.toString());
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}