package databaseManagement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

		long heapsize = Runtime.getRuntime().totalMemory();
		long maxSize = Runtime.getRuntime().maxMemory();
		int availableProcessors = Runtime.getRuntime().availableProcessors();

        System.out.println("Total memory is: " + heapsize);
		System.out.println("Max memory is: " + maxSize);
		System.out.println("The number of available processors is: " + availableProcessors);
		
		// Pour touts les datasets
		for (int i = 0; i < listSourceDatasetsFileName.size(); i++) {

			String fileName = listSourceDatasetsFileName.get(i);
			// Un nom pour la création du graphe TDB à partir du nom de l'ontologie
			String nameForGraphURI = fileName.replaceFirst("[.][^.]+$", "");

			Path pathFileDataset = Paths.get(pathOfTheDirectory.toString() , fileName);
			
			System.out.println("The dataset " + pathFileDataset.toString() + " is being loaded");   
			
			String typeOfSerialization = null;
			// Si le fichier à l'extention .json 
			if(fileName.matches("^.*json$")) {
				typeOfSerialization = "JSON-LD";
				try {  
					// Effacement des statements contenus dans le graphe TDB
					TDBUtil.DeleteTDBContent(nameForGraphURI); 
					String jsonString = ProfilingUtil.readFileAsString(pathFileDataset.toString());
					TDBUtil.InputJsonStringToTDB(typeOfSerialization, jsonString , nameForGraphURI);
					System.out.println("Graph size " + nameForGraphURI + " : " + TDBUtil.SizeOfTDBContent(nameForGraphURI));
				}
				catch (FileNotFoundException e) {System.out.println("File not found");}
				catch (IOException e) {System.out.println("IO problem");}
				catch (Exception e) {System.out.println("big problem: " + e.toString());}
			} else
			{
				try {  
					// Effacement des statements contenus dans le graphe TDB
					TDBUtil.DeleteTDBContent(nameForGraphURI); 
					InputStream is = new FileInputStream(pathFileDataset.toString());
					TDBUtil.InputInputStreamContentToTDB(null, is, nameForGraphURI);
	
					System.out.println("Graph size " + nameForGraphURI + " : " + TDBUtil.SizeOfTDBContent(nameForGraphURI));
				}
				catch (FileNotFoundException e) {System.out.println("File not found");}
				catch (IOException e) {System.out.println("IO problem");}
				catch (Exception e) {System.out.println("big problem: " + e.toString());}
			}
			
			
	    }   
		System.out.println("End of the transfer of datasets to TDB");
	}

}




