package databaseManagement;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.system.AsyncParser;
import profiling.util.ProfilingConf;
import profiling.util.ProfilingUtil;
import profiling.util.TDBUtil;

public class TDBVerification {

	public static void main(final String[] args) throws Exception {

		initialisation();

	}
	public static void initialisation() throws Exception {
		
		Instant start0 = Instant.now();
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf(); 

		//System.out.println(ProfilingConf.mainFolderProfiling);
		//System.out.println(ProfilingConf.fileNameListDatasetsForInitTDB);
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
			
			
			// Si le fichier à l'extention .json 
				Dataset dataset = TDBUtil.CreateTDBDataset();
				try {  
					// Effacement des statements contenus dans le graphe TDB
		    		System.out.println("Deletion of the TDB model");
					dataset.begin(ReadWrite.WRITE);
					dataset.removeNamedModel(nameForGraphURI);
					System.out.println("Reading the JSON-LD file");
					//System.out.println(pathFileDataset.toString());
					//String jsonString = ProfilingUtil.readFileAsString(pathFileDataset.toString());
					// InputStream is = new FileInputStream(pathFileDataset.toString());
					Model model = dataset.getNamedModel(nameForGraphURI);
					
					try (Stream<Triple> stream = AsyncParser.of(pathFileDataset.toString())
            			.setQueueSize(1).setChunkSize(100000).streamTriples()) {
        				// Do something with the stream
						stream.forEach(triple -> {
							model.createResource(triple.getSubject().toString())
         					.addProperty(model.createProperty(triple.getPredicate().getURI()), triple.getObject().toString());
						});
					};

					// IteratorCloseable<Triple> it = AsyncParser.of(pathFileDataset.toString()).asyncParseTriples();
    				// try {
        			// 	while (it.hasNext()) {
            		// 	Triple triple = it.next();
            		// 		System.out.println(triple.toString());
        			// 	}
    				// } finally {
        			// 	Iter.close(it);
    				// }
					
					// Read JSON File and put it in model
					//StringReader in = new StringReader(jsonString);
					
					System.out.println("Triplet registration in the TDB model");
					// read the RDF/JSON in model
					//model.read(is, null, typeOfSerialization);
					//model.read(is, null);
					System.out.println("Ok ....................................................");
					System.out.println("Graph size " + nameForGraphURI + " : " + model.size());
					dataset.commit();
					dataset.close();    
					
					model.close();
				}
				//catch (FileNotFoundException e) {System.out.println("File not found : " + e);}
				//catch (IOException e) {System.out.println("IO problem : " + e );}
				catch (Exception e) {System.out.println("big problem : " + e.toString());e.printStackTrace();}
				finally { 
					//dataset.abort();
					dataset.end(); 
				}
			
			
			
	    }   
		System.out.println("End of the transfer of datasets to TDB");
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
	}

}




