package inferencesAndQueries;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

import aobra.util.*;
import openllet.jena.PelletReasonerFactory;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;

public class AobraCreateInferedModel { 

	public static InfModel createInferedModel(ArrayList<String> listOntologies, ArrayList<String> listRules, String topSpatial) throws Exception {
		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...	
		new AobraConf();

		Instant start0 = Instant.now();

		// On charge les ontologies de la liste listOntologies recues en paramétre
		Map<String, Model> ontologiesModels = new HashMap<String, Model>();
		for(String nameOntology: listOntologies){
			// Un nom pour le graphe TDB à partir du nom de l'ontologie
			String nameForGraphURI = nameOntology.replaceFirst("[.][^.]+$", "");
			Dataset dataset = TDBUtil.CreateTDBDataset();
			dataset.begin(ReadWrite.READ);
			ontologiesModels.put(nameForGraphURI, ModelFactory.createDefaultModel().add(dataset.getNamedModel(nameForGraphURI)));
			dataset.commit();
			dataset.close();
			dataset.end();
		}

		Model modelTemp = ModelFactory.createDefaultModel();
		Model modelImports = ModelFactory.createDefaultModel();
		Model modelInfered = ModelFactory.createDefaultModel();

		if (topSpatial.equalsIgnoreCase("true")) {
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
		System.out.println("Runtime for loading models into memory: " + Duration.between(start0, end0).toMillis() + " millisecondes");

		Instant start1 = Instant.now();

		// create Pellet reasoner
		final Reasoner reasoner = PelletReasonerFactory.theInstance().create();

		modelInfered = AobraPelletInferences.MakeInferencesWithPellet(reasoner, modelTemp);

		modelTemp.close();
		
		// Register custom primitive
		BuiltinRegistry.theRegistry.register(new CalcTreeBiomass());
		BuiltinRegistry.theRegistry.register(new CalcRSumVector());
		BuiltinRegistry.theRegistry.register(new CalcRMeanVector());
		BuiltinRegistry.theRegistry.register(new CalcRMedianVector());
		BuiltinRegistry.theRegistry.register(new CalcRSdVector());
		BuiltinRegistry.theRegistry.register(new CalcRVarVector());
		BuiltinRegistry.theRegistry.register(new CalcRatio());
		BuiltinRegistry.theRegistry.register(new CalcPercent());
		BuiltinRegistry.theRegistry.register(new CalcRQuantile());
		BuiltinRegistry.theRegistry.register(new CalcRShapiro());
		BuiltinRegistry.theRegistry.register(new CalcRKolmogorov());
		BuiltinRegistry.theRegistry.register(new CalcMakeVector());
		BuiltinRegistry.theRegistry.register(new CalcTotalTreesBiomass());
		BuiltinRegistry.theRegistry.register(new CalcRWilcoxon());
		BuiltinRegistry.theRegistry.register(new CalcRAreaWKTPolygone());
		BuiltinRegistry.theRegistry.register(new CalcTreeBasalArea());
		BuiltinRegistry.theRegistry.register(new CalcTotalTreesBasalArea());
		BuiltinRegistry.theRegistry.register(new CalcTestSPARQL());
		BuiltinRegistry.theRegistry.register(new CalcDistanceBetween2Elements());
		BuiltinRegistry.theRegistry.register(new CalcMakeListConceptsWithTerm());
		BuiltinRegistry.theRegistry.register(new CalcNearestNeighbor());
		BuiltinRegistry.theRegistry.register(new CalcNeighborOfNeighbors());
		BuiltinRegistry.theRegistry.register(new CalcIsInLine());
		BuiltinRegistry.theRegistry.register(new CalcNumberTreesInPlot());
		BuiltinRegistry.theRegistry.register(new CalcOkTreatmentNeighbors());
		BuiltinRegistry.theRegistry.register(new CalcGeomLine());
		BuiltinRegistry.theRegistry.register(new CalcDistanceBetweenLines());
		BuiltinRegistry.theRegistry.register(new CalcDistanceBetweenTrees());
		BuiltinRegistry.theRegistry.register(new CalcAzimutLine());
		BuiltinRegistry.theRegistry.register(new CalcPlotLinesDirection());
		BuiltinRegistry.theRegistry.register(new CalcDistanceBetweenTreesOfLine());
		BuiltinRegistry.theRegistry.register(new CalcTreeDensity());
		BuiltinRegistry.theRegistry.register(new CalcCultivatedLaneOpennessIndex());
		BuiltinRegistry.theRegistry.register(new CalcTreeDensityIndex());
		BuiltinRegistry.theRegistry.register(new CalcCumulativeCrownLengthIndex());
		BuiltinRegistry.theRegistry.register(new CalcRatioDiff());
		BuiltinRegistry.theRegistry.register(new CalcScore());
		BuiltinRegistry.theRegistry.register(new CalcMakeListNarrowerConceptsWithTerm());
		BuiltinRegistry.theRegistry.register(new CalcGiveLabelOfRessource());

		// !!!! pour l'instant une seule liste de règles
		System.out.println(listRules.get(0).toString());
		Path pathFileRules = Paths.get(AobraConf.folderForRules , listRules.get(0).toString());
		System.out.println(pathFileRules.toString());
		Reasoner reasonerRules = new GenericRuleReasoner(Rule.rulesFromURL(pathFileRules.toString()));

		InfModel infModel = ModelFactory.createInfModel(reasonerRules, modelInfered);  
		// infModel.rebind();
		// infModel.prepare();

		//System.out.println("VBOX_MSI_INSTALL_PATH = "  + System.getenv("VBOX_MSI_INSTALL_PATH"));
		//System.out.println("SIS_DATA = "  + System.getenv("SIS_DATA"));

		Instant end1 = Instant.now();
		System.out.println("Runtime for inferred model delivery: " + Duration.between(start1, end1).toMillis() + " millisecondes");


		return infModel;
	}


}
