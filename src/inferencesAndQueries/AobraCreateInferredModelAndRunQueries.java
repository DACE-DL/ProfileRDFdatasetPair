package inferencesAndQueries;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;

import com.fasterxml.jackson.databind.ObjectMapper;

import aobra.util.AobraConf;
import aobra.util.AobraQueryObject;
import aobra.util.AobraQueryOutputObject;
import aobra.util.AobraUtil;

public class AobraCreateInferredModelAndRunQueries {
	
	public static void InferencesAndQuery() throws Exception {
	    
		ArrayList<AobraQueryOutputObject> listQueriesOutputs = new ArrayList<AobraQueryOutputObject>();
		
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new AobraConf(); 
		
		//System.out.println("mainFolderAobra : " + AobraConf.mainFolderAobra);
		//System.out.println("fileNameListOntologies : " + AobraConf.fileNameListOntologies);

		// Récupération du nom du fichier contenant la liste des ontologies à traiter.
		Path pathOfTheListOntologies = Paths.get(AobraConf.mainFolderAobra, AobraConf.fileNameListOntologies);					
		// Récupération du nom des fichiers d'ontologies dans listOntologiesFileName
		ArrayList<String> listOntologiesFileName = new ArrayList<String>();	
		listOntologiesFileName = AobraUtil.makeListFileName(pathOfTheListOntologies.toString()); 

		// Récupération du nom du fichier contenant la liste des règles à traiter.
		Path pathOfTheListRules = Paths.get(AobraConf.mainFolderAobra, AobraConf.fileNameListRules);
		// Récupération du nom des fichiers des régles dans listRulesFileName
		ArrayList<String> listRulesFileName = new ArrayList<String>();	
		listRulesFileName = AobraUtil.makeListFileName(pathOfTheListRules.toString()); 
		
		// Récupération du nom du fichier contenant la liste des requêtes à traiter.
		Path pathOfTheListQueries = Paths.get(AobraConf.mainFolderAobra, AobraConf.fileNameListQueries);
		// Récupération du nom des fichiers des régles dans listQueriesFileName
		ArrayList<String> listQueriesFileName = new ArrayList<String>();	
		listQueriesFileName = AobraUtil.makeListFileName(pathOfTheListQueries.toString());
		
		// Récupération du nom du fichier pour les résultat.
		Path pathOfTheFileResults = Paths.get(AobraConf.mainFolderAobra, AobraConf.fileNameResultsQueries);
		// Récupération du nom du fichier des résultat dans fileNameResults
		String fileNameResults = AobraUtil.makeFileName(pathOfTheFileResults.toString()); 
		
		// Récupération du nom du fichier contenant les paramètres.
		Path pathOfTheParameters = Paths.get(AobraConf.mainFolderAobra, AobraConf.fileNameParameters);
		// Récupération du top spatial
		String topSpatial = AobraUtil.extractParameter(pathOfTheParameters.toString(), "topSpatial"); 	
		//System.out.println("topSpatial : " + topSpatial);
		
		// Création du model inféré
		InfModel infModel = AobraCreateInferedModel.createInferedModel(listOntologiesFileName, listRulesFileName, topSpatial);
		// Execution des requêtes sur le modèle inféré
		listQueriesOutputs = AobraQueryInferedModel.queryInferedModel(listQueriesFileName, infModel);
	    
		// Sauvegarde des résultats dans fichier JSON	
		Path pathOfTheResultsFile = Paths.get(AobraConf.folderForResults, fileNameResults);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(new File(pathOfTheResultsFile.toString()), listQueriesOutputs);
		
	}
	
	public static void InferencesAndQuery(ArrayList<String> listOntologies, ArrayList<String> listRules, String topSpatial, ArrayList<AobraQueryObject> listQueries) throws Exception {
	    InfModel infModel = AobraCreateInferedModel.createInferedModel(listOntologies, listRules, topSpatial);
	    AobraQueryInferedModel.queryInferedModel(infModel, listQueries);
	}

}