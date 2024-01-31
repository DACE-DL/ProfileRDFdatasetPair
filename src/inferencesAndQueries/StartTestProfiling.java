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
		Integer iDquery = 0;
		String titleQuery = ""; 
		String commentQuery = "";
		String typeQuery = "";
		String stringQuery = "";
		ArrayList<String> listRules = new ArrayList<String>();
		String idPair = "";
		ArrayList<String> listDatasets = new ArrayList<String>();
		String fileNameTestResult = "";
		String topSpatial = "";
		String consoleOutput = "";
		ArrayList<ProfilingQueryObject> listQuery = new ArrayList<ProfilingQueryObject>();

		/////////////////////////////////////////////////////
		// Injection des données nécessaires pour l'étude  //
		/////////////////////////////////////////////////////
		listQuery.clear();
		titleQuery = ""; 
		commentQuery = "";
		iDquery = 0;
		typeQuery = "INSERT";

		// Insertion dans le modèle du nameSpace de l'ontologie (pour les rules)
		stringQuery = prefix + "INSERT DATA {dsp:thisOntology dsp:hasNameSpace dsp:.}";
		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


		
		/////////////////////////////
		// Affichage des résultats //
		/////////////////////////////

		titleQuery = "PROFILE OF A DATASET";
		commentQuery = "";
		iDquery = 0;
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


		titleQuery = "Number of triples";
		commentQuery = "Number of triples in the dataset.";
		iDquery = 1;
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?number AS ?Triples) WHERE {dsp:thisOntology dsp:numberOfTriples ?number.}";
		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of distinct property";
// 		commentQuery = "Number of distinct properties in the dataset.";
// 		iDquery = 2;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_distinct_property) WHERE {dsp:thisOntology dsp:numberOfPropertyUsage ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of distinct subject of properties";
// 		commentQuery = "";
// 		iDquery = 50;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubject ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of Property usage distinct per subject";
// 		commentQuery = "";
// 		iDquery = 51;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_property_usage_distinct_per_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectSum ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Mean of Property usage distinct per subject";
// 		commentQuery = "";
// 		iDquery = 52;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Mean_of_property_usage_distinct_per_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMean ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Minimun of Property usage distinct per subject";
// 		commentQuery = "";
// 		iDquery = 53;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Min_of_property_usage_distinct_per_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMin ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Maximun of Property usage distinct per subject";
// 		commentQuery = "";
// 		iDquery = 54;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Max_of_property_usage_distinct_per_subject) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMax ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of distinct object of properties";
// 		commentQuery = "";
// 		iDquery = 55;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubject ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of Property usage distinct per object";
// 		commentQuery = "";
// 		iDquery = 56;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_property_usage_distinct_per_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectSum ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Mean of Property usage distinct per object";
// 		commentQuery = "";
// 		iDquery = 57;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Mean_of_property_usage_distinct_per_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMean ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Minimun of Property usage distinct per object";
// 		commentQuery = "";
// 		iDquery = 58;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Min_of_property_usage_distinct_per_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMin ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Maximun of Property usage distinct per object";
// 		commentQuery = "";
// 		iDquery = 59;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Max_of_property_usage_distinct_per_object) WHERE {dsp:thisOntology dsp:numberOfPropertyUsageDistinctPerSubjectMax ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "Statistiques property usage count";
// 		commentQuery = "Statistics on the number of uses within a triplet of different properties.";
// 		iDquery = 3;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT  " +
// 				"(str(?valueHQ1) AS ?Quantile_25)"+ 
// 				"(str(?valueHQ2) AS ?Quantile_50)"+ 
// 				"(str(?valueHQ3) AS ?Quantile_75)"+ 
// 				" WHERE {" +
// 				"dsp:thisOntology dsp:hasPropertyUsageCountQ1 ?valueHQ1." +
// 				"dsp:thisOntology dsp:hasPropertyUsageCountQ2 ?valueHQ2." +
// 				"dsp:thisOntology dsp:hasPropertyUsageCountQ3 ?valueHQ3." +
// 				"} ";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Property Most Used";
// 		commentQuery = "";
// 		iDquery = 4;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?uri AS ?Property) (?val AS ?usage) WHERE { dsp:listPropertyMostUsed rdf:rest*/rdf:first ?element ." +
// 							" ?element dsp:asURI ?uri ." +
// 							" ?element dsp:asValue ?val ." +
// 							" } LIMIT 20";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of property most used";
// 		commentQuery = "";
// 		iDquery = 5;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_property_most_used) WHERE {dsp:thisOntology dsp:numberOfPropertyMostUsed ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "List of property and subproperty";
// 		commentQuery = "";
// 		iDquery = 6;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?URI1 AS ?Property) (?URI2 AS ?Subproperty) WHERE { dsp:listPropertyAndSubproperty rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asProperty ?URI1 ." +
// 		" ?element dsp:asSubproperty ?URI2 ." +
// 		" } LIMIT 20";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "Out degree";
// 		commentQuery = "";
// 		iDquery = 7;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Outdegree) WHERE {dsp:thisOntology dsp:outdegree ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "In degree";
// 		commentQuery = "";
// 		iDquery = 8;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Indegree) WHERE {dsp:thisOntology dsp:indegree ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Property hierarchy deep";
// 		commentQuery = "";
// 		iDquery = 9;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Max_depp) WHERE {dsp:thisOntology dsp:propertyHierarchyDeep ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Property hierarchy infinite loop";
// 		commentQuery = "";
// 		iDquery = 10;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?loop AS ?Infinite_loop) WHERE {dsp:thisOntology dsp:propertyHierarchyInfiniteLoop ?loop.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Subclass usage";
// 		commentQuery = "";
// 		iDquery = 11;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:subclassUsage ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Entities mentioned";
// 		commentQuery = "";
// 		iDquery = 12;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:entitiesMentioned ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Distinct entities";
// 		commentQuery = "";
// 		iDquery = 13;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:distinctEntities ?number.} LIMIT 20";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of literals";
// 		commentQuery = "";
// 		iDquery = 14;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:numberOfLiterals ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of blanks as subject";
// 		commentQuery = "";
// 		iDquery = 15;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:numberBlanksAsSubj ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of blanks as object";
// 		commentQuery = "";
// 		iDquery = 16;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number) WHERE {dsp:thisOntology dsp:numberBlanksAsObj ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Datatypes";
// 		commentQuery = "";
// 		iDquery = 17;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?uri AS ?Datatype) WHERE { dsp:listOfDatatypes rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asURI ?uri ." +
// 		" } LIMIT 20";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "Number of datatypes";
// 		commentQuery = "";
// 		iDquery = 18;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_datatypes) WHERE {dsp:thisOntology dsp:numberOfDatatypes ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Languages";
// 		commentQuery = "";
// 		iDquery = 19;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?element AS ?Language) WHERE { dsp:listOfLanguages rdf:rest*/rdf:first ?element }";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Typed string length";
// 		commentQuery = "";
// 		iDquery = 20;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Average_length) WHERE {dsp:thisOntology dsp:typedStringLength ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Untyped string length";
// 		commentQuery = "";
// 		iDquery = 21;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Average_length) WHERE {dsp:thisOntology dsp:untypedStringLength ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Typed subjects";
// 		commentQuery = "";
// 		iDquery = 22;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_typed_subjects) WHERE {dsp:thisOntology dsp:typedSubjects ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Labeled subjects";
// 		commentQuery = "";
// 		iDquery = 23;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_labeled_subjects) WHERE {dsp:thisOntology dsp:labeledSubjects ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Same As";
// 		commentQuery = "";
// 		iDquery = 24;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_same_as) WHERE {dsp:thisOntology dsp:sameAs ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Links";
// 		commentQuery = "";
// 		iDquery = 25;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_links) WHERE {dsp:thisOntology dsp:links ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Max per property";
// 		commentQuery = "";
// 		iDquery = 26;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?uri AS ?Property) (?datatype AS ?Datatype) (?val AS ?Max) WHERE { dsp:listOfMaxPerProperty rdf:rest*/rdf:first ?element ." +
// 							" ?element dsp:asURI ?uri ." +
// 							" ?element dsp:asStr ?datatype ." +
// 							" ?element dsp:asValue ?val ." +
// 							" } ORDER BY DESC (?val) LIMIT 20";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Average per property";
// 		commentQuery = "";
// 		iDquery = 27;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?uri AS ?Property) (?datatype AS ?Datatype) (?val AS ?Average) WHERE { dsp:listOfPerProperty rdf:rest*/rdf:first ?element ." +
// 							" ?element dsp:asURI ?uri ." +
// 							" ?element dsp:asStr ?datatype ." +
// 							" ?element dsp:asValue ?val ." +
// 							" }ORDER BY DESC (?val) LIMIT 20";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		 titleQuery = "Subject vocabularies";
// 		 commentQuery = "";
// 		 iDquery = 28;
// 		 typeQuery = "SELECT";
// 		 stringQuery = prefix + "SELECT (?element AS ?Vocabulary) WHERE { dsp:listOfSubjectVocabularies rdf:rest*/rdf:first ?element } LIMIT 20";
// 		 listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		 titleQuery = "Number of subject vocabularies";
// 		 commentQuery = "";
// 		 iDquery = 29;
// 		 typeQuery = "SELECT";
// 		 stringQuery = prefix + "SELECT (?number AS ?Number_of_subject_vocabularies) WHERE {dsp:thisOntology dsp:numberOfSubjectVocabularies ?number.}";
// 		 listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		 titleQuery = "Predicat vocabularies";
// 		 commentQuery = "";
// 		 iDquery = 30;
// 		 typeQuery = "SELECT";
// 		 stringQuery = prefix + "SELECT (?element AS ?Vocabulary) WHERE { dsp:listOfPredicatVocabularies rdf:rest*/rdf:first ?element } LIMIT 20";
// 		 listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		 titleQuery = "Number of predicat vocabularies";
// 		 commentQuery = "";
// 		 iDquery = 31;
// 		 typeQuery = "SELECT";
// 		 stringQuery = prefix + "SELECT (?number AS ?Number_of_predicat_vocabularies) WHERE {dsp:thisOntology dsp:numberOfPredicatVocabularies ?number.}";
// 		 listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		 titleQuery = "Object vocabularies";
// 		 commentQuery = "";
// 		 iDquery = 32;
// 		 typeQuery = "SELECT";
// 		 stringQuery = prefix + "SELECT (?element AS ?Vocabulary) WHERE { dsp:listOfObjectVocabularies rdf:rest*/rdf:first ?element } LIMIT 20";
// 		 listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

//          titleQuery = "Number of object vocabularies";
// 		 commentQuery = "";
// 		 iDquery =32;
// 		 typeQuery = "SELECT";
// 		 stringQuery = prefix + "SELECT (?number AS ?Number_of_object_vocabularies) WHERE {dsp:thisOntology dsp:numberOfObjectVocabularies ?number.}";
// 		 listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 		// For the classes
// 		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
// 		titleQuery = "CLASS";
// 		commentQuery = "";
// 		iDquery = 0;
// 		typeQuery = "";
// 		stringQuery = "";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "Number of distinct classes";
// 		commentQuery = "Number of distinct classes in the dataset.";
// 		iDquery = 33;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_distinct_class) WHERE {dsp:thisOntology dsp:numberOfClassUsage ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "Class usage count";
// 		commentQuery = "";
// 		iDquery = 34;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?uri AS ?Class) (?val AS ?usage) WHERE { dsp:listClassUsageCount rdf:rest*/rdf:first ?element ." +
// 							" ?element dsp:asURI ?uri ." +
// 							" ?element dsp:asValue ?val ." +
// 							" } LIMIT 20";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));
		
	
// 		titleQuery = "List of class Defined";
// 		commentQuery = "";
// 		iDquery = 36;				
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?element AS ?Class_defined) WHERE { dsp:listClassDefined rdf:rest*/rdf:first ?element } LIMIT 20";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "Number of class defined";
// 		commentQuery = "";
// 		iDquery = 37;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_defined_class) WHERE {dsp:thisOntology dsp:numberOfClassDefined ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "List of class Not Defined";
// 		commentQuery = "";
// 		iDquery = 38;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?element AS ?Class_not_defined) WHERE { dsp:listClassNotDefined rdf:rest*/rdf:first ?element } ";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));
		
// 		titleQuery = "Number of class not defined";
// 		commentQuery = "";
// 		iDquery = 39;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_not_defined_class) WHERE {dsp:thisOntology dsp:numberOfClassNotDefined ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Class hierarchy deep";
// 		commentQuery = "";
// 		iDquery = 40;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Max_depp) WHERE {dsp:thisOntology dsp:classHierarchyDeep ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Class hierarchy infinite loop";
// 		commentQuery = "";
// 		iDquery = 41;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?loop AS ?Infinite_loop) WHERE {dsp:thisOntology dsp:classHierarchyInfiniteLoop ?loop.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Statistiques class usage count";
// 		commentQuery = "Statistics on the number of uses of classes to characterize dataset resources.";
// 		iDquery = 42;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT  " +
// 				"(str(?valueHQ1) AS ?Quantile_25)"+ 
// 				"(str(?valueHQ2) AS ?Quantile_50)"+ 
// 				"(str(?valueHQ3) AS ?Quantile_75)"+ 
// 				" WHERE {" +
// 				"dsp:thisOntology dsp:hasClassUsageCountQ1 ?valueHQ1." +
// 				"dsp:thisOntology dsp:hasClassUsageCountQ2 ?valueHQ2." +
// 				"dsp:thisOntology dsp:hasClassUsageCountQ3 ?valueHQ3." +
// 				"} ";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Class Most Used";
// 		commentQuery = "";
// 		iDquery = 43;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?uri AS ?Property) (?val AS ?usage) WHERE { dsp:listClassMostUsed rdf:rest*/rdf:first ?element ." +
// 							" ?element dsp:asURI ?uri ." +
// 							" ?element dsp:asValue ?val ." +
// 							" } LIMIT 20";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "Number of class most used";
// 		commentQuery = "";
// 		iDquery = 44;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_class_most_used) WHERE {dsp:thisOntology dsp:numberOfClassMostUsed ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "List of class and subclass";
// 		commentQuery = "";
// 		iDquery = 45;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?URI1 AS ?Class) (?URI2 AS ?Subclass) WHERE { dsp:listClassAndSubclass rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asClass ?URI1 ." +
// 		" ?element dsp:asSubclass ?URI2 ." +
// 		" } LIMIT 20";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

// 		titleQuery = "Number of class and subclass";
// 		commentQuery = "";
// 		iDquery = 46;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_class_and_subclass) WHERE {dsp:thisOntology dsp:numberOfClassAndSubclass ?number.}";
// 		listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
		
		
		
		
		
		
		
		
		
		
// 		titleQuery = "List of most used properties with domaine and range";
// 		commentQuery = "";
// iDquery = 60;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?property AS ?Property) (?class1 AS ?Class1) (?class2 AS ?Class2) WHERE { dsp:listPropertyWithDomaineAndRange rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asProperty ?property ." +
// 		" ?element dsp:asClass1 ?class1 ." +
// 		" ?element dsp:asClass2 ?class2 ." +
// 		" } ORDER BY ?Property LIMIT 20 ";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
		
		
		
		
		
		
		
		
		
// 		titleQuery = "Number of most used properties with domaine and range";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_class_most_used_with_domain_range) WHERE {dsp:thisOntology dsp:numberOfPropertyWithDomaineAndRange ?number.}";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
		
// 		titleQuery = "test sujet";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT DISTINCT (?p AS ?Property) ?class (COUNT(?s) AS ?Nombre_de_sujet) WHERE { " +
// 		" ?s ?p ?o ." +
// 		" ?p rdf:type <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> ." +
// 		" ?s rdf:type ?class " +
// 		"} GROUP BY ?p ?class ";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "test objet";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT DISTINCT (?p AS ?Property) ?class (COUNT(?o) AS ?Nombre_d_objet) WHERE { " +
// 		" ?s ?p ?o ." +
// 		" ?p rdf:type <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> ." +
// 		" ?o rdf:type ?class " +
// 		"} GROUP BY ?p ?class ";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "List of classes of interest in pair";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?uri1 AS ?Class1) (?uri2 AS ?Class2) WHERE { dsp:listClassOfInterest rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asClass1 ?uri1 ." +
// 		" ?element dsp:asClass2 ?uri2 ." +
// 		" } LIMIT 10";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Number of class of interest in pair";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_class_of_interest) WHERE {dsp:thisOntology dsp:numberOfClassOfInterest ?number.}";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		

		
		


		

// 		titleQuery = "Number of distinct property";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_distinct_property) WHERE {dsp:thisOntology dsp:numberOfDistinctProperty ?number.}";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "List of property";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?element AS ?Property) WHERE { dsp:listURIofProperty rdf:rest*/rdf:first ?element } LIMIT 20";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of property of interest with domaine and range";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?number AS ?Number_of_property_of_interest) WHERE {dsp:thisOntology dsp:numberOfPropertyOfInterest ?number.}";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "List of property of interest with domaine and range";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?property AS ?Property) (?class1 AS ?Class1) (?class2 AS ?Class2) WHERE { dsp:listURIofPropertyOfInterest rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asClass1 ?class1 ." +
// 		" ?element dsp:asClass2 ?class2 ." +
// 		" ?element dsp:asProperty ?property ." +
// 		" } ORDER BY ?Property LIMIT 100 ";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "Number of property of interest";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (COUNT(DISTINCT ?Property) AS ?Number_of_property_of_interest) WHERE {dsp:listURIofPropertyOfInterest rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asProperty ?Property ." +
// 		" } ORDER BY ?Property";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "List of property of interest";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT DISTINCT ?Property  WHERE { dsp:listURIofPropertyOfInterest rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asProperty ?Property ." +
// 		" } ORDER BY ?Property";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "List of subject classes and their properties";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?class1 AS ?Class1) (?property AS ?Property) WHERE { dsp:listURIofPropertyOfInterest rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asClass1 ?class1 ." +
// 		" ?element dsp:asClass2 ?class2 ." +
// 		" ?element dsp:asProperty ?property ." +
// 		" } GROUP BY ?class1 ?property ORDER BY ?class1 LIMIT 100 ";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


// 		titleQuery = "List of properties and their object classes";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT (?property AS ?Property) (?class2 AS ?Class2)  WHERE { dsp:listURIofPropertyOfInterest rdf:rest*/rdf:first ?element ." +
// 		" ?element dsp:asClass1 ?class1 ." +
// 		" ?element dsp:asClass2 ?class2 ." +
// 		" ?element dsp:asProperty ?property ." +
// 		" } GROUP BY ?class2 ?property ORDER BY ?property LIMIT 100 ";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));

		
// 		titleQuery = "Test";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "SELECT";
// 		stringQuery = prefix + "SELECT * WHERE { " +
// 							" <http://dbkwik.webdatacommons.org/marvelcinematicuniverse.wikia.com/resource/Chris_Pratt> ?p ?o ." +
// 							" } ";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));
		
		
		
// 		titleQuery = "PROPERTIES";
// 		commentQuery = "";
// iDquery = 0;
// 		typeQuery = "";
// 		stringQuery = "";
// 		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


		

		


		
		//titleQuery = "Number of distinct property";
		//typeQuery = "SELECT";
		//stringQuery = prefix + "SELECT (?number AS ?Number_of_distinct_property) WHERE {dsp:thisOntology dsp:numberOfDistinctProperty ?number.}";
		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


		//titleQuery = "Property usage count";
		//typeQuery = "SELECT";
		//stringQuery = prefix + "SELECT (?uri AS ?Property) (?val AS ?usage) WHERE { dsp:listPropertyUsageCount rdf:rest*/rdf:first ?element ." +
		//					" ?element dsp:asURI ?uri ." +
		//					" ?element dsp:asValue ?val ." +
		//					" } LIMIT 50";
		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


		//titleQuery = "Class usage count";
		//typeQuery = "SELECT";
		//stringQuery = prefix + "SELECT (?uri AS ?Class) (?val AS ?usage) WHERE { dsp:listClassUsageCount rdf:rest*/rdf:first ?element ." +
		//					" ?element dsp:asURI ?uri ." +
		//					" ?element dsp:asValue ?val ." +
		//					" } LIMIT 50";
		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


		//titleQuery = "Mean class usage count";
		//typeQuery = "SELECT";
		//stringQuery = prefix + "SELECT  (AVG(?val) AS ?mean) WHERE { dsp:listClassUsageCount rdf:rest*/rdf:first ?element ." +
		//					" ?element dsp:asURI ?uri ." +
		//					" ?element dsp:asValue ?val ." +
		//					" } ";
		//listQuery.add(new ProfilingQueryObject(iDquery, titleQuery, commentQuery, typeQuery, stringQuery));


		//Sauvegarde des queries dans un fichier json
		//ObjectMapper objectMapper = new ObjectMapper();
	    //objectMapper.writeValue(new File("C:\\var\\www\\profiling\\queries\\queryProfiling.json"), listQuery);
		
		Instant start0 = Instant.now();
		
		listRules.add("profiling.rules");
		idPair = "starwars-swtor";
		listDatasets.add("source.json");
		fileNameTestResult = "Result_Test_profiling.json";
		topSpatial = "false";
		consoleOutput = "true";
		
		CreateInferredModelAndRunQueries.InferencesAndQuery(idPair, listDatasets, listRules, topSpatial, listQuery, consoleOutput, fileNameTestResult);
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}