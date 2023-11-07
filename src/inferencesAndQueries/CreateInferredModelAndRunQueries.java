package inferencesAndQueries;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;

import com.fasterxml.jackson.databind.ObjectMapper;

import profiling.util.FileName;
import profiling.util.PairOfDatasets;
import profiling.util.ProfilingConf;
import profiling.util.ProfilingQueryObject;
import profiling.util.ProfilingQueryOutputObject;
import profiling.util.ProfilingUtil;

public class CreateInferredModelAndRunQueries {
	
	public static void InferencesAndQuery() throws Exception {
	    
		ArrayList<ProfilingQueryOutputObject> listQueriesOutputs = new ArrayList<ProfilingQueryOutputObject>();
		
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf(); 
		
		//System.out.println("mainFolderProfiling : " + ProfilingConf.mainFolderProfiling);
		//System.out.println("fileNameListSourceDatasets : " + ProfilingConf.fileNameListSourceDatasets);

		// Récupération du nom du fichier contenant la liste des ontologies à traiter pour le jeux de données source et target.
		Path pathOfTheListPairDatasets = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameListPairDatasets);					
		// Récupération du nom des fichiers d'ontologies dans listPairDatasetsFileName pour les jeux de données source et target
		ArrayList<PairOfDatasets> listPairDatasetsFileName = new ArrayList<PairOfDatasets>();	
		listPairDatasetsFileName = ProfilingUtil.makeListPairFileName(pathOfTheListPairDatasets.toString()); 
		
		for(PairOfDatasets pairOfDatasets: listPairDatasetsFileName){
			// Il peut y avoir plusieurs ontologies pour le jeux de données source et pour le jeux de données target. 

			// Récupération de l'identifiant de la paire de datasets.
			String idPair = pairOfDatasets.getIdPair();
			
			// Récupération du nom des ontologies à traiter pour le jeux de données source.
			ArrayList<String> listSourceDatasetsFileName = new ArrayList<String>();
			for(FileName filename: pairOfDatasets.getFilesSource()){
				listSourceDatasetsFileName.add(filename.getName());
			}
			
			// Récupération du nom des ontologies à traiter pour le jeux de données target.
			ArrayList<String> listTargetDatasetsFileName = new ArrayList<String>();
			for(FileName filename: pairOfDatasets.getFilesTarget()){
				listTargetDatasetsFileName.add(filename.getName());
			}

			// Récupération du nom du fichier contenant la liste des règles à traiter.
			Path pathOfTheListRules = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameListRules);
			// Récupération du nom des fichiers des régles dans listRulesFileName
			ArrayList<String> listRulesFileName = new ArrayList<String>();	
			listRulesFileName = ProfilingUtil.makeListFileName(pathOfTheListRules.toString()); 
			
			// Récupération du nom du fichier contenant la liste des requêtes à traiter.
			Path pathOfTheListQueries = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameListQueries);
			// Récupération du nom des fichiers des régles dans listQueriesFileName
			ArrayList<String> listQueriesFileName = new ArrayList<String>();	
			listQueriesFileName = ProfilingUtil.makeListFileName(pathOfTheListQueries.toString());
			
			
			// Récupération du nom du fichier des résultat dans fileNameSourceResults
			String fileNameSourceResults = ProfilingConf.fileNameSourceResultsQueries; 

			
			// Récupération du nom du fichier des résultat dans fileNameSourceResults
			String fileNameTargetResults = ProfilingConf.fileNameTargetResultsQueries;
			
			// Récupération du nom du fichier contenant les paramètres.
			Path pathOfTheParameters = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameParameters);
			// Récupération du top spatial
			String topSpatial = ProfilingUtil.extractParameter(pathOfTheParameters.toString(), "topSpatial"); 	
			//System.out.println("topSpatial : " + topSpatial);
			String consoleOutput = ProfilingUtil.extractParameter(pathOfTheParameters.toString(), "consoleOutput");
			//System.out.println("console output : " + consoleOutput); 
			//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			
			
			
			//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			// Pour le jeux de données Source 
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("Processing the Source dataset");
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			// Création du model inféré
			InfModel infModel = CreateInferedModel.createInferedModel(listSourceDatasetsFileName, listRulesFileName, topSpatial);
			// Execution des requêtes sur le modèle inféré
			listQueriesOutputs = QueryInferedModel.queryInferedModel(listQueriesFileName, infModel, consoleOutput);
			// Sauvegarde des résultats des queries dans fichier JSON	
			Path pathOfTheResultsFile = Paths.get(ProfilingConf.folderForTmp, fileNameSourceResults);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(new File(pathOfTheResultsFile.toString()), listQueriesOutputs);
			
			Path pathForSourceResults = Paths.get(ProfilingConf.folderForResults, idPair, "source");
			ProfilingUtil.ChangeDirectoryFiles(ProfilingConf.folderForTmp, pathForSourceResults.toString());
			
			// Pour le jeux de données Target
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("Processing the Target dataset");
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			infModel = CreateInferedModel.createInferedModel(listTargetDatasetsFileName, listRulesFileName, topSpatial);
			// Execution des requêtes sur le modèle inféré
			listQueriesOutputs = QueryInferedModel.queryInferedModel(listQueriesFileName, infModel, consoleOutput);
			// Sauvegarde des résultats dans fichier JSON	
			pathOfTheResultsFile = Paths.get(ProfilingConf.folderForTmp, fileNameTargetResults);
			objectMapper.writeValue(new File(pathOfTheResultsFile.toString()), listQueriesOutputs);
			
			Path pathForTargetResults = Paths.get(ProfilingConf.folderForResults, idPair, "target");
			ProfilingUtil.ChangeDirectoryFiles(ProfilingConf.folderForTmp, pathForTargetResults.toString());
			//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			// Pour la determination des LPT
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("Processing of LPT determination");
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			ProfilingPostProcessing.makeTreatements(pathForSourceResults.toString(), pathForTargetResults.toString());
			Path pathForLptResults = Paths.get(ProfilingConf.folderForResults, idPair);
			ProfilingUtil.ChangeDirectoryFiles(ProfilingConf.folderForTmp, pathForLptResults.toString());

		}
	}
	
	public static void InferencesAndQuery(ArrayList<String> listDatasets, ArrayList<String> listRules,
	 	String topSpatial, ArrayList<ProfilingQueryObject> listQueries, String consoleOutput, String fileNameResult) throws Exception {

		ArrayList<ProfilingQueryOutputObject> listQueriesOutputs = new ArrayList<ProfilingQueryOutputObject>();

	    InfModel infModel = CreateInferedModel.createInferedModel(listDatasets, listRules, topSpatial);
	    listQueriesOutputs = QueryInferedModel.queryInferedModel(infModel, listQueries, consoleOutput);
		
		// Récupération du nom du fichier pour les résultat du jeux de données target.
		Path pathOfTheResultsFile = Paths.get(ProfilingConf.folderForResults, fileNameResult);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(new File(pathOfTheResultsFile.toString()), listQueriesOutputs);
		
	}

}