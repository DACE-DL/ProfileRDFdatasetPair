package queriesForWebService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import aobra.util.*;
import inferencesAndQueries.AobraCreateInferedModel;

public class AobraQueryCreationInfModel {
	
    public static void creationModel() throws Exception {
    	// Initialisation de la configuration
    			// Chemin d'accès, noms fichiers...
    			new AobraConf(); 
    			
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
    			
    			// Récupération du nom du fichier contenant les paramètres.
    			Path pathOfTheParameters = Paths.get(AobraConf.mainFolderAobra, AobraConf.fileNameParameters);
    			// Récupération du top spatial
    			String topSpatial = AobraUtil.extractParameter(pathOfTheParameters.toString(), "topSpatial"); 	
    			//System.out.println("topSpatial : " + topSpatial);
    		
    	AobraSingletonInfModel.setModel(AobraCreateInferedModel.createInferedModel(listOntologiesFileName, listRulesFileName, topSpatial));
	}
}