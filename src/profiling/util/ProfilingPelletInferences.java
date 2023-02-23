package profiling.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.ValidityReport.Report;

import openllet.jena.PelletReasonerFactory;

public class ProfilingPelletInferences {
	/**
	 * @param Json des données en RDF/JSON
	 * @param json du schema en RDF/JSON
	 * @return json des inférences en RDF/JSON 
	 * 
	 * Calcul les inférences avec les données et le modèle recus en format JSON 
	 *  et place le résultat dans un fichier json.
	 */
	public static String MakeInferencesWithPellet(String inData, String inSchema) {
		
		// Initialisation de la configuration
		// Chemin d'accés, noms fichiers...
		new ProfilingConf(); 
		
		String jsonStringOut = null;
		Model schema = ModelFactory.createDefaultModel();
		Model data = ModelFactory.createDefaultModel();
		OntModel myOntology = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		
		try {
			
			StringReader inD = new StringReader(inData);
			data.read(inD, null, "RDF/JSON");
			
			StringReader inS = new StringReader(inSchema);
			schema.read(inS, null, "RDF/JSON");
			
			myOntology.add(schema);
			myOntology.add(data);
			
			Instant start = Instant.now();
			
			// create an empty ontology model using Pellet spec
			final OntModel infModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);

			// read the file
			infModel.add(myOntology);
			
			ValidityReport validity = infModel.validate();
			if (validity.isValid()) {
				System.out.println("Validation du modèle inféré OK");
				
				Instant end = Instant.now();
				System.out.println("Durée d'exécution pour les inférences: " + Duration.between(start, end).getSeconds() + " secondes");
		         
				// exporte le resultat dans un fichier au format RDF/JSON
		    	StringWriter out = new StringWriter();
		    	infModel.write(out, "RDF/JSON");
		    	jsonStringOut = out.toString();		
				
			} else {
				System.out.println("Validation du modèle inféré KO ! ");
				System.out.println("Liste des conflits : ");
				for (Iterator<Report> i = validity.getReports(); i.hasNext(); ) {
					ValidityReport.Report report = (ValidityReport.Report)i.next();
					System.out.println(" - " + report);
				}
			}	
			
			schema.close();
			data.close();
			infModel.close();
		} 
		catch (Exception e) {System.out.println("big problem :" + e);}	
		
		return jsonStringOut;
	}
	
	/**
	 * @param données Abox
	 * @param schema Tbox
	 * @return inférences 
	 * 
	 * Calcul les inférences avec les données et le modèle recus en entrée 
	 *  et place le résultat dans un modèle en sortie.
	 */
	public static OntModel MakeInferencesWithPellet(OntModel modelData, OntModel modelSchema) {

		// Initialisation de la configuration
		// Chemin d'accés, noms fichiers...
		new ProfilingConf(); 

		OntModel myOntology = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		OntModel myOntologyInfered = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		try {

			myOntology.add(modelSchema);
			myOntology.union(modelData);

			Instant start = Instant.now();

			// create an empty ontology model using Pellet spec
			final OntModel infModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);

			// read the file to infer
			infModel.add(myOntology);

			// save model for output
			myOntologyInfered.add(infModel);

			ValidityReport validity = infModel.validate();
			if (validity.isValid()) {
				System.out.println("Validation du modèle inféré OK");

				Instant end = Instant.now();
				System.out.println("Durée d'exécution pour les inférences: " + Duration.between(start, end).getSeconds() + " secondes");

			} else {
				System.out.println("Validation du modèle inféré KO ! ");
				System.out.println("Liste des conflits : ");
				for (Iterator<Report> i = validity.getReports(); i.hasNext(); ) {
					ValidityReport.Report report = (ValidityReport.Report)i.next();
					System.out.println(" - " + report);
				}
			}	

			infModel.close();
		} 
		catch (Exception e) {System.out.println("big problem :" + e);}	

		return myOntologyInfered;
	}

	/**
	 * @param model à inférer
	 * @return model inféré 
	 * 
	 * Calcul les inférences avec le modèle recus
	 *  et place le résultat dans un model en sortie.
	 */
	public static OntModel MakeInferencesWithPellet(OntModel model) {

		final OntModel inferedModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC,model);
		
		//Instant start = Instant.now();

		ValidityReport validity = inferedModel.validate();
		if (validity.isValid()) {
			//System.out.println("Validation du modèle inféré OK");
		} else {
			System.out.println("Validation du modèle inféré KO ! ");
			System.out.println("Liste des conflits : ");
			for (Iterator<Report> i = validity.getReports(); i.hasNext(); ) {
				ValidityReport.Report report = (ValidityReport.Report)i.next();
				System.out.println(" - " + report);
			}
		}	

		//Instant end = Instant.now();
		//System.out.println("Durée d'exécution pour les inférences: " + Duration.between(start, end).getSeconds() + " secondes");

		return inferedModel;
	}

	/**
	 * @param reasoner
	 * @param model à inférer
	 * @return model inféré 
	 * 
	 * Calcul les inférences avec le modèle recus
	 *  et place le résultat dans un model en sortie.
	 */
	public static InfModel MakeInferencesWithPellet(Reasoner reasoner, OntModel model) {

		final InfModel infModel = ModelFactory.createInfModel(reasoner,model);
		
		//Instant start = Instant.now();

		ValidityReport validity = infModel.validate();
		if (validity.isValid()) {
			//System.out.println("Validation du modèle inféré OK");
		} else {
			System.out.println("Validation du modèle inféré KO ! ");
			System.out.println("Liste des conflits : ");
			for (Iterator<Report> i = validity.getReports(); i.hasNext(); ) {
				ValidityReport.Report report = (ValidityReport.Report)i.next();
				System.out.println(" - " + report);
			}
		}	

		//Instant end = Instant.now();
		//System.out.println("Durée d'exécution pour les inférences: " + Duration.between(start, end).getSeconds() + " secondes");

		return infModel;
	}

	public static InfModel MakeInferencesWithPellet(Reasoner reasoner, Model model) {
		
		// create an empty model
		final Model emptyModel = ModelFactory.createDefaultModel();
		
		// create an inferencing model using Pellet reasoner
		final InfModel infModel = ModelFactory.createInfModel(reasoner, emptyModel);
		
		// read the file
		infModel.add(model);

		// print validation report
		//final ValidityReport report = infModel.validate();
	    //printIterator(report.getReports(), "Validation Results");
		
		return infModel;
	}
	
	
	/**
	 * @param InputStream des données en RDF/JSON
	 * @param InputStream du schema en RDF/JSON
	 * @return OutputStream des inférences en RDF/JSON 
	 * 
	 * Calcul les inférences avec les données et le modèle et place le résultat
	 * dans un fichier json.
	 */
	public static OutputStream MakeInferencesWithPellet(InputStream inData, InputStream inSchema) {
		
		// Initialisation de la configuration
		// Chemin d'accés, noms fichiers...
		new ProfilingConf(); 
		
		OutputStream jsonOut = new ByteArrayOutputStream(1024);
		Model schema = ModelFactory.createDefaultModel();
		Model data = ModelFactory.createDefaultModel();
		OntModel myOntology = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		
		try {	
			data.read(inData, null, "RDF/JSON");
			schema.read(inSchema, null, "RDF/JSON");
			
			myOntology.add(schema);
			myOntology.add(data);
			
			Instant start = Instant.now();
			
			// create an empty ontology model using Pellet spec
			final OntModel infModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);

			// read the file
			infModel.add(myOntology);
			
			ValidityReport validity = infModel.validate();
			if (validity.isValid()) {
				System.out.println("Validation du modèle inféré OK");
				
				Instant end = Instant.now();
				System.out.println("Durée d'exécution pour les inférences: " + Duration.between(start, end).getSeconds() + " secondes");
		         
				// exporte le resultat dans un fichier OutputStream
				infModel.write(jsonOut, "RDF/JSON");			
				
			} else {
				System.out.println("Validation du modèle inféré KO ! ");
				System.out.println("Liste des conflits : ");
				for (Iterator<Report> i = validity.getReports(); i.hasNext(); ) {
					ValidityReport.Report report = (ValidityReport.Report)i.next();
					System.out.println(" - " + report);
				}
			}	
			
			schema.close();
			data.close();
			infModel.close();
		} 
		catch (Exception e) {System.out.println("big problem :" + e);}	
		
		return jsonOut;
	}

	/**
	 * @param Nom du graphe des données
	 * @param Nom du graphe du schema
	 * @param Nom du graphe des inférences
	 * @return void
	 * Calcul les inférences avec les données et le modèle et place le résultat
	 * dans le graphe des inférences
	 */
	public static void MakeInferencesWithPellet(String nameOfDataGraph, String nameOfSchemaGraph, String nameOfInferedGraph) {
		
		// Initialisation de la configuration
		// Chemin d'accés, noms fichiers...
		new ProfilingConf(); 
		
		Dataset dataset = null;
		Model schema = ModelFactory.createDefaultModel();
		Model data = ModelFactory.createDefaultModel();
		Model inferences = ModelFactory.createDefaultModel();

		OntModel myOntology = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		
		
		try {
			dataset = TDBUtil.CreateTDBDataset();
			dataset.begin(ReadWrite.WRITE);
			data = dataset.getNamedModel(nameOfDataGraph);
			schema = dataset.getNamedModel(nameOfSchemaGraph);
			inferences = dataset.getNamedModel(nameOfInferedGraph);
			
			myOntology.add(schema);
			myOntology.add(data);
			
			//Instant start = Instant.now();
			
			// create an empty ontology model using Pellet spec
			final OntModel infModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
			
			infModel.add(myOntology);
			// Sauvegarde du modèle inféré dans graphURIInferences de TDB
			inferences.add(infModel);
			
			dataset.commit();
			dataset.close();
			dataset.end();
			schema.close();
			data.close();
			inferences.close();
			infModel.close();
			
		} 
		catch (Exception e) {System.out.println("big problem :" + e);}	
		
	}

	
	
	
	
	public static void printIterator(final Iterator<?> i, final String header)
	{
		System.out.println(header);
		for (int c = 0; c < header.length(); c++)
			System.out.print("=");
		System.out.println();

		if (i.hasNext())
			while (i.hasNext())
				System.out.println(i.next());
		else
			System.out.println("<EMPTY>");

		System.out.println();
	}

	
}