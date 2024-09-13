package databaseManagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.shared.Lock;
import java.net.URLEncoder;

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
		System.out.println("Total running time : " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
	}


	public static void treatment(String mainDirectoryForDatasets, String idPair, ArrayList<String> listDatasetsFileName) throws Exception {
		String idPairForGraphURI = idPair.replaceFirst("[.][^.]+$", "");
		
		for (String fileName : listDatasetsFileName) {
			String nameForGraphURI = fileName.replaceFirst("[.][^.]+$", "");
			Path pathFileDataset = Paths.get(mainDirectoryForDatasets, idPair, fileName);
			System.out.println("The dataset " + pathFileDataset + " is being loaded");
	
			// Correction des erreurs du fichier
			modifyFile(pathFileDataset.toString());
	
			String typeOfSerialization = getFileSerializationType(fileName);
	
			Dataset dataset = null;
			try {
				dataset = TDBUtil.CreateTDBDataset();
				dataset.begin(ReadWrite.WRITE);
	
				// Effacement des statements contenus dans le graphe TDB
				dataset.removeNamedModel(idPairForGraphURI + nameForGraphURI);
	
				Model model = dataset.getNamedModel(idPairForGraphURI + nameForGraphURI);
				model.enterCriticalSection(Lock.WRITE);
				model.clearNsPrefixMap();
	
				// Lecture du fichier en fonction du type de sérialisation
				try (InputStream is = new FileInputStream(pathFileDataset.toString())) {
					model.read(is, "", typeOfSerialization);
				}
	
				System.out.println("Graph size " + idPairForGraphURI + nameForGraphURI + " : " + model.size());
				model.leaveCriticalSection();
				dataset.commit();
	
			} catch (FileNotFoundException e) {
				System.out.println("File not found: " + pathFileDataset + " : " + e);
			} catch (Exception e) {
				System.out.println("Error processing dataset: " + e.toString());
				e.printStackTrace();
			} finally {
				if (dataset != null) {
					dataset.end();
					dataset.close();
				}
			}
		}
	}
	
	/**
	 * Détection du type de sérialisation
	 */
	private static String getFileSerializationType(String fileName) {
		if (fileName.matches("^.*json$")) {
			return "JSONLD";
		} else if (fileName.matches("^.*ttl$")) {
			return "TTL";
		} else {
			return "RDF/XML";  // Default to RDF/XML if no specific type is detected
		}
	}
	
	/**
	 * Correction des problémes de typo du fichier
	 */
	public static void modifyFile(String filePath) {
		try {
			// Ouverture du fichier d'entrée et du fichier temporaire de sortie
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + ".tmp"), StandardCharsets.UTF_8));
	
			// Lecture et modification ligne par ligne
			String line;
			while ((line = reader.readLine()) != null) {
				// Modification des erreurs typographiques
				String modifiedLine = line.replaceAll("http:///", "http://")
					.replaceAll("https:///", "https://")
					.replaceAll("%5D%5(?!D)", "%5D%5D")
					.replaceAll("xmlns:ns3=\"http://dbkwik.webdatacommons.org/marvel.wikia.com/property/%3\"", "")
					.replaceAll("ns3:CdivAlign", "ns1:divAlign")
					.replaceAll("http://www.wikipedia.com:secrets_of_spiderman_revealed", "http://www.wikipedia.com/secrets_of_spiderman_revealed")
					.replaceAll("OntologyID\\(Anonymous-2\\)module1", "http://OntologyID/Anonymous_2/module1")
					.replaceAll("ِAlRay_AlAam", "AlRay_AlAam")
					.replaceAll("<dcterms:created rdf:datatype=\"http://www.w3.org/2001/XMLSchema#dateTime\"></dcterms:created>", "<dcterms:created rdf:datatype=\"http://www.w3.org/2001/XMLSchema#dateTime\">0001-01-01T00:00:00Z</dcterms:created>")
					.replaceAll("<dcterms:modified rdf:datatype=\"http://www.w3.org/2001/XMLSchema#dateTime\"></dcterms:modified>", "<dcterms:modified rdf:datatype=\"http://www.w3.org/2001/XMLSchema#dateTime\">0001-01-01T00:00:00Z</dcterms:modified>")
					.replaceAll("http:/php.net", "http://php.net")
					.replaceAll("http:/www.apple.com/jp/iphone", "http://www.apple.com/jp/iphone")
					.replaceAll("http:/www.apple.com/safari", "http://www.apple.com/safari")
					.replaceAll("http:/www.senate.gov", "http://www.senate.gov");
	
				// Encodage des caractères illégaux dans les IRIs
				String encodedLine = encodeInvalidCharactersInIRI(modifiedLine);
	
				// Unicode Normalization Form C
				String normalizedLine = Normalizer.normalize(encodedLine, Normalizer.Form.NFC);
				writer.write(normalizedLine + "\n");
			}
	
			// Fermeture des flux
			reader.close();
			writer.close();
	
			// Suppression du fichier d'origine
			Files.deleteIfExists(Paths.get(filePath));
	
			// Renommer le fichier temporaire en fichier d'origine
			Files.move(Paths.get(filePath + ".tmp"), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
	
			System.out.println("Le fichier a été modifié avec succès.");
		} catch (IOException e) {
			System.out.println("Une erreur s'est produite lors de la modification du fichier : " + e.getMessage());
		}
	}
	
	private static String encodeInvalidCharactersInIRI(String iri) {
		StringBuilder encodedIRI = new StringBuilder();
		for (char c : iri.toCharArray()) {
			// Vérifier si le caractère est illégal pour un IRI et doit être encodé
			if (isIllegalIRICharacter(c)) {
				try {
					// Encoder le caractère illégal
					encodedIRI.append(URLEncoder.encode(String.valueOf(c), StandardCharsets.UTF_8.toString()));
				} catch (Exception e) {
					// En cas d'erreur d'encodage
					encodedIRI.append(c); // Garder le caractère original si l'encodage échoue
				}
			} else {
				encodedIRI.append(c); // Caractère valide, on le garde tel quel
			}
		}
		return encodedIRI.toString();
	}
	
	private static boolean isIllegalIRICharacter(char c) {
		// Définir les caractères illégaux à encoder
		return (c >= 0xD800 && c <= 0xDFFF) // Paires de substitution UTF-16
				//|| c == ' ' // Les espaces sont également illégaux dans un IRI (mais là nous lisons la ligne entière)
				|| !Character.isValidCodePoint(c); // Tout autre caractère non valide
	}


	public static void treatmentSVG(String mainDirectoryForDatasets, String idPair, ArrayList<String> listDatasetsFileName) throws Exception {
		String idPairForGraphURI = idPair.replaceFirst("[.][^.]+$", "");
		// Pour touts les datasets
		for (int i = 0; i < listDatasetsFileName.size(); i++) {

			String fileName = listDatasetsFileName.get(i);
			// Un nom pour la création du graphe TDB à partir du nom de l'ontologie
			String nameForGraphURI = fileName.replaceFirst("[.][^.]+$", "");

			Path pathFileDataset = Paths.get(mainDirectoryForDatasets, idPair, fileName);
			
			System.out.println("The dataset " + pathFileDataset.toString() + " is being loaded");  
			// correction des erreurs du fichier
			modifyFile(pathFileDataset.toString()); 
			
			String typeOfSerialization = null;
			// Si le fichier a l'extention .json 
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
					model.clearNsPrefixMap();
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
			} else // Le fichier n'a pas l'extention .json 
			{   // Si le fichier a l'extention .json 
				if(fileName.matches("^.*ttl$")) {
					typeOfSerialization = "TTL";
					Dataset dataset = TDBUtil.CreateTDBDataset();
					try {  
						// Effacement des statements contenus dans le graphe TDB
						dataset.begin(ReadWrite.WRITE);
						dataset.removeNamedModel(idPairForGraphURI + nameForGraphURI); 
						Model model = dataset.getNamedModel(idPairForGraphURI + nameForGraphURI);
						model.enterCriticalSection(Lock.WRITE);
						model.clearNsPrefixMap();
						// Read File and put it in model
						InputStream is = new FileInputStream(pathFileDataset.toString());
						// read the files in model
						model.read(is,"",typeOfSerialization);
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
				else // Le fichier n'a pas l'extention .ttl --> RDF/XML
				{
					Dataset dataset = TDBUtil.CreateTDBDataset();
					try {  
						// Effacement des statements contenus dans le graphe TDB
						dataset.begin(ReadWrite.WRITE);
						// Iterator<String> listgraph = dataset.listNames();
						// while(listgraph.hasNext()) {
						// 	System.out.println("Graph: " + listgraph.next());
						// };
						dataset.removeNamedModel(idPairForGraphURI + nameForGraphURI); 
						Model model = dataset.getNamedModel(idPairForGraphURI + nameForGraphURI);
						model.enterCriticalSection(Lock.WRITE);
						model.clearNsPrefixMap();
						// Vérifier les préfixes avant lecture
						// System.out.println("Before: ");
						// model.getNsPrefixMap().forEach((prefix, uri) -> {
						// 	System.out.println("Prefix: " + prefix + ", URI: " + uri);
						// });
						// Read File and put it in model
						// System.out.println("File: " + pathFileDataset.toString());
						InputStream is = new FileInputStream(pathFileDataset.toString());
						// read the files in model
						model.read(is,"");
						// // Vérifier les préfixes après lecture
						// System.out.println("After: ");
						// model.getNsPrefixMap().forEach((prefix, uri) -> {
						// 	System.out.println("Prefix: " + prefix + ", URI: " + uri);
						// });
						System.out.println("Graph size " + idPairForGraphURI + nameForGraphURI + " : " + model.size());
						// Path pathOfTheListPairDatasets = Paths.get(ProfilingConf.mainFolderProfiling, "output"+ idPairForGraphURI + nameForGraphURI +".rdf");		
						// String outputFileName = pathOfTheListPairDatasets.toString();
						// try (OutputStream out = new FileOutputStream(outputFileName)) {
						// 	model.write(out, "RDF/XML-ABBREV");
						// 	System.out.println("Modèle écrit dans le fichier " + outputFileName);
						// } catch (Exception e) {
						// 	e.printStackTrace();
						// }
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

}




