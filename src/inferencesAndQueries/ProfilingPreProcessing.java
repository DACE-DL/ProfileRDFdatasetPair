package inferencesAndQueries;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import profiling.util.*;

public class ProfilingPreProcessing {
	
	// On effectue les pré-traitements
	public static void makeTreatements(Model model) {

		ProfilingResultsObject results = new ProfilingResultsObject();

		// Nombre de triplets du jeux de données
       	Integer numberOfTriples = GiveNumberOfTriples.giveNumber(model);
		results.setNumberOfTriples(numberOfTriples);

		// Liste des propriétés et de leur usage.
		String nameOfListPropertyUsageCount = "listPropertyUsageCount";
		ArrayList<UriAndNumber> listPropertyUsageCount = new ArrayList<UriAndNumber>();
		listPropertyUsageCount = MakeListPropertyUsageCount.makeList(model, nameOfListPropertyUsageCount);
		
		// Liste des usages distincts par propriétés par subject.
		Usage propertyUsageDistinctPerSubject = new Usage();
		propertyUsageDistinctPerSubject = GivePropertyUsageDistinctPerSubject.giveUsage(model);
		results.setPropertyUsageDistinctPerSubjectSubjectCount(propertyUsageDistinctPerSubject.getResourceCount());
		results.setPropertyUsageDistinctPerSubjectUsageSum(propertyUsageDistinctPerSubject.getUsageSum());
		results.setPropertyUsageDistinctPerSubjectUsageMean(propertyUsageDistinctPerSubject.getUsageMean());
		results.setPropertyUsageDistinctPerSubjectUsageMin(propertyUsageDistinctPerSubject.getUsageMin());
		results.setPropertyUsageDistinctPerSubjectUsageMax(propertyUsageDistinctPerSubject.getUsageMax());

		// Liste des usages distincts par propriétés par object.
		Usage propertyUsageDistinctPerObject = new Usage();
		propertyUsageDistinctPerObject = GivePropertyUsageDistinctPerObject.giveUsage(model);
		results.setPropertyUsageDistinctPerObjectObjectCount(propertyUsageDistinctPerObject.getResourceCount());
		results.setPropertyUsageDistinctPerObjectUsageSum(propertyUsageDistinctPerObject.getUsageSum());
		results.setPropertyUsageDistinctPerObjectUsageMean(propertyUsageDistinctPerObject.getUsageMean());
		results.setPropertyUsageDistinctPerObjectUsageMin(propertyUsageDistinctPerObject.getUsageMin());
		results.setPropertyUsageDistinctPerObjectUsageMax(propertyUsageDistinctPerObject.getUsageMax());
		
		// Indegree
		double inDegree = 0.0;
		inDegree = GiveInDegree.giveDegree(model);
		results.setInDegree(inDegree);

		// Outdegree
		double outDegree = 0.0;
		outDegree = GiveOutDegree.giveDegree(model);
		results.setOutDegree(outDegree);

		// Liste des propriétés et des sous-propriétés.
		String nameOfListPropertyAndSubproperty = "listPropertyAndSubproperty";
		ArrayList<UriAndUri> listPropertyAndSubproperty = new ArrayList<UriAndUri>();
		listPropertyAndSubproperty = MakeListPropertyAndSubproperty.makeList(model, nameOfListPropertyAndSubproperty);

		// Liste des classes et de leur usage.
		String nameOfListClassUsageCount = "listClassUsageCount";
		ArrayList<UriAndNumber> listClassUsageCount = new ArrayList<UriAndNumber>();
		listClassUsageCount = MakeListClassUsageCount.makeList(model, nameOfListClassUsageCount);

		// Properties hierarchy deep and loop
		HierarchyDeepAndLoop hierarchyDeepAndLoop = new HierarchyDeepAndLoop();
		hierarchyDeepAndLoop = GivePropertyHierarchyDeep.giveHierarchyDeepAndLoop(model, nameOfListPropertyAndSubproperty);
		results.setPropertyHierarchyDeep(hierarchyDeepAndLoop.getHierarchyDeep());
		results.setPropertyHierarchyLoop(hierarchyDeepAndLoop.getLoop());
		
		// SubclassUsage
		Integer numberOfSubclassUsage = GiveSubclassUsage.giveUsage(model);
		results.setSubclassUsage(numberOfSubclassUsage);
		
		// Entities mentioned
		Integer numberEntitiesMentioned = GiveEntitiesMentioned.giveNumber(model);
		results.setEntitiesMentioned(numberEntitiesMentioned);

		// Distinct Entities
		Integer numberDistinctEntities = GiveDistinctEntities.giveNumber(model);
		results.setDistinctEntities(numberDistinctEntities);

		// Literals
		Integer numberOfLiterals = GiveLiterals.giveNumber(model);
		results.setNumberOfLiterals(numberOfLiterals);
		
		// Blanks as subject
		Integer numberOfBlanksAsSubj = GiveBlanksAsSubj.giveNumber(model);
		results.setNumberBlanksAsSubj(numberOfBlanksAsSubj);

		// Blanks as object
		Integer numberOfBlanksAsObj = GiveBlanksAsObj.giveNumber(model);
		results.setNumberBlanksAsObj(numberOfBlanksAsObj);

		// Liste of datatypes.
		String nameOfListDatatypes = "listOfDatatypes";
		ArrayList<Uri> listOfDatatypes = new ArrayList<Uri>();
		listOfDatatypes = MakeListDatatypes.makeList(model, nameOfListDatatypes);
		
		// Liste of languages.
		String nameOfListLanguages = "listOfLanguages";
		ArrayList<Uri> listOfLanguages = new ArrayList<Uri>();
		listOfLanguages = MakeListLanguages.makeList(model, nameOfListLanguages);

		// Typed String Length
		double numberOfTypedStringLength = GiveTypedStringLength.giveLength(model);
		results.setNumberTypedStringLength(numberOfTypedStringLength);

		// Untyped String Length
		double numberOfUntypedStringLength = GiveUntypedStringLength.giveLength(model);
		results.setNumberUntypedStringLength(numberOfUntypedStringLength);


		// Typed Subjects
		Integer numberOfTypedSubjects = GiveTypedSubjects.giveNumber(model);
		results.setNumberTypedSubjects(numberOfTypedSubjects);

		// Labeled Subjects
		Integer numberOfLabeledSubjects = GiveLabeledSubjects.giveNumber(model);
		results.setNumberLabeledSubjects(numberOfLabeledSubjects);

		// Same As
		Integer numberOfSameAs = GiveSameAs.giveNumber(model);
		results.setNumberSameAs(numberOfSameAs);

		// List of links between domain names. A optimiser
		String nameOfListLinks = "listLinks";
		ArrayList<UriAndUri> listLinks = new ArrayList<UriAndUri>();
		listLinks = MakeListLinks.makeList(model, nameOfListPropertyUsageCount, nameOfListLinks);

		// List of Max per Property.
		String nameOfListMaxPerProperty = "listMaxPerProperty"; 
		ArrayList<UriAndStringAndBigNumber> listMaxPerProperty = new ArrayList<UriAndStringAndBigNumber>();
		listMaxPerProperty = MakeListMaxPerProperty.makeList(model, nameOfListMaxPerProperty);

		// List of Per Property.
		String nameOfListPerProperty = "listPerProperty"; 
		ArrayList<UriAndStringAndBigNumber> listPerProperty = new ArrayList<UriAndStringAndBigNumber>();
		listPerProperty = MakeListPerProperty.makeList(model, nameOfListPerProperty);

		// Lists of vocabularies.
		String nameOfListSvocabulary = "listSubjectVocabulary"; 
		String nameOfListPvocabulary = "listPredicatVocabulary"; 
		String nameOfListOvocabulary = "listObjectVocabulary"; 
		ArrayList<List<String>> listResourcesSPO = new ArrayList<List<String>>(3);
		listResourcesSPO = MakeListSPOvocabularies.makeList(model, nameOfListSvocabulary, nameOfListPvocabulary, nameOfListOvocabulary);
		List<String> listDistinctSubjectVocabularies = new ArrayList<>();
		List<String> listDistinctPredicatVocabularies = new ArrayList<>();
		List<String> listDistinctObjectVocabularies = new ArrayList<>();
		listDistinctSubjectVocabularies = listResourcesSPO.get(0);
		listDistinctPredicatVocabularies = listResourcesSPO.get(1);
		listDistinctObjectVocabularies = listResourcesSPO.get(2);
        
		// Construction d'un vecteur pour l'usage des propriétés.
		String vectorUsageProperty = "c(0.0)";
		vectorUsageProperty = MakeVectorWithListUriAndNumber.makeVector(model, nameOfListPropertyUsageCount);
		//System.out.println("Vector Usages Properties : " + vectorUsageProperty);

		// Calcul quantile pour l'usage des propriétés.
		double propertyUsageQuantile75 = 0.0;
		propertyUsageQuantile75 = GiveRQuantile.giveDouble(vectorUsageProperty, "c(0.75)");
		//System.out.println("Properties Usage Quantile 75 : " + propertyUsageQuantile75);
		
		// Liste des propriétés les plus utilisées (quartile 75).
		String nameOfListPropertyMostUsed = "listPropertyMostUsed";
		ArrayList<UriAndNumber> listPropertyMostUsed = new ArrayList<UriAndNumber>();
		listPropertyMostUsed = MakeListPropertyMostUsed.makeList(model, nameOfListPropertyMostUsed, propertyUsageQuantile75 );

		//// For the Classes ////

		// Liste of classes defined.
		String nameOfListClassDefined = "listClassDefined";
		ArrayList<Uri> listOfClassDefined = new ArrayList<Uri>();
		listOfClassDefined = MakeListClassDefined.makeList(model, nameOfListClassDefined);

		// Liste of classes not defined.
		String nameOfListClassNotDefined = "listClassNotDefined";
		ArrayList<Uri> listOfClassNotDefined = new ArrayList<Uri>();
		listOfClassNotDefined = MakeListClassNotDefined.makeList(model, nameOfListClassNotDefined);

		// Classes hierarchy deep and loop
		HierarchyDeepAndLoop ClassHierarchyDeepAndLoop = new HierarchyDeepAndLoop();
		ClassHierarchyDeepAndLoop = GiveClassHierarchyDeep.giveHierarchyDeepAndLoop(model, nameOfListPropertyAndSubproperty);
		results.setClassHierarchyDeep(ClassHierarchyDeepAndLoop.getHierarchyDeep());
		results.setClassHierarchyLoop(ClassHierarchyDeepAndLoop.getLoop());

		// Construction d'un vecteur pour l'usage des classes.
		String vectorUsageClass = "c(0.0)";
		vectorUsageClass = MakeVectorWithListUriAndNumber.makeVector(model, nameOfListClassUsageCount);
		//System.out.println("Vector Usages Classes : " + vectorUsageClass);

		// Calcul quantile pour l'usage des classes.
		double classUsageQuantile75 = 0.0;
		classUsageQuantile75 = GiveRQuantile.giveDouble(vectorUsageClass, "c(0.75)");
		//System.out.println("Class Usage Quantile 75 : " + classUsageQuantile75);

		// Liste des classes les plus utilisées (quartile 75).
		String nameOfListClassMostUsed = "listClassMostUsed";
		ArrayList<UriAndNumber> listClassMostUsed = new ArrayList<UriAndNumber>();
		listClassMostUsed = MakeListClassMostUsed.makeList(model, nameOfListClassMostUsed, classUsageQuantile75);

		
		// Liste des classes les plus utilisées et leur connections avec d'autres classes.
		String nameOfListClassOfInterest = "listClassOfInterest";
		ArrayList<UriAndUriAndUri> listClassOfInterest = new ArrayList<UriAndUriAndUri>();
		listClassOfInterest = MakeListClassOfInterest.makeList(model, nameOfListClassOfInterest);

		// Liste des classes les plus utilisées et leur connections avec d'autres classes.
		String nameOfListClassOfInterestCount = "listClassOfInterestCount";
		ArrayList<UriAndUriAndUriAndNumber> listClassOfInterestCount = new ArrayList<UriAndUriAndUriAndNumber>();
		listClassOfInterestCount = MakeListClassOfInterestCount.makeList(model, nameOfListClassOfInterest);

		// TEST
		ArrayList<UriAndUriAndUri> listOfTest = new ArrayList<UriAndUriAndUri>();
		//listOfTest = MakeTest.make(model);

		
		
		//////////////////////////////////////////////
		// Transfert des listes dans fichers .json  //
		//////////////////////////////////////////////

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listPropertyUsageCount, nameOfListPropertyUsageCount + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listClassUsageCount, nameOfListClassUsageCount + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriFile(listPropertyAndSubproperty, nameOfListPropertyAndSubproperty + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriFile(listOfDatatypes, nameOfListDatatypes + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriFile(listOfLanguages, nameOfListLanguages + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriFile(listLinks, nameOfListLinks + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndStringAndBigNumberFile(listMaxPerProperty, nameOfListMaxPerProperty + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndStringAndBigNumberFile(listPerProperty, nameOfListPerProperty + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonStringFile(listDistinctSubjectVocabularies, nameOfListSvocabulary + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonStringFile(listDistinctPredicatVocabularies, nameOfListPvocabulary + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonStringFile(listDistinctObjectVocabularies, nameOfListOvocabulary + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listPropertyMostUsed, nameOfListPropertyMostUsed + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		try {
			ProfilingUtil.makeJsonUriFile(listOfClassDefined, nameOfListClassDefined + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriFile(listOfClassNotDefined, nameOfListClassNotDefined + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listClassMostUsed, nameOfListClassMostUsed + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriAndUriFile(listClassOfInterest, nameOfListClassOfInterest + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriAndUriAndNumberFile(listClassOfInterestCount, nameOfListClassOfInterestCount + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String nameOfResultsFile = "results";
		try {
			ProfilingUtil.makeJsonResultsFile(results, nameOfResultsFile + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}