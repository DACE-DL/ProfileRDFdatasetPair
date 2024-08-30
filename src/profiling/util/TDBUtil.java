package profiling.util;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.dboe.base.file.Location;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.tdb2.TDB2Factory;


public class TDBUtil {
	
	public static Dataset CreateTDBDataset() throws Exception {

		// Create dataset
		Dataset dataset = null;
		try {   
		Path pathDataBase = Paths.get(ProfilingConf.folderForTDB , ProfilingConf.fileNameTDBdatabase);
		Location location = Location.create(pathDataBase.toString());
		dataset = TDB2Factory.connectDataset(location);
		}
		catch (Exception e) {System.out.println("CreateTDBDataset erreur: " + e);}
		finally { 
			dataset.end() ; 
		}
		return dataset;
    }
	
	public static void DeleteStatementToTDBGraph(List<Statement> listStmt, String graphURI) throws Exception {
		
		Dataset dataset = CreateTDBDataset();
		dataset.begin(ReadWrite.WRITE);
		try {   
			
			Model model = dataset.getNamedModel(graphURI);
			model.remove(listStmt);
			dataset.commit();    
			model.close();
		}
		finally { dataset.end() ; }
	}
	
	
	public static void InputInputStreamContentToTDB(String serializationType, InputStream inputStream) throws Exception {
		try {   
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.WRITE);
			Model model = dataset.getDefaultModel();
			// Read inputStream and put it in temporary model
			Model modelTemp = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);    
			modelTemp.read( inputStream, serializationType );
			// Add temporary model to model
			model.add(modelTemp);
			dataset.commit();    
			dataset.close();
			modelTemp.close();
		}
        catch (IOException e) {System.out.println("IO problem");}
	}
	
	public static void InputInputStreamContentToTDB(String base, InputStream inputStream, String graphURI) throws Exception {
		try {   
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.WRITE);
			Model model = dataset.getNamedModel(graphURI);
			// Read JSON File and put it in temporary model
			Model modelTemp = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);    
			// read the RDF/JSON files in temporary model
			modelTemp.read(inputStream, base );
			// Add temporary model to model
			model.add(modelTemp);
			dataset.commit();    
			dataset.close();
			modelTemp.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException e) {System.out.println("IO problem");}
	}
	
	
	
	public static void InputJsonStringToTDB(String serializationType, String inJsonString) throws Exception {
		try {   
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.WRITE);
			Model model = dataset.getDefaultModel();
			// Read JSON File and put it in temporary model
			Model modelTemp = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);    
			StringReader in = new StringReader(inJsonString);
			// read the RDF/JSON in temporary model
			modelTemp.read(in, null, serializationType);
			// Add temporary model to model
			model.add(modelTemp);
			dataset.commit();    
			dataset.close();
			modelTemp.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException e) {System.out.println("IO problem");}
	}
	
	
	public static void InputJsonStringToTDB(String serializationType, String inJsonString, String graphURI) throws Exception {
		try {   
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.WRITE);
			Model model = dataset.getNamedModel(graphURI);
			// Read JSON File and put it in temporary model
			Model modelTemp = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);    
			StringReader in = new StringReader(inJsonString);
			// read the RDF/JSON in temporary model
			modelTemp.read(in, null, serializationType);
			// Add temporary model to model
			model.add(modelTemp);
			dataset.commit();    
			dataset.close();
			modelTemp.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException e) {System.out.println("IO problem");}
	}
	
	public static void InputFileContentToTDB(String serializationType, String inFile) throws Exception {
		try {   
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.WRITE);
			Model model = dataset.getDefaultModel();
			// Read JSON File and put it in temporary model
			Model modelTemp = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);    
			// read the RDF/JSON files in temporary model
			modelTemp.read( inFile, serializationType );
			// Add temporary model to model
			model.add(modelTemp);
			dataset.commit();    
			dataset.close();
			modelTemp.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException e) {System.out.println("IO problem");}
	}
	
	
	public static void InputFileContentToTDB(String serializationType, String inFile, String graphURI) throws Exception {
		try {   
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.WRITE);
			Model model = dataset.getNamedModel(graphURI);
			// Read JSON File and put it in temporary model
			Model modelTemp = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);    
			// read the RDF/JSON files in temporary model
			modelTemp.read( inFile, serializationType );
			// Add temporary model to model
			model.add(modelTemp);
			dataset.commit();    
			dataset.close();
			modelTemp.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException e) {System.out.println("IO problem");}
	}
	
	public static void InputModelToTDB(Model modelIn, String graphURI) throws Exception {
		try {   
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.WRITE);
			Model model = dataset.getNamedModel(graphURI);
			// Add modelIn to model
			model.add(modelIn);
			dataset.commit();    
			dataset.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException e) {System.out.println("IO problem");}
	}
	
	public static void OutputOfTDBContentToConsole(String serializationType) throws Exception {

		Dataset dataset = CreateTDBDataset();
		// Create transaction for writing
		dataset.begin(ReadWrite.READ);
		Model model = dataset.getDefaultModel();
		// Sortie console N3 N-TRIPLE RDF/XML RDF/XML-ABBREV
		model.write(System.out, serializationType);
		dataset.commit();    
		dataset.close();
	}
	
	public static void OutputOfTDBContentToConsole(String serializationType, String graphURI) throws Exception {

		Dataset dataset = CreateTDBDataset();
		// Create transaction for writing
		dataset.begin(ReadWrite.READ);
		Model model = dataset.getNamedModel(graphURI);
		// Sortie console N3 N-TRIPLE RDF/XML RDF/XML-ABBREV
		model.write(System.out, serializationType);
		dataset.commit();    
		dataset.close();
	}
	
	public static void OutputOfTDBContentToFile(String serializationType, String outFile) throws Exception {

		try {   
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.READ);
			Model model = dataset.getDefaultModel();
			// Sortie fichier  
			FileOutputStream outStream = new FileOutputStream(outFile);
			// exporte le resultat dans un fichier
			model.write(outStream, serializationType);
			outStream.close();
			dataset.commit(); 
			dataset.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException e) {System.out.println("IO problem");}
	}
	
	public static void OutputOfTDBContentToFile(String serializationType, String outFile, String graphURI) throws Exception {
		
		Dataset dataset = CreateTDBDataset();
		dataset.begin(ReadWrite.READ);
		
		try { 
			
			Model model = dataset.getNamedModel(graphURI);
			// Sortie fichier 
			FileOutputStream outStream = new FileOutputStream(outFile);
			// exporte le resultat dans un fichier
			model.write(outStream, serializationType);
			dataset.commit(); 
			outStream.close();   
			model.close();
		}
		catch (Exception e) {System.out.println("problem with the conversion: " + e );}
		finally { 
			dataset.end() ; 
		}
	}
	
	
	public static long SizeOfTDBContent() throws Exception {
		long sizeOfTDB = 0;
		try {
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.READ);
			Model model = dataset.getDefaultModel();
			sizeOfTDB = model.size();
			dataset.commit();    
			dataset.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException e) {System.out.println("IO problem");}
		
		return sizeOfTDB;	
	}
	
	public static long SizeOfTDBContent(String graphURI) throws Exception {
		long sizeOfTDB = 0;
		try {
			Dataset dataset = CreateTDBDataset();
			dataset.begin(ReadWrite.READ);
			Model model = dataset.getNamedModel(graphURI);
			sizeOfTDB = model.size();
			dataset.commit();    
		    dataset.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException e) {System.out.println("IO problem");}
		
		return sizeOfTDB;
		
	}
	
	
    public static void PrintAllSizeOfTDBContent(ArrayList<String> listGraphURI) throws Exception {
		
		System.out.println("Taille des différents graphes de TDB : " );
		// Taille du graphe par d�faut de TDB 
		System.out.println("- Taille du graphe par défaut de TDB : " + SizeOfTDBContent());
		
		// Taille des autres graphes de TDB donn�s par la liste 
		for (int i = 0; i < listGraphURI.size(); i++) {
		System.out.println("- Taille du graphe " + listGraphURI.get(i) + " de TDB : " + TDBUtil.SizeOfTDBContent(listGraphURI.get(i)));
		}
	}
	
	public static void DeleteTDBContent() throws Exception {
		try { 
		Dataset dataset = CreateTDBDataset();
		// Create transaction for writing
		dataset.begin(ReadWrite.WRITE);
		Model model = dataset.getDefaultModel();
		model.removeAll();
		model.clearNsPrefixMap();
		dataset.commit();
		dataset.close();
		}
		catch (Exception e) {System.out.println("problem" + e );}
	}
	
	public static void DeleteTDBContent(String graphURI) throws Exception {
		Dataset dataset = null;
		try {
			dataset = CreateTDBDataset();
		    // Create transaction for writing
			if (dataset != null) {
				//System.out.println(dataset);
		    	dataset.begin(ReadWrite.WRITE);
				dataset.removeNamedModel(graphURI);
				dataset.commit();
				dataset.close();	
			} else { 
				System.out.println("Fichier inexistant sur TDB !");
			}
		}
		catch (FileNotFoundException e) {System.out.println("File not found : " + e);}
        catch (IOException e) {System.out.println("IO problem : " + e );}
		catch (Exception e) {System.out.println("Problem with Delete TDB Content : " + e );}
	}
	
	public static void DeleteTDBContent(ArrayList<String> listGraphURI) throws Exception {
		try { 
			Dataset dataset = CreateTDBDataset();
			// Create transaction for writing
			dataset.begin(ReadWrite.WRITE);
			Model model = dataset.getDefaultModel();
			model.removeAll();
			model.clearNsPrefixMap();
			
			for (int i = 0; i < listGraphURI.size(); i++) {
			    model = dataset.getNamedModel(listGraphURI.get(i));
				model.removeAll();
				model.clearNsPrefixMap();
			}
			dataset.commit();
			model.close(); 
			dataset.close();
		}
		catch (Exception e) {System.out.println("problem" + e );}
	}

	public static void DifferencesBetween2Graphs(String graphURI1, String graphURI2, String resultGraphURI) throws Exception {
		Dataset dataset = CreateTDBDataset();
		dataset.begin(ReadWrite.WRITE);	
		try {   
			Model model1 = dataset.getNamedModel(graphURI1);
			Model model2 = dataset.getNamedModel(graphURI2);
			Model resultModel = dataset.getNamedModel(resultGraphURI);
			  
			resultModel.add(model1.difference(model2));
			
			dataset.commit();
			model1.close();
			model2.close();
			resultModel.close();
		}
		finally { 
			dataset.end() ; 
		}
	}
	

}