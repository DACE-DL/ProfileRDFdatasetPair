package inferencesAndQueries;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

import openllet.jena.PelletReasonerFactory;
import profiling.util.*;


public class CreateInferedModel { 

	public static InfModel createInferedModel(String idPair, ArrayList<String> listDatasets, ArrayList<String> listRules, String topSpatial) throws Exception {
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...	
		new ProfilingConf();

		Instant start0 = Instant.now();
		String idPairForGraphURI = idPair.replaceFirst("[.][^.]+$", "");

		// On charge les ontologies de la liste listDatasets recues en paramétre
		Map<String, Model> ontologiesModels = new HashMap<String, Model>();
		for(String nameOntology: listDatasets){
			// Un nom pour le graphe TDB à partir du nom de l'ontologie et de l'identifiant de la paire
			String nameForGraphURI = nameOntology.replaceFirst("[.][^.]+$", "");
			Dataset dataset = TDBUtil.CreateTDBDataset();
			dataset.begin(ReadWrite.READ);
			ontologiesModels.put(idPairForGraphURI + nameForGraphURI, ModelFactory.createDefaultModel().add(dataset.getNamedModel(idPairForGraphURI + nameForGraphURI)));
			dataset.commit();
			dataset.close();
			dataset.end();
		}

		Model modelTemp = ModelFactory.createDefaultModel();
		Model modelImports = ModelFactory.createDefaultModel();
		Model modelInfered = ModelFactory.createDefaultModel();

		if (topSpatial.equals("true")) {
			// On charge l'ontologie GeoSparql
			modelImports.read("http://www.opengis.net/ont/geosparql#");
			// Petit souci avec un statement de GeoSparql ==> on le supprime du modèle!
			Resource resource = modelImports.getResource("http://www.opengis.net/ont/geosparql");
			Property property = modelImports.getProperty("http://purl.org/dc/elements/1.1/source");
			RDFNode rdfNode = modelImports.getResource("http://www.opengis.net/doc/IS/geosparql/1.0");
			StmtIterator statements = modelImports.listStatements(resource,property,rdfNode);
			modelImports.remove(statements);
			GeoSPARQLConfig.setupMemoryIndex();
		}
		
		// Mise en place des ontologies dans le modèle modelTemp
		Set<String> ontologiesNames = ontologiesModels.keySet();
		for (String ontologyName : ontologiesNames) {
			modelTemp.add(ontologiesModels.get(ontologyName)); 
		}
		// On récupére les Imports (GeoSparql)
		modelTemp.add(modelImports);
		
		// Une fois chargés dans le modèle modelTemp les modéles intermédiaires sont fermés
		for (String ontologyName : ontologiesNames) {
			ontologiesModels.get(ontologyName).close(); 
		}
		modelImports.close();

		Instant end0 = Instant.now();
		System.out.println("Runtime for loading models into memory: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		Instant start1 = Instant.now();

		System.out.println("Model size before addtional treatement: " + modelTemp.size() + " triples");
        ProfilingPreProcessing.makeTreatements(modelTemp);
		System.out.println("Model size after addtional treatement: " + modelTemp.size() + " triples");	
		
		Instant end1 = Instant.now();
		System.out.println("Runtime for additional treatments: " + ProfilingUtil.getDurationAsString(Duration.between(start1, end1).toMillis()));
		Instant start2 = Instant.now();
		

		// create Pellet reasoner
		final Reasoner reasoner = PelletReasonerFactory.theInstance().create();

		modelInfered = ProfilingPelletInferences.MakeInferencesWithPellet(reasoner, modelTemp);

		modelTemp.close();
		
		// Register custom primitive
		
		//BuiltinRegistry.theRegistry.register(new CalcTest());
		BuiltinRegistry.theRegistry.register(new CalcNumberOfTriples());
		//BuiltinRegistry.theRegistry.register(new CalcPropertyUsageCount());
		// BuiltinRegistry.theRegistry.register(new CalcPropertyUsageDistinctPerSubject());
		// BuiltinRegistry.theRegistry.register(new CalcPropertyUsageDistinctPerObject());
		// BuiltinRegistry.theRegistry.register(new CalcMakeVectorWithListUriAndNumber());
		//BuiltinRegistry.theRegistry.register(new CalcRQuantile());
		//BuiltinRegistry.theRegistry.register(new CalcPropertyMostUsed());
		// BuiltinRegistry.theRegistry.register(new CalcOutDegree());
		// BuiltinRegistry.theRegistry.register(new CalcInDegree());
		// BuiltinRegistry.theRegistry.register(new CalcPropertyAndSubproperty());
		// BuiltinRegistry.theRegistry.register(new CalcPropertyHierarchyDeep());	
		// BuiltinRegistry.theRegistry.register(new CalcSubclassUsage());
		// BuiltinRegistry.theRegistry.register(new CalcEntitiesMentioned());
		// BuiltinRegistry.theRegistry.register(new CalcDistinctEntities());
		// BuiltinRegistry.theRegistry.register(new CalcNumberOfLiterals());
		// BuiltinRegistry.theRegistry.register(new CalcNumberBlanksAsSubj());
		// BuiltinRegistry.theRegistry.register(new CalcNumberBlanksAsObj());
		// BuiltinRegistry.theRegistry.register(new CalcMakeListDatatypes());
		// BuiltinRegistry.theRegistry.register(new CalcMakeListLanguages());
		// BuiltinRegistry.theRegistry.register(new CalcTypedStringLength());
		// BuiltinRegistry.theRegistry.register(new CalcUntypedStringLength());
		// BuiltinRegistry.theRegistry.register(new CalcTypedSubjects());
		// BuiltinRegistry.theRegistry.register(new CalcLabeledSubjects());
		// BuiltinRegistry.theRegistry.register(new CalcSameAs());
		// BuiltinRegistry.theRegistry.register(new CalcLinks());
		// BuiltinRegistry.theRegistry.register(new CalcMakeListMaxPerProperty());
		// BuiltinRegistry.theRegistry.register(new CalcMakeListPerProperty());
		// BuiltinRegistry.theRegistry.register(new CalcMakeListSPOvocabularies());
        // // For the Classes
		// BuiltinRegistry.theRegistry.register(new CalcClassUsageCount());
		// BuiltinRegistry.theRegistry.register(new CalcClassDefined());
		// BuiltinRegistry.theRegistry.register(new CalcClassNotDefined());
		// BuiltinRegistry.theRegistry.register(new CalcClassAndSubClass());
		// BuiltinRegistry.theRegistry.register(new CalcClassHierarchyDeep());
		// BuiltinRegistry.theRegistry.register(new CalcClassMostUsed());
		// BuiltinRegistry.theRegistry.register(new CalcPropertyWithDomaineAndRange());
		
		
		
		
		
		
		
		//BuiltinRegistry.theRegistry.register(new CalcListClassOfInterest());
		//BuiltinRegistry.theRegistry.register(new CalcMakeListClassOfInterest());
		//BuiltinRegistry.theRegistry.register(new CalcMakeListPropertyOfInterest());
		
		
		

		//String test = "C:/Users/conde/Documents/GitHub/ProfileRDFdatasetPair/src/profiling/util/CalcPropertyUsage.java";
		//Builtin impl = BuiltinRegistry.theRegistry.getImplementationByURI(test);
		//BuiltinRegistry.theRegistry.register(impl);

		// !!!! pour l'instant une seule liste de règles
		//System.out.println(listRules.get(0).toString());
		Path pathFileRules = Paths.get(ProfilingConf.folderForRules , listRules.get(0).toString());
		System.out.println(pathFileRules.toString());
		Reasoner reasonerRules = new GenericRuleReasoner(Rule.rulesFromURL(pathFileRules.toString()));

		InfModel infModel = ModelFactory.createInfModel(reasonerRules, modelInfered);  
		// infModel.rebind();
		// infModel.prepare();

		//System.out.println("VBOX_MSI_INSTALL_PATH = "  + System.getenv("VBOX_MSI_INSTALL_PATH"));
		//System.out.println("SIS_DATA = "  + System.getenv("SIS_DATA"));

		Instant end2 = Instant.now();
		System.out.println("Runtime for inferred model delivery: " + ProfilingUtil.getDurationAsString(Duration.between(start2, end2).toMillis()));

		return infModel;
	}


}
