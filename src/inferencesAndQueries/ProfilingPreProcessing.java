package inferencesAndQueries;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import profiling.util.*;

public class ProfilingPreProcessing {
	
	// On effectue les pré-traitements
	public static void makeTreatements(Model model) {
		
		Instant start0 = Instant.now();

		ProfilingResultsObject results = new ProfilingResultsObject();

		////////////////////////////////////
		//// Pour le dataset en général ////
		////////////////////////////////////
		
		// Nombre de triplets du jeux de données
       	Integer numberOfTriples = GiveNumberOfTriples.giveNumber(model);
		results.setNumberOfTriples(numberOfTriples);

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
		
		// SubclassUsage
		Integer numberOfSubclassUsage = GiveSubclassUsage.giveUsage(model);
		results.setSubclassUsage(numberOfSubclassUsage);

		// Entities mentioned (à vérifier)
		Integer numberEntitiesMentioned = GiveEntitiesMentioned.giveNumber(model);
		results.setEntitiesMentioned(numberEntitiesMentioned);

		// Distinct Entities (à vérifier)
		Integer numberDistinctEntities = GiveDistinctEntities.giveNumber(model);
		results.setDistinctEntities(numberDistinctEntities);

		// Literals (à vérifier DISTINCT ?)
		Integer numberOfLiterals = GiveLiterals.giveNumber(model);
		results.setNumberOfLiterals(numberOfLiterals);

		// Blanks as subject (à vérifier)
		Integer numberOfBlanksAsSubj = GiveBlanksAsSubj.giveNumber(model);
		results.setNumberBlanksAsSubj(numberOfBlanksAsSubj);

		// Blanks as object (à vérifier)
		Integer numberOfBlanksAsObj = GiveBlanksAsObj.giveNumber(model);
		results.setNumberBlanksAsObj(numberOfBlanksAsObj);

		// Typed Subjects
		Integer numberOfTypedSubjects = GiveTypedSubjects.giveNumber(model);
		results.setNumberTypedSubjects(numberOfTypedSubjects);

		// Labeled Subjects
		Integer numberOfLabeledSubjects = GiveLabeledSubjects.giveNumber(model);
		results.setNumberLabeledSubjects(numberOfLabeledSubjects);

		// Same As
		Integer numberOfSameAs = GiveSameAs.giveNumber(model);
		results.setNumberSameAs(numberOfSameAs);

		// Liste des propriétés et de leur usage.
		String nameOfListPropertyUsageCount = "listPropertyUsageCount";
		ArrayList<UriAndNumber> listPropertyUsageCount = new ArrayList<UriAndNumber>();
		listPropertyUsageCount = MakeListPropertyUsageCount.makeList(model);
		
		// Liste of datatypes.
		String nameOfListDatatypes = "listOfDatatypes";
		ArrayList<Uri> listOfDatatypes = new ArrayList<Uri>();
		listOfDatatypes = MakeListDatatypes.makeList(model, listPropertyUsageCount);
		
		// Liste of languages predicat.
		String nameOfListLanguagesPredicat = "listOfLanguagesPredicat";
		ArrayList<Uri> listOfLanguagesPredicat = new ArrayList<Uri>();
		listOfLanguagesPredicat = MakeListLanguagesPredicat.makeList(model, listPropertyUsageCount);

		// Liste of languages predicat values.
		String nameOfListLanguagesPredicatValue = "listOfLanguagesPredicatValue";
		ArrayList<Uri> listOfLanguagesPredicatValue = new ArrayList<Uri>();
		listOfLanguagesPredicatValue = MakeListLanguagesPredicatValue.makeList(model, listPropertyUsageCount);

		// Typed String Length
		double numberOfTypedStringLength = GiveTypedStringLength.giveLength(model, listPropertyUsageCount);
		results.setNumberTypedStringLength(numberOfTypedStringLength);

		// Untyped String Length
		double numberOfUntypedStringLength = GiveUntypedStringLength.giveLength(model, listPropertyUsageCount);
		results.setNumberUntypedStringLength(numberOfUntypedStringLength);

		// List of links between domain names.
		String nameOfListLinks = "listLinks";
		ArrayList<UriAndUri> listLinks = new ArrayList<UriAndUri>();
		listLinks = MakeListLinks.makeList(model, listPropertyUsageCount);

		// Lists of vocabularies.
		String nameOfListSvocabulary = "listSubjectVocabulary"; 
		String nameOfListPvocabulary = "listPredicatVocabulary"; 
		String nameOfListOvocabulary = "listObjectVocabulary"; 
		ArrayList<List<String>> listResourcesSPO = new ArrayList<List<String>>(3);
		listResourcesSPO = MakeListSPOvocabularies.makeList(model, listPropertyUsageCount);
		List<String> listDistinctSubjectVocabularies = new ArrayList<>();
		List<String> listDistinctPredicatVocabularies = new ArrayList<>();
		List<String> listDistinctObjectVocabularies = new ArrayList<>();
		listDistinctSubjectVocabularies = listResourcesSPO.get(0);
		listDistinctPredicatVocabularies = listResourcesSPO.get(1);
		listDistinctObjectVocabularies = listResourcesSPO.get(2);

		// Lists of shared part vocabularies for subjects.
		String nameOfListSharedPartSubjectVocabulary = "listSharedPartSubjectVocabulary"; 
		List<String> listSharedPartSubjectVocabulary = new ArrayList<String>();
		listSharedPartSubjectVocabulary = MakeListSharedPartVocabulary.makeList(model, listDistinctSubjectVocabularies);
		
		// Lists of shared part vocabularies for objects.
		String nameOfListSharedPartObjectVocabulary = "listSharedPartObjectVocabulary"; 
		List<String> listSharedPartObjectVocabulary = new ArrayList<String>();
		listSharedPartObjectVocabulary = MakeListSharedPartVocabulary.makeList(model, listDistinctObjectVocabularies);

		/////////////////////////////
		//// Pour les propriétés ////
		/////////////////////////////
		
		// List of Max per Property.
		String nameOfListMaxPerProperty = "listMaxPerProperty"; 
		ArrayList<UriAndUriAndValue> listMaxPerProperty = new ArrayList<UriAndUriAndValue>();
		listMaxPerProperty = MakeListMaxPerProperty.makeList(model);

		// List of Per Property.
		String nameOfListPerProperty = "listPerProperty"; 
		ArrayList<UriAndUriAndValue> listPerProperty = new ArrayList<UriAndUriAndValue>();
		listPerProperty = MakeListPerProperty.makeList(model);   

		// Liste des propriétés et des sous-propriétés.
		String nameOfListPropertyAndSubproperty = "listPropertyAndSubproperty";
		ArrayList<UriAndUri> listPropertyAndSubproperty = new ArrayList<UriAndUri>();
		listPropertyAndSubproperty = MakeListPropertyAndSubproperty.makeList(model, nameOfListPropertyAndSubproperty);

		// Properties hierarchy deep and loop
		HierarchyDeepAndLoop hierarchyDeepAndLoop = new HierarchyDeepAndLoop();
		hierarchyDeepAndLoop = GivePropertyHierarchyDeep.giveHierarchyDeepAndLoop(model, listPropertyAndSubproperty);
		results.setPropertyHierarchyDeep(hierarchyDeepAndLoop.getHierarchyDeep());
		results.setPropertyHierarchyLoop(hierarchyDeepAndLoop.getLoop());
		
		// Liste des propriétés les plus utilisées.
		String nameOfListMostUsedProperty = "listMostUsedProperty";
		ArrayList<UriAndNumber> listMostUsedProperty = new ArrayList<UriAndNumber>();
		listMostUsedProperty = MakeListMostUsedProperty.makeList(model, listPropertyUsageCount);

		// Liste des types des propriétés.
		String nameOfListMostUsedPropertyType = "listMostUsedPropertyType";
		ArrayList<UriAndUri> listMostUsedPropertyType = new ArrayList<UriAndUri>();
		listMostUsedPropertyType = MakeListMostUsedPropertyType.makeList(model, listMostUsedProperty);

		// Liste des propriétés les plus utilisées avec datatypes et classes range.
		String nameOfListMostUsedPropertyWithDatatypeAndClassRange = "listMostUsedPropertyWithDatatypeAndClassRange";
		ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listMostUsedPropertyWithDatatypeAndClassRange = new ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber>();
		listMostUsedPropertyWithDatatypeAndClassRange = MakeListMostUsedPropertyWithDatatypeAndClassRange.makeList(model, listMostUsedProperty);

		///////////

		// Liste des classes et de leur usage.
		String nameOfListClassUsageCount = "listClassUsageCount";
		ArrayList<UriAndNumber> listClassUsageCount = new ArrayList<UriAndNumber>();
		listClassUsageCount = MakeListClassUsageCount.makeList(model);

		// Construction d'un vecteur pour l'usage des classes.
		String vectorUsageClass = "c(0.0)";
		vectorUsageClass = MakeVectorWithNumber.makeVector(model, listClassUsageCount);
		//System.out.println("Vector Usages Classes : " + vectorUsageClass);

		// Calcul quantile pour l'usage des classes.
		double classUsageQuantile75 = 0.0;
		classUsageQuantile75 = GiveRQuantile.giveDouble(vectorUsageClass, "c(0.75)");
		//System.out.println("Class Usage Quantile 75 : " + classUsageQuantile75);

		// Liste des classes les plus utilisées (quartile 75).
		String nameOfListClassMostUsed = "listClassMostUsed";
		ArrayList<UriAndNumber> listClassMostUsed = new ArrayList<UriAndNumber>();
		listClassMostUsed = MakeListClassMostUsed.makeList(listClassUsageCount, classUsageQuantile75);

		///////////
				
		// Liste des propriétés les plus utilisées avec classes domain.
		String nameOfListMostUsedPropertyWithClassDomain = "listMostUsedPropertyWithClassDomain";
		ArrayList<UriAndUriListAndNumberListAndNumber> listMostUsedPropertyWithClassDomain = new ArrayList<UriAndUriListAndNumberListAndNumber>();
		listMostUsedPropertyWithClassDomain = MakeListMostUsedPropertyWithClassDomain.makeList(model, listMostUsedProperty, listClassMostUsed);

		// Liste des propriétés object les plus utilisées.
		String nameOfListMostUsedObjectProperty = "listMostUsedObjectProperty";
		ArrayList<UriAndNumber> listMostUsedObjectProperty = new ArrayList<UriAndNumber>();
		listMostUsedObjectProperty = MakeListMostUsedObjectProperty.makeList(model, listMostUsedPropertyWithDatatypeAndClassRange, listMostUsedPropertyType);

		// Liste des propriétés de type "datatype properties" les plus utilisées.
		String nameOflistMostUsedDatatypeProperty = "listMostUsedDatatypeProperty";
		ArrayList<UriAndNumber> listMostUsedDatatypeProperty = new ArrayList<UriAndNumber>();
		listMostUsedDatatypeProperty = MakeListMostUsedDatatypeProperty.makeList(model, listMostUsedPropertyWithDatatypeAndClassRange, listMostUsedPropertyType);

		// Liste des propriétés de type "annotation properties" les plus utilisées.
		String nameOflistMostUsedAnnotationProperty = "listMostUsedAnnotationProperty";
		ArrayList<UriAndNumber> listMostUsedAnnotationProperty = new ArrayList<UriAndNumber>();
		listMostUsedAnnotationProperty = MakeListMostUsedAnnotationProperty.makeList(model, listMostUsedPropertyWithDatatypeAndClassRange, listMostUsedPropertyType);

		// Liste des usages de propriétés par suject les plus utilisés.
		String nameOfListMostUsedPropertyUsagePerSubject = "listMostUsedPropertyUsagePerSubject";
		ArrayList<UriAndNumberAndNumberAndNumber> listMostUsedPropertyUsagePerSubject = new ArrayList<UriAndNumberAndNumberAndNumber>();
		listMostUsedPropertyUsagePerSubject = MakeListMostUsedPropertyUsagePerSubject.makeList(model, listMostUsedProperty);

		// Liste des usages de propriétés par object les plus utilisés.
		String nameOfListMostUsedPropertyUsagePerObject = "listMostUsedPropertyUsagePerObject";
		ArrayList<UriAndNumberAndNumberAndNumber> listMostUsedPropertyUsagePerObject = new ArrayList<UriAndNumberAndNumberAndNumber>();
		listMostUsedPropertyUsagePerObject = MakeListMostUsedPropertyUsagePerObject.makeList(model, listMostUsedProperty);

		// Liste of datatypes des propriétés les plus utilisées .
		String nameOfListDatatypesMostUsed = "listMostUsedPropertyDatatypes";
		ArrayList<Uri> listMostUsedPropertyDatatypes = new ArrayList<Uri>();
		listMostUsedPropertyDatatypes = MakeListMostUsedPropertyDatatypes.makeList(model, listMostUsedProperty);


		//////////////////////////
		//// Pour les classes ////
		//////////////////////////
		
		// Liste of languages class.
		String nameOfListLanguagesClass = "listClassLanguages";
		ArrayList<Uri> listClassLanguages = new ArrayList<Uri>();
		listClassLanguages = MakeListClassLanguages.makeList(model, listClassUsageCount);

		// Liste of classes defined.
		String nameOfListClassDefined = "listClassDefined";
		ArrayList<Uri> listOfClassDefined = new ArrayList<Uri>();
		listOfClassDefined = MakeListClassDefined.makeList(model);

		// Liste of classes not defined.
		String nameOfListClassNotDefined = "listClassNotDefined";
		ArrayList<Uri> listOfClassNotDefined = new ArrayList<Uri>();
		listOfClassNotDefined = MakeListClassNotDefined.makeList(listClassUsageCount, listOfClassDefined);

		// Liste des classes et des sous-classes.
		String nameOfListClassAndSubclass = "listClassAndSubclass";
		ArrayList<UriAndUri> listClassAndSubclass = new ArrayList<UriAndUri>();
		listClassAndSubclass = MakeListClassAndSubclass.makeList(model);
		
		// Classes hierarchy deep and loop
		HierarchyDeepAndLoop ClassHierarchyDeepAndLoop = new HierarchyDeepAndLoop();
		ClassHierarchyDeepAndLoop = GiveClassHierarchyDeep.giveHierarchyDeepAndLoop(listClassAndSubclass);
		results.setClassHierarchyDeep(ClassHierarchyDeepAndLoop.getHierarchyDeep());
		results.setClassHierarchyLoop(ClassHierarchyDeepAndLoop.getLoop());
		
		//////////////////////////////////////////////////////////
		//// Pour la détermination d'un modèle de description ////
		//////////////////////////////////////////////////////////
		
		// Liste des classes des sujets ayant pour caractéristiques des combinaisons de propriétés semblables.
		// Les propriétés <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>, <http://www.w3.org/1999/02/22-rdf-syntax-ns#first>
		//  et <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> sont exclus.
		// Si aucune classes n'a été trouvée pour une combinaison donnée, un enregistrement est généré avec une liste de 
		//  classes vide.
		String nameOfListCombinationPropertiesPerSubject = "listCombinationPropertiesPerSubject";
		ArrayList<UriListAndUriListAndNumberListAndNumber> listCombinationPropertiesPerSubject = new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		listCombinationPropertiesPerSubject = MakeListCombinationPropertiesPerSubject.makeList(model);
		
		// Création de nouvelles classes pour les combinaisons de propriétés sans classes (Avec typage des instances concernées).
		
		// On nettoie et réduit au préalable la liste
		String nameOfListCombinationPropertiesPerSubjectCleanedAndReduced = "listCombinationPropertiesPerSubjectCleanedAndReduced";
		ArrayList<UriListAndUriListAndNumberListAndNumber> listCombinationPropertiesPerSubjectCleanedAndReduced = new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		listCombinationPropertiesPerSubjectCleanedAndReduced = MakeListCombinationPropertiesPerSubjectCleanedAndReduced.makeList(listCombinationPropertiesPerSubject);
		
		// Création de nouvelles classes et marquage d'instances pour les combinaisons de propriétés.
		String nameOfListCombinationPropertiesWithNewClasses = "listCombinationPropertiesWithNewClass";
		ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClass = new ArrayList<UriListAndUriList>();
		listCombinationPropertiesWithNewClass = MakeListCombinationPropertiesWithNewClass.makeClasses(model,  listCombinationPropertiesPerSubjectCleanedAndReduced);

		// Liste des relations entre les classes pour les combinaisons de propriétés.
		String nameOflistCombinationPropertiesClassRelationships = "listCombinationPropertiesClassRelationships";
		ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships = new ArrayList<UriListAndUriAndUriListAndNumber>();
		listCombinationPropertiesClassRelationships = MakelistCombinationPropertiesClassRelationships.makeList(model,  listCombinationPropertiesWithNewClass);

		// Liste des classes les plus importantes pour les combinaisons de propriétés.
		String nameOfListCombinationPropertiesClassRelationshipsClasses = "listCombinationPropertiesClassRelationshipsClasses";
		ArrayList<String> listCombinationPropertiesClassRelationshipsClasses = new ArrayList<String>();
		listCombinationPropertiesClassRelationshipsClasses = MakeListCombinationPropertiesClassRelationshipsClasses.makeList(listCombinationPropertiesClassRelationships);

		// Liste des relation entre classes les plus importantes pour les combinaisons de propriétés.
		String nameOfListCombinationPropertiesClassRelationshipsRelationships = "listCombinationPropertiesClassRelationshipsRelationships";
		ArrayList<UriListAndUriAndUriList> listCombinationPropertiesClassRelationshipsRelationships = new ArrayList<UriListAndUriAndUriList>();
		listCombinationPropertiesClassRelationshipsRelationships = MakeListCombinationPropertiesClassRelationshipsRelationships.makeList(listCombinationPropertiesClassRelationships);
		
		// Liste des propriétés des classes les plus importantes pour les combinaisons de propriétés.
		String nameOfListCombinationPropertiesClassRelationshipsPropertiesOfClasses = "listCombinationPropertiesClassRelationshipsPropertiesOfClasses";
		ArrayList<UriListAndUriList> listCombinationPropertiesClassRelationshipsPropertiesOfClasses = new ArrayList<UriListAndUriList>();
		listCombinationPropertiesClassRelationshipsPropertiesOfClasses = MakeListCombinationPropertiesClassRelationshipsPropertiesOfClasses.makeList(listCombinationPropertiesClassRelationships, listCombinationPropertiesWithNewClass, listMostUsedPropertyWithDatatypeAndClassRange);

		OntModel descriptionModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		String nameOfDescriptionModel = "descriptionModel";
		// S'il existe des relations entre les différentes classes de combinaison de propriétés, on basera le modèle de description sur les classes et propriétés 
		//  concernèes par ces relations entre classes.
		if (listCombinationPropertiesClassRelationships.size()!=0) {

			// Création d'un model de description.
			descriptionModel = MakeDescriptionModel.makeModel(listCombinationPropertiesClassRelationships, listCombinationPropertiesClassRelationshipsPropertiesOfClasses,listCombinationPropertiesWithNewClass, listMostUsedPropertyWithClassDomain, listMostUsedPropertyWithDatatypeAndClassRange, listMostUsedObjectProperty, listMostUsedDatatypeProperty, listMostUsedAnnotationProperty );
			
	    }	else {
		// S'il n'existe pas des relations entre les différentes classes de combinaison de propriétés, on basera le modèle de description sur les classes et propriétés 
		//  les plus représentée du modèle	
		
			// Création d'un model de description.
			descriptionModel = MakeDescriptionModelWithoutClassRelationships.makeModel(listCombinationPropertiesWithNewClass, listClassMostUsed, listMostUsedPropertyWithClassDomain, listMostUsedPropertyWithDatatypeAndClassRange, listMostUsedObjectProperty, listMostUsedDatatypeProperty, listMostUsedAnnotationProperty);
	
		}

		// TEST
		//ArrayList<UriAndUriAndUri> listOfTest = new ArrayList<UriAndUriAndUri>();
		//listOfTest = MakeTest.make(model);

		Instant end0 = Instant.now();
		results.setRunningTimeInSecond(Duration.between(start0, end0).getSeconds());

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
			ProfilingUtil.makeJsonUriFile(listOfLanguagesPredicat, nameOfListLanguagesPredicat + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}


		try {
			ProfilingUtil.makeJsonUriFile(listOfLanguagesPredicatValue, nameOfListLanguagesPredicatValue + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriFile(listClassLanguages, nameOfListLanguagesClass + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriFile(listLinks, nameOfListLinks + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriAndValueFile(listMaxPerProperty, nameOfListMaxPerProperty + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriAndValueFile(listPerProperty, nameOfListPerProperty + ".json");
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
			ProfilingUtil.makeJsonStringFile(listSharedPartSubjectVocabulary, nameOfListSharedPartSubjectVocabulary + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonStringFile(listSharedPartObjectVocabulary, nameOfListSharedPartObjectVocabulary + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listMostUsedProperty, nameOfListMostUsedProperty + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriFile(listMostUsedPropertyType, nameOfListMostUsedPropertyType + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		try {
			ProfilingUtil.makeJsonUriAndUriListAndNumberListAndUriListAndNumberListAndNumberFile(listMostUsedPropertyWithDatatypeAndClassRange, nameOfListMostUsedPropertyWithDatatypeAndClassRange + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriListAndNumberListAndNumberFile(listMostUsedPropertyWithClassDomain, nameOfListMostUsedPropertyWithClassDomain + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listMostUsedObjectProperty, nameOfListMostUsedObjectProperty + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listMostUsedDatatypeProperty, nameOflistMostUsedDatatypeProperty + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listMostUsedAnnotationProperty, nameOflistMostUsedAnnotationProperty + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberAndNumberAndNumberFile(listMostUsedPropertyUsagePerSubject, nameOfListMostUsedPropertyUsagePerSubject + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberAndNumberAndNumberFile(listMostUsedPropertyUsagePerObject, nameOfListMostUsedPropertyUsagePerObject + ".json");
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
			ProfilingUtil.makeJsonUriAndUriFile(listClassAndSubclass, nameOfListClassAndSubclass + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listClassMostUsed, nameOfListClassMostUsed + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriListAndUriListAndNumberListAndNumberFile(listCombinationPropertiesPerSubjectCleanedAndReduced, nameOfListCombinationPropertiesPerSubjectCleanedAndReduced + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriListAndUriListAndNumberListAndNumberFile(listCombinationPropertiesPerSubject, nameOfListCombinationPropertiesPerSubject + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ProfilingUtil.makeJsonUriFile(listMostUsedPropertyDatatypes, nameOfListDatatypesMostUsed + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriListAndUriList2File(listCombinationPropertiesWithNewClass, nameOfListCombinationPropertiesWithNewClasses + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriListAndUriAndUriListAndNumberFile(listCombinationPropertiesClassRelationships, nameOflistCombinationPropertiesClassRelationships + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
		// Convertion du modèle de description en fichier JSON
		System.out.println("Convert description model to file.....");
        // Récupération du chemin du fichier.
		Path pathOut = Paths.get(ProfilingConf.folderForTmp, nameOfDescriptionModel + ".owl");				
		// Sortie fichier 
		FileOutputStream outStream = new FileOutputStream(pathOut.toString());
		// exporte le resultat dans un fichier
		descriptionModel.write(outStream, "RDF/XML");
		outStream.close();   
		descriptionModel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}				            	

		try {
			ProfilingUtil.makeJsonStringFile(listCombinationPropertiesClassRelationshipsClasses, nameOfListCombinationPropertiesClassRelationshipsClasses + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriListAndUriAndUriListFile(listCombinationPropertiesClassRelationshipsRelationships, nameOfListCombinationPropertiesClassRelationshipsRelationships + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriListAndUriListFile(listCombinationPropertiesClassRelationshipsPropertiesOfClasses, nameOfListCombinationPropertiesClassRelationshipsPropertiesOfClasses + ".json");
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