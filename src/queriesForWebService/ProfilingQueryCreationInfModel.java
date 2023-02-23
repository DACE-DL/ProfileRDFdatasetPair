package queriesForWebService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import inferencesAndQueries.CreateInferedModel;
import profiling.util.*;

public class ProfilingQueryCreationInfModel {
	
    public static void creationModel() throws Exception {
    	// Initialisation de la configuration
    			// Chemin d'accès, noms fichiers...
    			new ProfilingConf(); 
    			
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
    			
    			// Récupération du nom du fichier contenant les paramètres.
    			Path pathOfTheParameters = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameParameters);
    			// Récupération du top spatial
    			String topSpatial = ProfilingUtil.extractParameter(pathOfTheParameters.toString(), "topSpatial"); 	
    			//System.out.println("topSpatial : " + topSpatial);
    		
    	ProfilingSingletonInfModel.setModel(CreateInferedModel.createInferedModel(listSourceDatasetsFileName, listRulesFileName, topSpatial));
	}
}