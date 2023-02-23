package databaseManagement;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import profiling.util.ProfilingConf;
import profiling.util.ProfilingUtil;
import profiling.util.TDBUtil;

public class TDBInitialisation {

	public static void main(final String[] args) throws Exception {

		initialisation();

	}
	public static void initialisation() throws Exception {

		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf(); 

		// Récupération du nom du fichier contenant la liste des datasets à traiter.
		Path pathOfTheListDatasets = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameListDatasetsForInitTDB);
			
		// Récupération du nom du répertoire des datasets à traiter dans la configuration
		Path pathOfTheDirectory = Paths.get(ProfilingConf.folderForDatasets);
	
		// Récupération du nom des fichiers des datasets dans listSourceDatasetsFileName
		ArrayList<String> listSourceDatasetsFileName = new ArrayList<String>();	
		listSourceDatasetsFileName = ProfilingUtil.makeListFileName(pathOfTheListDatasets.toString()); 

		// Pour toutes les ontologies
		for (int i = 0; i < listSourceDatasetsFileName.size(); i++) {
			String fileName = listSourceDatasetsFileName.get(i);
			// Un nom pour la création du graphe TDB à partir du nom de l'ontologie
			String nameForGraphURI = fileName.replaceFirst("[.][^.]+$", "");
			
			System.out.println("The dataset " + fileName + " is being loaded");   
			
			try {			

				// Effacement des statements contenus dans le graphe TDB
				TDBUtil.DeleteTDBContent(nameForGraphURI);

				//lecture du fichier json	
				Path pathFileOntology = Paths.get(pathOfTheDirectory.toString() , fileName);
				String jsonString = ProfilingUtil.readFileAsString(pathFileOntology.toString());

				TDBUtil.InputJsonStringToTDB("JSON-LD", jsonString , nameForGraphURI);
				
				System.out.println("Graph size " + nameForGraphURI + " : " + TDBUtil.SizeOfTDBContent(nameForGraphURI));
				
			} 
			catch (Exception e) {System.out.println("big problem :" + e.toString());
			}	
		}   
		System.out.println("End of the transfer of datasets to TDB");
	}

}




