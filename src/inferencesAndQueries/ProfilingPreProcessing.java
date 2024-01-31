package inferencesAndQueries;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		listOfDatatypes = MakeListDatatypes.makeList(model, listPropertyUsageCount, nameOfListDatatypes);
		
		// Liste of languages predicat.
		String nameOfListLanguagesPredicat = "listOfLanguagesPredicat";
		ArrayList<Uri> listOfLanguagesPredicat = new ArrayList<Uri>();
		listOfLanguagesPredicat = MakeListLanguagesPredicat.makeList(model, listPropertyUsageCount, nameOfListLanguagesPredicat);

		// Liste of languages predicat values.
		String nameOfListLanguagesPredicatValue = "listOfLanguagesPredicatValue";
		ArrayList<Uri> listOfLanguagesPredicatValue = new ArrayList<Uri>();
		listOfLanguagesPredicatValue = MakeListLanguagesPredicatValue.makeList(model, listPropertyUsageCount, nameOfListLanguagesPredicatValue);

		// Typed String Length
		double numberOfTypedStringLength = GiveTypedStringLength.giveLength(model, listPropertyUsageCount);
		results.setNumberTypedStringLength(numberOfTypedStringLength);

		// Untyped String Length
		double numberOfUntypedStringLength = GiveUntypedStringLength.giveLength(model, listPropertyUsageCount);
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
		listResourcesSPO = MakeListSPOvocabularies.makeList(model, listPropertyUsageCount, nameOfListSvocabulary, nameOfListPvocabulary, nameOfListOvocabulary);
		List<String> listDistinctSubjectVocabularies = new ArrayList<>();
		List<String> listDistinctPredicatVocabularies = new ArrayList<>();
		List<String> listDistinctObjectVocabularies = new ArrayList<>();
		listDistinctSubjectVocabularies = listResourcesSPO.get(0);
		listDistinctPredicatVocabularies = listResourcesSPO.get(1);
		listDistinctObjectVocabularies = listResourcesSPO.get(2);
        
		// Lists of vocabularies.
		String nameOfListSharedPartSubjectVocabulary = "listSharedPartSubjectVocabulary"; 
		List<String> listSharedPartSubjectVocabulary = new ArrayList<String>();
		listSharedPartSubjectVocabulary = MakeListSharedPartSubjectVocabulary.makeList(model, listDistinctSubjectVocabularies, nameOfListSharedPartSubjectVocabulary);
		
		// Liste des 100 premiéres propriétés les plus utilisées.
		String nameOfListPropertyMostUsed = "listPropertyMostUsed";
		ArrayList<UriAndNumber> listPropertyMostUsed = new ArrayList<UriAndNumber>();
		listPropertyMostUsed = MakeListPropertyMostUsed.makeList(model, nameOfListPropertyMostUsed);

		// Liste des 100 premiéres propriétés les plus utilisées avec datatypes et classes range.
		String nameOfListPropertyMostUsedWithDatatypeAndClassRange = "listPropertyMostUsedWithDatatypeAndClassRange";
		ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber> listPropertyMostUsedWithDatatypeAndClassRange = new ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber>();
		listPropertyMostUsedWithDatatypeAndClassRange = MakeListPropertyMostUsedWithDatatypeAndClassRange.makeList(model, nameOfListPropertyMostUsedWithDatatypeAndClassRange);

		// Liste des 100 premiéres propriétés les plus utilisées.
		String nameOfListPropertyUsagePerSubject = "listPropertyUsagePerSubject";
		ArrayList<UriAndNumberAndNumber> listPropertyUsagePerSubject = new ArrayList<UriAndNumberAndNumber>();
		listPropertyUsagePerSubject = MakeListPropertyUsagePerSubject.makeList(model, nameOfListPropertyMostUsed, nameOfListPropertyUsagePerSubject);

		// Liste des 100 premiéres propriétés les plus utilisées.
		String nameOfListPropertyUsagePerObject = "listPropertyUsagePerObject";
		ArrayList<UriAndNumberAndNumber> listPropertyUsagePerObject = new ArrayList<UriAndNumberAndNumber>();
		listPropertyUsagePerObject = MakeListPropertyUsagePerObject.makeList(model, nameOfListPropertyMostUsed, nameOfListPropertyUsagePerObject);

		//// For the Classes ////

		// Liste des classes et de leur usage.
		String nameOfListClassUsageCount = "listClassUsageCount";
		ArrayList<UriAndNumber> listClassUsageCount = new ArrayList<UriAndNumber>();
		listClassUsageCount = MakeListClassUsageCount.makeList(model, nameOfListClassUsageCount);
		
		// Liste of languages class.
		String nameOfListLanguagesClass = "listOfLanguagesClass";
		ArrayList<Uri> listOfLanguagesClass = new ArrayList<Uri>();
		listOfLanguagesClass = MakeListLanguagesClass.makeList(model, listClassUsageCount, nameOfListLanguagesClass);

		// Liste of classes defined.
		String nameOfListClassDefined = "listClassDefined";
		ArrayList<Uri> listOfClassDefined = new ArrayList<Uri>();
		listOfClassDefined = MakeListClassDefined.makeList(model, nameOfListClassDefined);

		// Liste of classes not defined.
		String nameOfListClassNotDefined = "listClassNotDefined";
		ArrayList<Uri> listOfClassNotDefined = new ArrayList<Uri>();
		listOfClassNotDefined = MakeListClassNotDefined.makeList(model, nameOfListClassNotDefined);

		// Liste des classes et des sous-classes.
		String nameOfListClassAndSubclass = "listClassAndSubclass";
		ArrayList<UriAndUri> listClassAndSubclass = new ArrayList<UriAndUri>();
		listClassAndSubclass = MakeListClassAndSubclass.makeList(model, nameOfListClassAndSubclass);

		// Classes hierarchy deep and loop
		HierarchyDeepAndLoop ClassHierarchyDeepAndLoop = new HierarchyDeepAndLoop();
		ClassHierarchyDeepAndLoop = GiveClassHierarchyDeep.giveHierarchyDeepAndLoop(model, nameOfListClassAndSubclass);
		results.setClassHierarchyDeep(ClassHierarchyDeepAndLoop.getHierarchyDeep());
		results.setClassHierarchyLoop(ClassHierarchyDeepAndLoop.getLoop());

		// Construction d'un vecteur pour l'usage des classes.
		String vectorUsageClass = "c(0.0)";
		vectorUsageClass = MakeVectorWithNumber.makeVector(model, nameOfListClassUsageCount);
		//System.out.println("Vector Usages Classes : " + vectorUsageClass);

		// Calcul quantile pour l'usage des classes.
		double classUsageQuantile75 = 0.0;
		classUsageQuantile75 = GiveRQuantile.giveDouble(vectorUsageClass, "c(0.75)");
		//System.out.println("Class Usage Quantile 75 : " + classUsageQuantile75);

		// Liste des classes les plus utilisées (quartile 75).
		String nameOfListClassMostUsed = "listClassMostUsed";
		ArrayList<UriAndNumber> listClassMostUsed = new ArrayList<UriAndNumber>();
		listClassMostUsed = MakeListClassMostUsed.makeList(model, nameOfListClassMostUsed, classUsageQuantile75);

		//MakeTestInsert.make(model);
		
		// Liste des classes sujet des combinaisons de propriétés les plus utilisées par sujet.
		String nameOfListClassSubjectByPropertyCombination = "listClassSubjectByPropertyCombination";
		ArrayList<UriListAndUriAndNumberListAndNumber> listClassSubjectByPropertyCombination = new ArrayList<UriListAndUriAndNumberListAndNumber>();
		listClassSubjectByPropertyCombination = MakeListClassSubjectByPropertiesCombinations.makeList(model, nameOfListPropertyUsagePerObject);

		System.out.println("Début traitement listClassAndPropertyOfInterestCount ");
		// Liste des classes les plus utilisées et leur connections avec d'autres classes.
		String nameOfListClassAndPropertyOfInterestCount = "listClassAndPropertyOfInterestCount";
		ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestCount = new ArrayList<UriAndUriAndUriAndNumber>();
		listClassAndPropertyOfInterestCount = MakeListClassAndPropertyOfInterestCount.makeList(model, nameOfListClassAndPropertyOfInterestCount);

		// Liste des classes et propriétés les plus utilisées (100).
		String nameOfListClassAndPropertyOfInterestMostUsed = "listClassAndPropertyOfInterestMostUsed";
		ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestMostUsed = new ArrayList<UriAndUriAndUriAndNumber>();
		listClassAndPropertyOfInterestMostUsed = MakeListClassAndPropertyOfInterestMostUsed.makeList(model, listClassAndPropertyOfInterestCount, nameOfListClassAndPropertyOfInterestMostUsed);

		// Liste des classes d'intérêts pour les sujets.
		String nameOflistSubjectClassOfInterest = "listSubjectClassOfInterest";
		ArrayList<UriAndNumber> listSubjectClassOfInterest = new ArrayList<UriAndNumber>();
		listSubjectClassOfInterest = MakeListSubjectClassOfInterest.makeList(model, nameOfListClassAndPropertyOfInterestMostUsed, nameOflistSubjectClassOfInterest);

		// Liste des classes d'intérêts pour les sujets.
		String nameOflistObjectClassOfInterest = "listObjectClassOfInterest";
		ArrayList<UriAndNumber> listObjectClassOfInterest = new ArrayList<UriAndNumber>();
		listObjectClassOfInterest = MakeListObjectClassOfInterest.makeList(model, nameOfListClassAndPropertyOfInterestMostUsed, nameOflistObjectClassOfInterest);

		// Liste des propriétés d'intérêts.
		String nameOfListPropertyOfInterest = "listPropertyOfInterest";
		ArrayList<UriAndNumber> listPropertyOfInterest = new ArrayList<UriAndNumber>();
		listPropertyOfInterest = MakeListPropertyOfInterest.makeList(model, nameOfListClassAndPropertyOfInterestMostUsed, nameOfListPropertyOfInterest);

		// Liste of datatypes des propriétés les plus utilisées .
		String nameOfListDatatypesMostUsed = "listOfDatatypesMostUsed";
		ArrayList<Uri> listOfDatatypesMostUsed = new ArrayList<Uri>();
		listOfDatatypesMostUsed = MakeListDatatypesMostUsed.makeList(model, listPropertyMostUsed, nameOfListDatatypesMostUsed);

		// Création de nouvelles classes pour les combinaisons de propriétés (Avec marquage d'instances).
		// On nettoie et réduit au préalable la liste
		String nameOfCleanedAndReducedListClassSubjectByPropertyCombination = "cleanedAndReducedListClassSubjectByPropertyCombination";
		ArrayList<UriListAndUriAndNumberListAndNumber> cleanedAndReducedListClassSubjectByPropertyCombination = new ArrayList<UriListAndUriAndNumberListAndNumber>();
		cleanedAndReducedListClassSubjectByPropertyCombination = MakeListClassSubjectCleanedAndReduced.makeList(listClassSubjectByPropertyCombination);
		ArrayList<UriListAndUriAndNumberListAndNumber> cleanedAndReducedListClassSubjectByPropertyCombinationTemp = new ArrayList<UriListAndUriAndNumberListAndNumber>();
		cleanedAndReducedListClassSubjectByPropertyCombinationTemp.addAll(cleanedAndReducedListClassSubjectByPropertyCombination);

		String nameOfListNewClassWithPropertiesCombinaison = "listOfNewClassWithPropertiesCombinaison";
		ArrayList<UriAndUriList> listOfNewClassWithPropertiesCombinaison = new ArrayList<UriAndUriList>();
		listOfNewClassWithPropertiesCombinaison = MakeListNewClassesWithPropertiesCombinaisons.makeClasses(model,  cleanedAndReducedListClassSubjectByPropertyCombinationTemp);

		// Liste des relations entre les nouvelles classes.
		String nameOfListRelationshipsBetweenNewClasses = "listOfRelationshipsBetweenNewClasses";
		ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClasses = new ArrayList<UriAndUriAndUriAndNumber>();
		listOfRelationshipsBetweenNewClasses = MakeListNewClassesRelationships.makeList(model,  listOfNewClassWithPropertiesCombinaison);
		
		// Création d'un model de description.
		UriForDescriptionModel resultsForDescriptionModel = new UriForDescriptionModel();
		String nameOfDescriptionModel = "descriptionModel";
		OntModel descriptionModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		String nameOfListOfRelationshipsDomain = "listOfRelationshipsDomain";
		ArrayList<UriListAndUriList> listOfRelationshipsDomain = new ArrayList<UriListAndUriList>();
		String nameOfListOfRelationshipsRange = "listOfRelationshipsRange";
		ArrayList<UriListAndUriList> listOfRelationshipsRange = new ArrayList<UriListAndUriList>();
		resultsForDescriptionModel = MakeDescriptionModel.makeModel(listOfRelationshipsBetweenNewClasses);
		descriptionModel = resultsForDescriptionModel.getDescriptionModel();
		listOfRelationshipsDomain = resultsForDescriptionModel.getListOfRelationshipsDomain();
		listOfRelationshipsRange = resultsForDescriptionModel.getListOfRelationshipsRange();

		// Liste des classes les plus importantes.
		String nameOfListMostImportantClasses = "listMostImportantClasses";
		ArrayList<String> listMostImportantClasses = new ArrayList<String>();
		listMostImportantClasses = MakeListMostImportantClasses.makeList(listOfRelationshipsDomain, listOfRelationshipsRange);

		// Liste des relation entre classes les plus importantes.
		String nameOfListMostImportantRelationshipsBetweenClasses = "listMostImportantRelationshipsBetweenClasses";
		ArrayList<String> listMostImportantRelationshipsBetweenClasses = new ArrayList<String>();
		listMostImportantRelationshipsBetweenClasses = MakeListMostImportantRelationshipsBetweenClasses.makeList(listOfRelationshipsDomain, listOfRelationshipsRange);
		
		// Création d'un model de description.
		String nameOfDescriptionModelWithMultipleProperties = "descriptionModelWithMultipleProperties";
		OntModel descriptionModelWithMultipleProperties = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		descriptionModelWithMultipleProperties = MakeDescriptionModelWithMultipleProperties.makeModel(listOfRelationshipsBetweenNewClasses);


		// TEST
		ArrayList<UriAndUriAndUri> listOfTest = new ArrayList<UriAndUriAndUri>();
		listOfTest = MakeTest.make(model);


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
			ProfilingUtil.makeJsonUriFile(listOfLanguagesClass, nameOfListLanguagesClass + ".json");
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
			ProfilingUtil.makeJsonStringFile(listSharedPartSubjectVocabulary, nameOfListSharedPartSubjectVocabulary + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listPropertyMostUsed, nameOfListPropertyMostUsed + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ProfilingUtil.makeJsonUriAndUriAndNumberListAndUriAndNumberListAndNumberFile(listPropertyMostUsedWithDatatypeAndClassRange, nameOfListPropertyMostUsedWithDatatypeAndClassRange + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberAndNumberFile(listPropertyUsagePerSubject, nameOfListPropertyUsagePerSubject + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberAndNumberFile(listPropertyUsagePerObject, nameOfListPropertyUsagePerObject + ".json");
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
			ProfilingUtil.makeJsonUriListAndUriAndNumberListAndNumberFile(cleanedAndReducedListClassSubjectByPropertyCombination, nameOfCleanedAndReducedListClassSubjectByPropertyCombination + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriListAndUriAndNumberListAndNumberFile(listClassSubjectByPropertyCombination, nameOfListClassSubjectByPropertyCombination + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ProfilingUtil.makeJsonUriAndUriAndUriAndNumberFile(listClassAndPropertyOfInterestCount, nameOfListClassAndPropertyOfInterestCount + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriAndUriAndNumberFile(listClassAndPropertyOfInterestMostUsed, nameOfListClassAndPropertyOfInterestMostUsed + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listSubjectClassOfInterest, nameOflistSubjectClassOfInterest + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listObjectClassOfInterest, nameOflistObjectClassOfInterest + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(listPropertyOfInterest, nameOfListPropertyOfInterest + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriFile(listOfDatatypesMostUsed, nameOfListDatatypesMostUsed + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriListFile(listOfNewClassWithPropertiesCombinaison, nameOfListNewClassWithPropertiesCombinaison + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriAndUriAndNumberFile(listOfRelationshipsBetweenNewClasses, nameOfListRelationshipsBetweenNewClasses + ".json");
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
			ProfilingUtil.makeJsonStringFile(listMostImportantClasses, nameOfListMostImportantClasses + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonStringFile(listMostImportantRelationshipsBetweenClasses, nameOfListMostImportantRelationshipsBetweenClasses + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriListAndUriListFile(listOfRelationshipsDomain, nameOfListOfRelationshipsDomain + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriListAndUriListFile(listOfRelationshipsRange, nameOfListOfRelationshipsRange + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
		// Convertion du modèle de description en fichier JSON
		System.out.println("Convert description model with multiple properties to file.....");
		// Récupération du chemin du fichier.
		Path pathOut = Paths.get(ProfilingConf.folderForTmp, nameOfDescriptionModelWithMultipleProperties + ".owl");				
		// Sortie fichier 
		FileOutputStream outStream = new FileOutputStream(pathOut.toString());
		// exporte le resultat dans un fichier
		descriptionModelWithMultipleProperties.write(outStream, "RDF/XML");
		outStream.close();   
		descriptionModelWithMultipleProperties.close();
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