package inferencesAndQueries;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

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
		String fileNameTestResult = "";
		String topSpatial = "";
		String consoleOutput = "";
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

		titleQuery = "CLASS";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of class";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_class) WHERE {dsp:thisOntology dsp:numberOfClass ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of class";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?element AS ?Class) WHERE { dsp:listURIofClass rdf:rest*/rdf:first ?element } LIMIT 20";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Class usage count";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?uri AS ?Class) (?val AS ?usage) WHERE { dsp:listClassUsageCount rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asValue ?val ." +
							" } LIMIT 50";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of class defined";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_defined_class) WHERE {dsp:thisOntology dsp:numberOfClassDefined ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of class Defined";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?element AS ?Class_defined) WHERE { dsp:listURIofClassDefined rdf:rest*/rdf:first ?element } LIMIT 20";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Number of class not defined";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_not_defined_class) WHERE {dsp:thisOntology dsp:numberOfClassNotDefined ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of class Not Defined";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?element AS ?Class_not_defined) WHERE { dsp:listURIofClassNotDefined rdf:rest*/rdf:first ?element } ";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of class of interest";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_class_of_interest) WHERE {dsp:thisOntology dsp:numberOfClassOfInterest ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of class of interest";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?URI1 AS ?Class1) (?URI2 AS ?Class2) WHERE { dsp:listURIofClassOfInterest rdf:rest*/rdf:first ?element ." +
		" ?element dsp:asClass1 ?URI1 ." +
		" ?element dsp:asClass2 ?URI2 ." +
		" } LIMIT 100";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of distinct property";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_distinct_property) WHERE {dsp:thisOntology dsp:numberOfDistinctProperty ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of property";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?element AS ?Property) WHERE { dsp:listURIofProperty rdf:rest*/rdf:first ?element } LIMIT 20";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of property of interest";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_property_of_interest) WHERE {dsp:thisOntology dsp:numberOfPropertyOfInterest ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "List of property of interest";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?URI1 AS ?Class1) (?URI3 AS ?Property) (?URI2 AS ?Class2) WHERE { dsp:listURIofPropertyOfInterest rdf:rest*/rdf:first ?element ." +
		" ?element dsp:asClass1 ?URI1 ." +
		" ?element dsp:asClass2 ?URI2 ." +
		" ?element dsp:asProperty ?URI3 ." +
		" } LIMIT 100";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Test";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (SUM(?val)/COUNT(?uri) AS ?usage) WHERE { dsp:listClassUsageCount rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asValue ?val ." +
							" } ";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Test 2";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?p AS ?property) (COUNT(?p) AS ?count) WHERE { " +
							" ?s rdf:type <http://dbkwik.webdatacommons.org/marvelcinematicuniverse.wikia.com/class/actor> ." +
							" ?s ?p ?o ." +
							" } GROUP BY ?p";
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
		" } LIMIT 20";
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

		

		titleQuery = "Number of distinct subject of properties";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubject ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of Property usage distinct per subject";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_property_usage_distinct_per_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectSum ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Mean of Property usage distinct per subject";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Mean_of_property_usage_distinct_per_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMean ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Minimun of Property usage distinct per subject";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Min_of_property_usage_distinct_per_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMin ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Maximun of Property usage distinct per subject";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Max_of_property_usage_distinct_per_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMax ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of distinct object of properties";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubject ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Number of Property usage distinct per object";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_property_usage_distinct_per_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectSum ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Mean of Property usage distinct per object";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Mean_of_property_usage_distinct_per_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMean ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Minimun of Property usage distinct per object";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Min_of_property_usage_distinct_per_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMin ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Maximun of Property usage distinct per object";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Max_of_property_usage_distinct_per_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMax ?number.}";
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
		" } LIMIT 20";
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
		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:distinctEntities ?number.} LIMIT 20";
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
		stringQuery = prefix + "SELECT (?element AS ?Datatype) WHERE { dsp:listOfDatatypes rdf:rest*/rdf:first ?element } LIMIT 20";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Languages";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?element AS ?Language) WHERE { dsp:listOfLanguages rdf:rest*/rdf:first ?element }";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Typed string length";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Average_length) WHERE {dsp:thisOntology dsp:typedStringLength ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Untyped string length";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Average_length) WHERE {dsp:thisOntology dsp:untypedStringLength ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Typed subjects";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_typed_subjects) WHERE {dsp:thisOntology dsp:typedSubjects ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Labeled subjects";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_labeled_subjects) WHERE {dsp:thisOntology dsp:labeledSubjects ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Same As";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_same_as) WHERE {dsp:thisOntology dsp:sameAs ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Links";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Number_of_links) WHERE {dsp:thisOntology dsp:links ?number.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Max per property";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?uri AS ?Property) (?datatype AS ?Datatype) (?val AS ?Max) WHERE { dsp:listOfMaxPerProperty rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asStr ?datatype ." +
							" ?element dsp:asValue ?val ." +
							" } ORDER BY DESC (?val) LIMIT 20";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Average per property";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?uri AS ?Property) (?datatype AS ?Datatype) (?val AS ?Average) WHERE { dsp:listOfPerProperty rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asStr ?datatype ." +
							" ?element dsp:asValue ?val ." +
							" }ORDER BY DESC (?val) LIMIT 20";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		
		 titleQuery = "Number of subject vocabularies";
		 typeQuery = "SELECT";
		 stringQuery = prefix + "SELECT (?number AS ?Number_of_subject_vocabularies) WHERE {dsp:thisOntology dsp:numberOfSubjectVocabularies ?number.}";
		 listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		 titleQuery = "Subject vocabularies";
		 typeQuery = "SELECT";
		 stringQuery = prefix + "SELECT (?element AS ?Vocabulary) WHERE { dsp:listOfSubjectVocabularies rdf:rest*/rdf:first ?element } LIMIT 20";
		 listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		 titleQuery = "Number of predicat vocabularies";
		 typeQuery = "SELECT";
		 stringQuery = prefix + "SELECT (?number AS ?Number_of_predicat_vocabularies) WHERE {dsp:thisOntology dsp:numberOfPredicatVocabularies ?number.}";
		 listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		 titleQuery = "Predicat vocabularies";
		 typeQuery = "SELECT";
		 stringQuery = prefix + "SELECT (?element AS ?Vocabulary) WHERE { dsp:listOfPredicatVocabularies rdf:rest*/rdf:first ?element } LIMIT 20";
		 listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		 titleQuery = "Number of object vocabularies";
		 typeQuery = "SELECT";
		 stringQuery = prefix + "SELECT (?number AS ?Number_of_object_vocabularies) WHERE {dsp:thisOntology dsp:numberOfObjectVocabularies ?number.}";
		 listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		 titleQuery = "Object vocabularies";
		 typeQuery = "SELECT";
		 stringQuery = prefix + "SELECT (?element AS ?Vocabulary) WHERE { dsp:listOfObjectVocabularies rdf:rest*/rdf:first ?element } LIMIT 20";
		 listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		 

		//Sauvegarde des queries dans un fichier json
		//ObjectMapper objectMapper = new ObjectMapper();
	    //objectMapper.writeValue(new File("C:\\var\\www\\profiling\\queries\\queryProfiling.json"), listQuery);
		
		Instant start0 = Instant.now();
		
		listRules.add("profiling.rules");
		listDatasets.add("source.json");
		fileNameTestResult = "Result_Test_profiling.json";
		topSpatial = "false";
		consoleOutput = "true";
		
		CreateInferredModelAndRunQueries.InferencesAndQuery(listDatasets, listRules, topSpatial, listQuery, consoleOutput, fileNameTestResult);
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}