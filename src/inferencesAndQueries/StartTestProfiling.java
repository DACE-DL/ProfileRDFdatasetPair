package inferencesAndQueries;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import profiling.util.ProfilingConf;
import profiling.util.ProfilingQueryObject;

public class StartTestProfiling {

	public static void main(String[] args) throws Exception {

		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf();
		//String UserLanguage = ProfilingConf.preferredLanguage;
		String prefix = ProfilingConf.queryPrefix;
		String titleQuery = "";
		String typeQuery = "";
		String stringQuery = "";
		ArrayList<String> listRules = new ArrayList<String>();
		ArrayList<String> listDatasets = new ArrayList<String>();
		String topSpatial = "";
		ArrayList<ProfilingQueryObject> listQuery = new ArrayList<ProfilingQueryObject>();


		/////////////////////////////////////////////////////
		// Etude des parcelles                             //
		/////////////////////////////////////////////////////

		/////////////////////////////////////////////////////
		// Injection des données nécessaires pour l'étude  //
		/////////////////////////////////////////////////////
		listQuery.clear();
		titleQuery = "";
		typeQuery = "INSERT";

		// Insertion dans le modèle du nameSpace de l'ontologie (pour les rules)
		stringQuery = prefix + "INSERT DATA {dsp:thisOntology dsp:hasNameSpace dsp:.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		
		/////////////////////////////
		// Affichage des résultats //
		/////////////////////////////

		titleQuery = "PROFILE OF A DATASET";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of triples";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Triples) WHERE {dsp:thisOntology dsp:numberOfTriples ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Number of class";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_class) WHERE {dsp:thisOntology dsp:numberOfClass ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of class";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?element AS ?Class) WHERE { dsp:listURIofClass rdf:rest*/rdf:first ?element }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Class usage count";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?uri AS ?Class) (?val AS ?usage) WHERE { dsp:listClassUsageCount rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asValue ?val ." +
							" }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of class defined";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_defined_class) WHERE {dsp:thisOntology dsp:numberOfClassDefined ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of class Defined";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?element AS ?Class_defined) WHERE { dsp:listURIofClassDefined rdf:rest*/rdf:first ?element }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Number of class and subclass";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_class_and_subclass) WHERE {dsp:thisOntology dsp:numberOfClassAndSubclass ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of class and subclass";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?URI1 AS ?Class) (?URI2 AS ?Subclass) WHERE { dsp:listURIsOfClassAndSubclass rdf:rest*/rdf:first ?element ." +
		" ?element dsp:asClass ?URI1 ." +
		" ?element dsp:asSubclass ?URI2 ." +
		" }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Class hierarchy deep";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Max_depp) WHERE {dsp:thisOntology dsp:classHierarchyDeep ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Class hierarchy infinite loop";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?loop AS ?Infinite_loop) WHERE {dsp:thisOntology dsp:classHierarchyInfiniteLoop ?loop.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "PROPERTIES";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		
		titleQuery = "Number of property";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_property) WHERE {dsp:thisOntology dsp:numberOfPropertyUsage ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Property usage count";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?uri AS ?Property) (?val AS ?usage) WHERE { dsp:listPropertyUsage rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asValue ?val ." +
							" }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of property usage per subject";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_property) WHERE {dsp:thisOntology dsp:numberOfPropertyUsagePerSubj ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Property usage per subject";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?uri AS ?Property) (?val AS ?usage) WHERE { dsp:listPropertyUsagePerSubj rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asValue ?val ." +
							" }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Number of property usage per object";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_property) WHERE {dsp:thisOntology dsp:numberOfPropertyUsagePerObj ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Property usage per object";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?uri AS ?Property) (?val AS ?usage) WHERE { dsp:listPropertyUsagePerObj rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asValue ?val ." +
							" }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));	

		titleQuery = "Number of property per subject";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_property) WHERE {dsp:thisOntology dsp:numberOfPropertyPerSubj ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Property per subject";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?uri AS ?Property) (?val AS ?usage) WHERE { dsp:listPropertyPerSubj rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asValue ?val ." +
							" }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of property per object";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_property) WHERE {dsp:thisOntology dsp:numberOfPropertyPerObj ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Property per object";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?uri AS ?Property) (?val AS ?usage) WHERE { dsp:listPropertyPerObj rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asValue ?val ." +
							" }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Out degree";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Outdegree) WHERE {dsp:thisOntology dsp:outdegree ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "In degree";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Indegree) WHERE {dsp:thisOntology dsp:indegree ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of property and subproperty";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?URI1 AS ?Property) (?URI2 AS ?Subproperty) WHERE { dsp:listURIsOfPropertyAndSubproperty rdf:rest*/rdf:first ?element ." +
		" ?element dsp:asProperty ?URI1 ." +
		" ?element dsp:asSubproperty ?URI2 ." +
		" }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Property hierarchy deep";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Max_depp) WHERE {dsp:thisOntology dsp:propertyHierarchyDeep ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Property hierarchy infinite loop";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?loop AS ?Infinite_loop) WHERE {dsp:thisOntology dsp:propertyHierarchyInfiniteLoop ?loop.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Subclass usage";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:subclassUsage ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Entities mentioned";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:entitiesMentioned ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Distinct entities";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:distinctEntities ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of literals";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:numberOfLiterals ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of blanks as subject";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:numberBlanksAsSubj ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of blanks as object";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:numberBlanksAsObj ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of datatypes";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_datatypes) WHERE {dsp:thisOntology dsp:numberOfDatatypes ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Datatypes";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?element AS ?Datatype) WHERE { dsp:listOfDatatypes rdf:rest*/rdf:first ?element }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		
		// Sauvegarde des queries dans un fichier json
		//ObjectMapper objectMapper = new ObjectMapper();
	    //objectMapper.writeValue(new File("C:\\var\\www\\profiling\\queries\\testQuery.json"), listQuery);
		
		Instant start0 = Instant.now();
		
		listRules.add("profiling.rules");
		listDatasets.add("source.json");
		topSpatial = "false";
		
		CreateInferredModelAndRunQueries.InferencesAndQuery(listDatasets, listRules, topSpatial, listQuery);
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}