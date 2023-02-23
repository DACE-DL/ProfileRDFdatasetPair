package inferencesAndQueries;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;

import com.fasterxml.jackson.databind.ObjectMapper;

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

		// Récupération du nom du fichier contenant la liste des ontologies à traiter.
		Path pathOfTheListDatasets = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameListSourceDatasets);					
		// Récupération du nom des fichiers d'ontologies dans listSourceDatasetsFileName
		ArrayList<String> listSourceDatasetsFileName = new ArrayList<String>();	
		listSourceDatasetsFileName = ProfilingUtil.makeListFileName(pathOfTheListDatasets.toString()); 

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
		
		// Récupération du nom du fichier pour les résultat.
		Path pathOfTheFileResults = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameResultsQueries);
		// Récupération du nom du fichier des résultat dans fileNameResults
		String fileNameResults = ProfilingUtil.makeFileName(pathOfTheFileResults.toString()); 
		
		// Récupération du nom du fichier contenant les paramètres.
		Path pathOfTheParameters = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameParameters);
		// Récupération du top spatial
		String topSpatial = ProfilingUtil.extractParameter(pathOfTheParameters.toString(), "topSpatial"); 	
		//System.out.println("topSpatial : " + topSpatial);
		
		// Création du model inféré
		InfModel infModel = CreateInferedModel.createInferedModel(listSourceDatasetsFileName, listRulesFileName, topSpatial);
		// Execution des requêtes sur le modèle inféré
		listQueriesOutputs = QueryInferedModel.queryInferedModel(listQueriesFileName, infModel);
	    
		// Sauvegarde des résultats dans fichier JSON	
		Path pathOfTheResultsFile = Paths.get(ProfilingConf.folderForResults, fileNameResults);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(new File(pathOfTheResultsFile.toString()), listQueriesOutputs);
		
	}
	
	public static void InferencesAndQuery(ArrayList<String> listOntologies, ArrayList<String> listRules, String topSpatial, ArrayList<ProfilingQueryObject> listQueries) throws Exception {
	    InfModel infModel = CreateInferedModel.createInferedModel(listOntologies, listRules, topSpatial);
	    QueryInferedModel.queryInferedModel(infModel, listQueries);
	}

}