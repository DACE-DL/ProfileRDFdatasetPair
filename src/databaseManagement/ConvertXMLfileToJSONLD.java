package databaseManagement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import profiling.util.ProfilingConf;
import profiling.util.ProfilingUtil;
import profiling.util.TDBUtil;


public class ConvertXMLfileToJSONLD {

	// Lancement initial 
	public static void main(String[] args) throws Exception {
	
	
		Instant start0 = Instant.now();
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf();
		
		// Récupération du nom du fichier contenant la liste des datasets à traiter.
		Path pathOfTheListDatasets = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameListDatasetsForJSONLDconversion);
			
		// Récupération du nom du répertoire des datasets à traiter dans la configuration
		Path pathOfTheDirectory = Paths.get(ProfilingConf.folderForDatasets);
	
		// Récupération du nom des fichiers des datasets dans listSourceDatasetsFileName
		ArrayList<String> listSourceDatasetsFileName = new ArrayList<String>();	
		listSourceDatasetsFileName = ProfilingUtil.makeListFileName(pathOfTheListDatasets.toString()); 
	   
		long heapsize = Runtime.getRuntime().totalMemory();
		long maxSize = Runtime.getRuntime().maxMemory();
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		System.out.println("The features of your JAVA machine: ");
        System.out.println("Total memory is: " + heapsize);
		System.out.println("Max memory is: " + maxSize);
		System.out.println("The number of available processors is: " + availableProcessors);
		
		// Pour touts les datasets
		for (int i = 0; i < listSourceDatasetsFileName.size(); i++) {
			String fileName = listSourceDatasetsFileName.get(i);			
			Path pathFileDataset = Paths.get(pathOfTheDirectory.toString(), fileName);
			// Un nom pour la création du graphe TDB à partir du nom de l'ontologie
			String nameForGraphURI = fileName.replaceFirst("[.][^.]+$", "");

			// On vérifie l'extension .xml du fichier puis on le converti
			if (fileName.matches("^.*xml$")) {
				System.out.println("Converting the file " + pathFileDataset.toString() + " to a json file");

				

				InputStream is = new FileInputStream(pathFileDataset.toString());
				try {
					// Effacement des statements contenus dans le graphe TDB
					TDBUtil.DeleteTDBContent(nameForGraphURI);
					System.out.println("delete of TDB graph OK");
					// Tranfert du contenu du fichier dans le graphe TDB
					TDBUtil.InputInputStreamContentToTDB(null, is, nameForGraphURI);
				} catch (FileNotFoundException e) {
					System.out.println("File not found");
				} catch (IOException e) {
					System.out.println("IO problem");
				} catch (Exception e) {System.out.println("problem " + e );
				}

				System.out.println("Graph size " + nameForGraphURI + " : " + TDBUtil.SizeOfTDBContent(nameForGraphURI));
				System.out.println("Converting the file.....");

				Instant end = Instant.now();
				System.out.println("Running time : " + Duration.between(start0, end).getSeconds() + " secondes");

				Path pathOut = Paths.get(pathOfTheDirectory.toString(), nameForGraphURI + ".json");
				try {
					TDBUtil.OutputOfTDBContentToFile("JSON-LD", pathOut.toString(), nameForGraphURI);
				} catch (FileNotFoundException e) {
					System.out.println("File not found");
				} catch (IOException e) {
					System.out.println("IO problem");
				} 

				System.out.println("The file " + pathOut.toString() + " is created");
				end = Instant.now();
				System.out.println("Running time : " + Duration.between(start0, end).getSeconds() + " secondes");
			}

		}   
		System.out.println("End of XML to JSON-LD file conversion");
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}