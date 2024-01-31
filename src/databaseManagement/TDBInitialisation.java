package databaseManagement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.shared.Lock;

import profiling.util.FileName;
import profiling.util.PairOfDatasets;
import profiling.util.ProfilingConf;
import profiling.util.ProfilingUtil;
import profiling.util.TDBUtil;

public class TDBInitialisation {

	public static void main(final String[] args) throws Exception {

		initialisation();

	}
	public static void initialisation() throws Exception {
		
		Instant start0 = Instant.now();
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf(); 

		// Récupération du nom du répertoire des datasets à traiter dans la configuration
		String mainDirectoryForDatasets = ProfilingConf.folderForDatasets;
	
		// Récupération du nom du fichier contenant la liste des datasets à traiter.
		Path pathOfTheListDatasets = Paths.get(ProfilingConf.mainFolderProfiling, ProfilingConf.fileNameListDatasetsForInitTDB);
			
		// Récupération du nom des fichiers d'ontologies dans listPairDatasetsFileName pour les jeux de données source et target
		ArrayList<PairOfDatasets> listPairDatasetsFileName = new ArrayList<PairOfDatasets>();	
		listPairDatasetsFileName = ProfilingUtil.makeListPairFileName(pathOfTheListDatasets.toString()); 
		
		long heapsize = Runtime.getRuntime().totalMemory();
		long maxSize = Runtime.getRuntime().maxMemory();
		int availableProcessors = Runtime.getRuntime().availableProcessors();

        System.out.println("Total memory is: " + heapsize);
		System.out.println("Max memory is: " + maxSize);
		System.out.println("The number of available processors is: " + availableProcessors);
			
		for(PairOfDatasets pairOfDatasets: listPairDatasetsFileName){
			// Il peut y avoir plusieurs ontologies pour le jeux de données source et pour le jeux de données target. 

			// Récupération de l'identifiant de la paire de datasets.
			String idPair = pairOfDatasets.getIdPair();

			// Récupération du tag de la paire de datasets.
			String pairActive = pairOfDatasets.getPairActive();
			

			if (pairActive.equals("yes") ) {

				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				System.out.println("Loading files from the directory " + idPair);
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
	
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

				//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				
				treatment(mainDirectoryForDatasets, idPair, listSourceDatasetsFileName);
				treatment(mainDirectoryForDatasets, idPair, listTargetDatasetsFileName);

			}
		}
		
		System.out.println("End of the transfer of datasets to TDB");
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}




	public static void treatment(String mainDirectoryForDatasets, String idPair, ArrayList<String> listDatasetsFileName) throws Exception {
		String idPairForGraphURI = idPair.replaceFirst("[.][^.]+$", "");
		// Pour touts les datasets
		for (int i = 0; i < listDatasetsFileName.size(); i++) {

			String fileName = listDatasetsFileName.get(i);
			// Un nom pour la création du graphe TDB à partir du nom de l'ontologie
			String nameForGraphURI = fileName.replaceFirst("[.][^.]+$", "");

			Path pathFileDataset = Paths.get(mainDirectoryForDatasets, idPair, fileName);
			
			System.out.println("The dataset " + pathFileDataset.toString() + " is being loaded");   
			
			String typeOfSerialization = null;
			// Si le fichier à l'extention .json 
			if(fileName.matches("^.*json$")) {
				typeOfSerialization = "JSONLD";
				Dataset dataset = TDBUtil.CreateTDBDataset();
				try {  
					// Effacement des statements contenus dans le graphe TDB
		    		System.out.println("Deletion of the TDB model");
					dataset.begin(ReadWrite.WRITE);
					dataset.removeNamedModel(idPairForGraphURI + nameForGraphURI);
					System.out.println("Reading the JSON-LD file");
					//System.out.println(pathFileDataset.toString());
					//String jsonString = ProfilingUtil.readFileAsString(pathFileDataset.toString());
					InputStream is = new FileInputStream(pathFileDataset.toString());
					Model model = dataset.getNamedModel(idPairForGraphURI + nameForGraphURI);
					model.enterCriticalSection(Lock.WRITE);
					// Read JSON File and put it in model
					//StringReader in = new StringReader(jsonString);
					
					System.out.println("Triplet registration in the TDB model");
					// read the RDF/JSON in model
					model.read(is, null, typeOfSerialization);
					//model.read(is, null);
					System.out.println("Ok ....................................................");
					System.out.println("Graph size " + idPairForGraphURI + nameForGraphURI + " : " + model.size());
					dataset.commit();
					dataset.close();    
					model.leaveCriticalSection();
					model.close();
				}
				catch (FileNotFoundException e) {System.out.println("File not found : " + e);}
				//catch (IOException e) {System.out.println("IO problem : " + e );}
				catch (Exception e) {System.out.println("big problem : " + e.toString());e.printStackTrace();}
				finally { 
					//dataset.abort();
					dataset.end(); 
				}
			} else
			{
				Dataset dataset = TDBUtil.CreateTDBDataset();
				try {  
					// Effacement des statements contenus dans le graphe TDB
					dataset.begin(ReadWrite.WRITE);
					dataset.removeNamedModel(idPairForGraphURI + nameForGraphURI); 
					Model model = dataset.getNamedModel(idPairForGraphURI + nameForGraphURI);
					model.enterCriticalSection(Lock.WRITE);
					// Read JSON File and put it in model
					InputStream is = new FileInputStream(pathFileDataset.toString());
					// read the RDF/JSON files in model
					model.read(is, null );
					System.out.println("Graph size " + idPairForGraphURI + nameForGraphURI + " : " + model.size());
					dataset.commit();    
					dataset.close();
					model.leaveCriticalSection();
					model.close();
				}
				catch (FileNotFoundException e) {System.out.println("File not found");}
				catch (Exception e) {System.out.println("big problem: " + e.toString()); e.printStackTrace();}
				finally { 
					//dataset.abort();
					dataset.end(); 
				}
			}
	    }   
	}	
}




