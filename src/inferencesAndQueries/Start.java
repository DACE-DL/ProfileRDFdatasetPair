package inferencesAndQueries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

import org.apache.jena.query.Dataset;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import profiling.util.ProfilingConf;

public class Start {

	// Lancement initial 
	public static void main(String[] args) throws Exception {

		Instant start0 = Instant.now();
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf();
		 // Récupération du nom du répertoire des ontologies à traiter dans la configuration
		 String fileName = "bibi.xml";
		 Path pathIn = Paths.get(ProfilingConf.folderForDatasets, fileName);
	   
		// Un nom pour la création du fichier
		String nameForOutFile = fileName.replaceFirst("[.][^.]+$", "");
		System.out.println("The file " + fileName + " is being treaded");   
		System.out.println(pathIn.toString());
		// Create a dataset and read into it from file 
        Dataset dataset = RDFDataMgr.loadDataset(pathIn.toString()) ;
        // Write as JSON-LD
		Path pathOut = Paths.get(ProfilingConf.folderForDatasets, nameForOutFile + ".json");
		File outFile = new File(pathOut.toString());
		// FileOutputStream  
		OutputStream os = new FileOutputStream(outFile);
		RDFDataMgr.write(os, dataset, Lang.JSONLD) ;
		System.out.println("The file " + outFile + " is created");   
		dataset.close();

		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}