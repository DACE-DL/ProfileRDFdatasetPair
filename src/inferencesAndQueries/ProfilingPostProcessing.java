package inferencesAndQueries;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import profiling.util.*;

public class ProfilingPostProcessing{
	
	// On effectue les post-traitements
	public static void makeTreatements(String idPair, String pathForSourceResults, String pathForTargetResults) {

		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf(); 

		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();
		ArrayList<Lpt> listLptsTemp = new ArrayList<Lpt>();

		String nameOfResultsFile = "results"; // dans CSV
		String nameOfListPropertyUsagePerSubject = "listPropertyUsagePerSubject";
		String nameOfListPropertyUsagePerObject = "listPropertyUsagePerObject";
		String nameOfListPropertyAndSubproperty = "listPropertyAndSubproperty";
		String nameOfListClassUsageCount = "listClassUsageCount";
		String nameOfListDatatypes = "listOfDatatypes"; // dans CSV
		String nameOfListLanguagesPredicat = "listOfLanguagesPredicat"; // dans CSV
		String nameOfListLanguagesPredicatValue = "listOfLanguagesPredicatValue"; // dans CSV
		String nameOfListLanguagesClass = "listOfLanguagesClass"; // dans CSV
		String nameOfListLinks = "listLinks"; // dans CSV
		String nameOfListMaxPerProperty = "listMaxPerProperty"; 
		String nameOfListPerProperty = "listPerProperty"; 
		String nameOfListSubjectVocabulary = "listSubjectVocabulary"; // dans CSV
		String nameOfListPredicatVocabulary = "listPredicatVocabulary"; // dans CSV
		String nameOfListObjectVocabulary = "listObjectVocabulary"; // dans CSV
		String nameOfListPropertyMostUsed = "listPropertyMostUsed";
		String nameOfListClassDefined = "listClassDefined";
		String nameOfListClassNotDefined = "listClassNotDefined";
		String nameOfListClassMostUsed = "listClassMostUsed";
		String nameOfListClassAndPropertyOfInterestCount = "listClassAndPropertyOfInterestCount";
		//String nameOfListClassAndPropertyOfInterest = "listClassAndPropertyOfInterest";
		String nameOfListSubjectClassOfInterest = "listSubjectClassOfInterest";
		String nameOfListObjectClassOfInterest = "listObjectClassOfInterest";
		//String nameOfListPropertyOfInterest = "listPropertyOfInterest";
		String nameOfListDatatypesOfInterest = "listOfDatatypesMostUsed";
		String nameOfListOfNewClassWithPropertiesCombinaison = "listOfNewClassWithPropertiesCombinaison";
		String nameOfListOfRelationshipsDomain = "listOfRelationshipsDomain";
		String nameOfListOfRelationshipsRange = "listOfRelationshipsRange";
		String nameOfListMostImportantClasses = "listMostImportantClasses";
		String nameOfListMostImportantRelationshipsBetweenClasses = "listMostImportantRelationshipsBetweenClasses";
		String nameOfListMostImportantPropertiesOfClasses = "listMostImportantPropertiesOfClasses";
		String nameOfListClassAndSubclass = "listClassAndSubclass";
		
		
		
		
		
		////////////////////////////////////////////////////////////////////
		// Récupération des résultats et listes générés lors du profilage //
		////////////////////////////////////////////////////////////////////

		ProfilingResultsObject resultsSource = new ProfilingResultsObject();
		Path pathResultsSource = Paths.get(pathForSourceResults, nameOfResultsFile + ".json");
		try {
			resultsSource = ProfilingUtil.makeResultsObject(pathResultsSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProfilingResultsObject resultsTarget = new ProfilingResultsObject();
		Path pathResultsTarget = Paths.get(pathForTargetResults, nameOfResultsFile + ".json");
		try {
			resultsTarget = ProfilingUtil.makeResultsObject(pathResultsTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndNumberAndNumberAndNumber> listPropertyUsagePerObjectSource = new ArrayList<UriAndNumberAndNumberAndNumber>();
		Path pathNameListPropertyUsagePerObjectSource = Paths.get(pathForSourceResults, nameOfListPropertyUsagePerObject + ".json");
	    try {
			listPropertyUsagePerObjectSource = ProfilingUtil.makeArrayListUriAndNumberAndNumberAndNumber(pathNameListPropertyUsagePerObjectSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumberAndNumberAndNumber> listPropertyUsagePerObjectTarget = new ArrayList<UriAndNumberAndNumberAndNumber>();
		Path pathNameListPropertyUsagePerObjectTarget = Paths.get(pathForTargetResults, nameOfListPropertyUsagePerObject + ".json");
	    try {
			listPropertyUsagePerObjectTarget = ProfilingUtil.makeArrayListUriAndNumberAndNumberAndNumber(pathNameListPropertyUsagePerObjectTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriAndNumberAndNumberAndNumber> listPropertyUsagePerSubjectSource = new ArrayList<UriAndNumberAndNumberAndNumber>();
		Path pathNameListPropertyUsagePerSubjectSource = Paths.get(pathForSourceResults, nameOfListPropertyUsagePerSubject + ".json");
	    try {
			listPropertyUsagePerSubjectSource = ProfilingUtil.makeArrayListUriAndNumberAndNumberAndNumber(pathNameListPropertyUsagePerSubjectSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumberAndNumberAndNumber> listPropertyUsagePerSubjectTarget = new ArrayList<UriAndNumberAndNumberAndNumber>();
		Path pathNameListPropertyUsagePerSubjectTarget = Paths.get(pathForTargetResults, nameOfListPropertyUsagePerSubject + ".json");
	    try {
			listPropertyUsagePerSubjectTarget = ProfilingUtil.makeArrayListUriAndNumberAndNumberAndNumber(pathNameListPropertyUsagePerSubjectTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriAndUri> listPropertyAndSubpropertySource = new ArrayList<UriAndUri>();
		Path pathNamelistPropertyAndSubpropertySource = Paths.get(pathForSourceResults, nameOfListPropertyAndSubproperty + ".json");
	    try {
			listPropertyAndSubpropertySource = ProfilingUtil.makeArrayListUriAndUri(pathNamelistPropertyAndSubpropertySource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndUri> listPropertyAndSubpropertyTarget = new ArrayList<UriAndUri>();
		Path pathNamelistPropertyAndSubpropertyTarget = Paths.get(pathForTargetResults, nameOfListPropertyAndSubproperty + ".json");
	    try {
			listPropertyAndSubpropertyTarget = ProfilingUtil.makeArrayListUriAndUri(pathNamelistPropertyAndSubpropertyTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndNumber> listClassUsageCountSource = new ArrayList<UriAndNumber>();
		Path pathNamelistClassUsageCountSource = Paths.get(pathForSourceResults, nameOfListClassUsageCount + ".json");
	    try {
			listClassUsageCountSource = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistClassUsageCountSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumber> listClassUsageCountTarget = new ArrayList<UriAndNumber>();
		Path pathNamelistClassUsageCountTarget = Paths.get(pathForTargetResults, nameOfListClassUsageCount + ".json");
	    try {
			listClassUsageCountTarget = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistClassUsageCountTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Uri> listOfDatatypesSource = new ArrayList<Uri>();
		Path pathNamelistOfDatatypesSource = Paths.get(pathForSourceResults, nameOfListDatatypes + ".json");
	    try {
			listOfDatatypesSource = ProfilingUtil.makeArrayListUri(pathNamelistOfDatatypesSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Uri> listOfDatatypesTarget = new ArrayList<Uri>();
		Path pathNamelistOfDatatypesTarget = Paths.get(pathForTargetResults, nameOfListDatatypes + ".json");
	    try {
			listOfDatatypesTarget = ProfilingUtil.makeArrayListUri(pathNamelistOfDatatypesTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Uri> listOfLanguagesPredicatSource = new ArrayList<Uri>();
		Path pathNamelistOfLanguagesPredicatSource = Paths.get(pathForSourceResults, nameOfListLanguagesPredicat + ".json");
	    try {
			listOfLanguagesPredicatSource = ProfilingUtil.makeArrayListUri(pathNamelistOfLanguagesPredicatSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Uri> listOfLanguagesPredicatTarget = new ArrayList<Uri>();
		Path pathNamelistOfLanguagesPredicatTarget = Paths.get(pathForTargetResults, nameOfListLanguagesPredicat + ".json");
	    try {
			listOfLanguagesPredicatTarget = ProfilingUtil.makeArrayListUri(pathNamelistOfLanguagesPredicatTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<Uri> listOfLanguagesClassSource = new ArrayList<Uri>();
		Path pathNamelistOfLanguagesClassSource = Paths.get(pathForSourceResults, nameOfListLanguagesClass + ".json");
	    try {
			listOfLanguagesClassSource = ProfilingUtil.makeArrayListUri(pathNamelistOfLanguagesClassSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Uri> listOfLanguagesClassTarget = new ArrayList<Uri>();
		Path pathNamelistOfLanguagesClassTarget = Paths.get(pathForTargetResults, nameOfListLanguagesClass + ".json");
	    try {
			listOfLanguagesClassTarget = ProfilingUtil.makeArrayListUri(pathNamelistOfLanguagesClassTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<Uri> listOfLanguagesPredicatValueSource = new ArrayList<Uri>();
		Path pathNamelistOfLanguagesPredicatValueSource = Paths.get(pathForSourceResults, nameOfListLanguagesPredicatValue + ".json");
	    try {
			listOfLanguagesPredicatValueSource = ProfilingUtil.makeArrayListUri(pathNamelistOfLanguagesPredicatValueSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Uri> listOfLanguagesPredicatValueTarget = new ArrayList<Uri>();
		Path pathNamelistOfLanguagesPredicatValueTarget = Paths.get(pathForTargetResults, nameOfListLanguagesPredicatValue + ".json");
	    try {
			listOfLanguagesPredicatValueTarget = ProfilingUtil.makeArrayListUri(pathNamelistOfLanguagesPredicatValueTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriAndUri> listLinksSource = new ArrayList<UriAndUri>();
		Path pathNamelistLinksSource = Paths.get(pathForSourceResults, nameOfListLinks + ".json");
	    try {
			listLinksSource = ProfilingUtil.makeArrayListUriAndUri(pathNamelistLinksSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndUri> listLinksTarget = new ArrayList<UriAndUri>();
		Path pathNamelistLinksTarget = Paths.get(pathForTargetResults, nameOfListLinks + ".json");
	    try {
			listLinksTarget = ProfilingUtil.makeArrayListUriAndUri(pathNamelistLinksTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndStringAndBigNumber> listMaxPerPropertySource = new ArrayList<UriAndStringAndBigNumber>();
		Path pathNamelistMaxPerPropertySource = Paths.get(pathForSourceResults, nameOfListMaxPerProperty + ".json");
	    try {
			listMaxPerPropertySource = ProfilingUtil.makeArrayListUriAndStringAndBigNumber(pathNamelistMaxPerPropertySource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndStringAndBigNumber> listMaxPerPropertyTarget = new ArrayList<UriAndStringAndBigNumber>();
		Path pathNamelistMaxPerPropertyTarget = Paths.get(pathForTargetResults, nameOfListMaxPerProperty + ".json");
	    try {
			listMaxPerPropertyTarget = ProfilingUtil.makeArrayListUriAndStringAndBigNumber(pathNamelistMaxPerPropertyTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndStringAndBigNumber> listPerPropertySource = new ArrayList<UriAndStringAndBigNumber>();
		Path pathNamelistPerPropertySource = Paths.get(pathForSourceResults, nameOfListPerProperty + ".json");
	    try {
			listPerPropertySource = ProfilingUtil.makeArrayListUriAndStringAndBigNumber(pathNamelistPerPropertySource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndStringAndBigNumber> listPerPropertyTarget = new ArrayList<UriAndStringAndBigNumber>();
		Path pathNamelistPerPropertyTarget = Paths.get(pathForTargetResults, nameOfListPerProperty + ".json");
	    try {
			listPerPropertyTarget = ProfilingUtil.makeArrayListUriAndStringAndBigNumber(pathNamelistPerPropertyTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<String> listSubjectVocabularySource = new ArrayList<String>();
		Path pathNamelistSubjectVocabularySource = Paths.get(pathForSourceResults, nameOfListSubjectVocabulary + ".json");
	    try {
			listSubjectVocabularySource = ProfilingUtil.makeArrayListString(pathNamelistSubjectVocabularySource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> listSubjectVocabularyTarget = new ArrayList<String>();
		Path pathNamelistSubjectVocabularyTarget = Paths.get(pathForTargetResults, nameOfListSubjectVocabulary + ".json");
	    try {
			listSubjectVocabularyTarget = ProfilingUtil.makeArrayListString(pathNamelistSubjectVocabularyTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<String> listPredicatVocabularySource = new ArrayList<String>();
		Path pathNamelistPredicatVocabularySource = Paths.get(pathForSourceResults, nameOfListPredicatVocabulary + ".json");
	    try {
			listPredicatVocabularySource = ProfilingUtil.makeArrayListString(pathNamelistPredicatVocabularySource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> listPredicatVocabularyTarget = new ArrayList<String>();
		Path pathNamelistPredicatVocabularyTarget = Paths.get(pathForTargetResults, nameOfListPredicatVocabulary + ".json");
	    try {
			listPredicatVocabularyTarget = ProfilingUtil.makeArrayListString(pathNamelistPredicatVocabularyTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<String> listObjectVocabularySource = new ArrayList<String>();
		Path pathNamelistObjectVocabularySource = Paths.get(pathForSourceResults, nameOfListObjectVocabulary + ".json");
	    try {
			listObjectVocabularySource = ProfilingUtil.makeArrayListString(pathNamelistObjectVocabularySource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> listObjectVocabularyTarget = new ArrayList<String>();
		Path pathNamelistObjectVocabularyTarget = Paths.get(pathForTargetResults, nameOfListObjectVocabulary + ".json");
	    try {
			listObjectVocabularyTarget = ProfilingUtil.makeArrayListString(pathNamelistObjectVocabularyTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndNumber> listPropertyMostUsedSource = new ArrayList<UriAndNumber>();
		Path pathNamelistPropertyMostUsedSource = Paths.get(pathForSourceResults, nameOfListPropertyMostUsed + ".json");
	    try {
			listPropertyMostUsedSource = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistPropertyMostUsedSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumber> listPropertyMostUsedTarget = new ArrayList<UriAndNumber>();
		Path pathNamelistPropertyMostUsedTarget = Paths.get(pathForTargetResults, nameOfListPropertyMostUsed + ".json");
	    try {
			listPropertyMostUsedTarget = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistPropertyMostUsedTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Uri> listClassDefinedSource = new ArrayList<Uri>();
		Path pathNamelistClassDefinedSource = Paths.get(pathForSourceResults, nameOfListClassDefined + ".json");
	    try {
			listClassDefinedSource = ProfilingUtil.makeArrayListUri(pathNamelistClassDefinedSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Uri> listClassDefinedTarget = new ArrayList<Uri>();
		Path pathNamelistClassDefinedTarget = Paths.get(pathForTargetResults, nameOfListClassDefined + ".json");
	    try {
			listClassDefinedTarget = ProfilingUtil.makeArrayListUri(pathNamelistClassDefinedTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Uri> listClassNotDefinedSource = new ArrayList<Uri>();
		Path pathNamelistClassNotDefinedSource = Paths.get(pathForSourceResults, nameOfListClassNotDefined + ".json");
	    try {
			listClassNotDefinedSource = ProfilingUtil.makeArrayListUri(pathNamelistClassNotDefinedSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Uri> listClassNotDefinedTarget = new ArrayList<Uri>();
		Path pathNamelistClassNotDefinedTarget = Paths.get(pathForTargetResults, nameOfListClassNotDefined + ".json");
	    try {
			listClassNotDefinedTarget = ProfilingUtil.makeArrayListUri(pathNamelistClassNotDefinedTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndNumber> listClassMostUsedSource = new ArrayList<UriAndNumber>();
		Path pathNamelistClassMostUsedSource = Paths.get(pathForSourceResults, nameOfListClassMostUsed + ".json");
	    try {
			listClassMostUsedSource = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistClassMostUsedSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumber> listClassMostUsedTarget = new ArrayList<UriAndNumber>();
		Path pathNamelistClassMostUsedTarget = Paths.get(pathForTargetResults, nameOfListClassMostUsed + ".json");
	    try {
			listClassMostUsedTarget = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistClassMostUsedTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestCountSource = new ArrayList<UriAndUriAndUriAndNumber>();
		Path pathNamelistClassAndPropertyOfInterestCountSource = Paths.get(pathForSourceResults, nameOfListClassAndPropertyOfInterestCount + ".json");
	    try {
			listClassAndPropertyOfInterestCountSource = ProfilingUtil.makeArrayListUriAndUriAndUriAndNumber(pathNamelistClassAndPropertyOfInterestCountSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestCountTarget = new ArrayList<UriAndUriAndUriAndNumber>();
		Path pathNamelistClassAndPropertyOfInterestCountTarget = Paths.get(pathForTargetResults, nameOfListClassAndPropertyOfInterestCount + ".json");
	    try {
			listClassAndPropertyOfInterestCountTarget = ProfilingUtil.makeArrayListUriAndUriAndUriAndNumber(pathNamelistClassAndPropertyOfInterestCountTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndNumber> listSubjectClassOfInterestSource = new ArrayList<UriAndNumber>();
		Path pathNamelistSubjectClassOfInterestSource = Paths.get(pathForSourceResults, nameOfListSubjectClassOfInterest + ".json");
	    try {
			listSubjectClassOfInterestSource = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistSubjectClassOfInterestSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumber> listSubjectClassOfInterestTarget = new ArrayList<UriAndNumber>();
		Path pathNamelistSubjectClassOfInterestTarget = Paths.get(pathForTargetResults, nameOfListSubjectClassOfInterest + ".json");
	    try {
			listSubjectClassOfInterestTarget = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistSubjectClassOfInterestTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndNumber> listObjectClassOfInterestSource = new ArrayList<UriAndNumber>();
		Path pathNamelistObjectClassOfInterestSource = Paths.get(pathForSourceResults, nameOfListObjectClassOfInterest + ".json");
	    try {
			listObjectClassOfInterestSource = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistObjectClassOfInterestSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumber> listObjectClassOfInterestTarget = new ArrayList<UriAndNumber>();
		Path pathNamelistObjectClassOfInterestTarget = Paths.get(pathForTargetResults, nameOfListObjectClassOfInterest + ".json");
	    try {
			listObjectClassOfInterestTarget = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistObjectClassOfInterestTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriAndUri> listClassAndSubclassSource = new ArrayList<UriAndUri>();
		Path pathNameListClassAndSubclassSource = Paths.get(pathForSourceResults, nameOfListClassAndSubclass + ".json");
	    try {
			listClassAndSubclassSource = ProfilingUtil.makeArrayListUriAndUri(pathNameListClassAndSubclassSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndUri> listClassAndSubclassTarget = new ArrayList<UriAndUri>();
		Path pathNameListClassAndSubclassTarget = Paths.get(pathForTargetResults, nameOfListClassAndSubclass + ".json");
	    try {
			listClassAndSubclassTarget = ProfilingUtil.makeArrayListUriAndUri(pathNameListClassAndSubclassTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Uri> listOfDatatypesMostUsedSource = new ArrayList<Uri>();
		Path pathNamelistOfDatatypesMostUsedSource = Paths.get(pathForSourceResults, nameOfListDatatypesOfInterest + ".json");
	    try {
			listOfDatatypesMostUsedSource = ProfilingUtil.makeArrayListUri(pathNamelistOfDatatypesMostUsedSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Uri> listOfDatatypesMostUsedTarget = new ArrayList<Uri>();
		Path pathNamelistOfDatatypesMostUsedTarget = Paths.get(pathForTargetResults, nameOfListDatatypesOfInterest + ".json");
	    try {
			listOfDatatypesMostUsedTarget = ProfilingUtil.makeArrayListUri(pathNamelistOfDatatypesMostUsedTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriAndUriList> listOfNewClassWithPropertiesCombinaisonSource = new ArrayList<UriAndUriList>();
		Path pathNamelistOfNewClassWithPropertiesCombinaisonSource = Paths.get(pathForSourceResults, nameOfListOfNewClassWithPropertiesCombinaison + ".json");
	    try {
			listOfNewClassWithPropertiesCombinaisonSource = ProfilingUtil.makeArrayListUriAndUriList(pathNamelistOfNewClassWithPropertiesCombinaisonSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndUriList> listOfNewClassWithPropertiesCombinaisonTarget = new ArrayList<UriAndUriList>();
		Path pathNamelistOfNewClassWithPropertiesCombinaisonTarget = Paths.get(pathForTargetResults, nameOfListOfNewClassWithPropertiesCombinaison + ".json");
	    try {
			listOfNewClassWithPropertiesCombinaisonTarget = ProfilingUtil.makeArrayListUriAndUriList(pathNamelistOfNewClassWithPropertiesCombinaisonTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClassesSource = new ArrayList<UriAndUriAndUriAndNumber>();
		// Path pathNameListOfRelationshipsBetweenNewClassesSource = Paths.get(pathForTargetResults, nameOfListOfNewClassWithPropertiesCombinaison + ".json");
	    // try {
		// 	listOfRelationshipsBetweenNewClassesSource = ProfilingUtil.makeArrayListUriAndUriAndUriAndNumber(pathNameListOfRelationshipsBetweenNewClassesSource.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		
		// ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClassesTarget = new ArrayList<UriAndUriAndUriAndNumber>();
		// Path pathNameListOfRelationshipsBetweenNewClassesTarget = Paths.get(pathForTargetResults, nameOfListOfRelationshipsBetweenNewClasses + ".json");
	    // try {
		// 	listOfRelationshipsBetweenNewClassesTarget = ProfilingUtil.makeArrayListUriAndUriAndUriAndNumber(pathNameListOfRelationshipsBetweenNewClassesTarget.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }

		ArrayList<UriListAndUriList> listOfRelationshipsDomainSource = new ArrayList<UriListAndUriList>();
		Path pathNameListOfRelationshipsDomainSource = Paths.get(pathForSourceResults, nameOfListOfRelationshipsDomain + ".json");
	    try {
			listOfRelationshipsDomainSource = ProfilingUtil.makeArrayListUriListAndUriList(pathNameListOfRelationshipsDomainSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriListAndUriList> listOfRelationshipsDomainTarget = new ArrayList<UriListAndUriList>();
		Path pathNameListOfRelationshipsDomainTarget = Paths.get(pathForTargetResults, nameOfListOfRelationshipsDomain + ".json");
	    try {
			listOfRelationshipsDomainTarget = ProfilingUtil.makeArrayListUriListAndUriList(pathNameListOfRelationshipsDomainTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriListAndUriList> listOfRelationshipsRangeSource = new ArrayList<UriListAndUriList>();
		Path pathNamelistOfRelationshipsRangeSource = Paths.get(pathForSourceResults, nameOfListOfRelationshipsRange + ".json");
	    try {
			listOfRelationshipsRangeSource = ProfilingUtil.makeArrayListUriListAndUriList(pathNamelistOfRelationshipsRangeSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriListAndUriList> listOfRelationshipsRangeTarget = new ArrayList<UriListAndUriList>();
		Path pathNamelistOfRelationshipsRangeTarget = Paths.get(pathForTargetResults, nameOfListOfRelationshipsRange + ".json");
	    try {
			listOfRelationshipsRangeTarget = ProfilingUtil.makeArrayListUriListAndUriList(pathNamelistOfRelationshipsRangeTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<String> listMostImportantClassesSource = new ArrayList<String>();
		Path pathNameListMostImportantClassesSource = Paths.get(pathForSourceResults, nameOfListMostImportantClasses + ".json");
	    try {
			listMostImportantClassesSource = ProfilingUtil.makeArrayListString2(pathNameListMostImportantClassesSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<String> listMostImportantClassesTarget = new ArrayList<String>();
		Path pathNameListMostImportantClassesTarget = Paths.get(pathForTargetResults, nameOfListMostImportantClasses + ".json");
	    try {
			listMostImportantClassesTarget = ProfilingUtil.makeArrayListString2(pathNameListMostImportantClassesTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriAndUriAndUri> listMostImportantRelationshipsBetweenClassesSource = new ArrayList<UriAndUriAndUri>();
		Path pathNameListMostImportantRelationshipsBetweenClassesSource = Paths.get(pathForSourceResults, nameOfListMostImportantRelationshipsBetweenClasses + ".json");
	    try {
			listMostImportantRelationshipsBetweenClassesSource = ProfilingUtil.makeArrayListUriAndUriAndUri(pathNameListMostImportantRelationshipsBetweenClassesSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndUriAndUri> listMostImportantRelationshipsBetweenClassesTarget = new ArrayList<UriAndUriAndUri>();
		Path pathNameListMostImportantRelationshipsBetweenClassesTarget = Paths.get(pathForTargetResults, nameOfListMostImportantRelationshipsBetweenClasses + ".json");
	    try {
			listMostImportantRelationshipsBetweenClassesTarget = ProfilingUtil.makeArrayListUriAndUriAndUri(pathNameListMostImportantRelationshipsBetweenClassesTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriAndUriAndUriListList> listMostImportantPropertiesOfClassesSource = new ArrayList<UriAndUriAndUriListList>();
		Path pathNameListMostImportantPropertiesOfClassesSource = Paths.get(pathForSourceResults, nameOfListMostImportantPropertiesOfClasses + ".json");
	    try {
			listMostImportantPropertiesOfClassesSource = ProfilingUtil.makeArrayListUriAndUriAndUriListList(pathNameListMostImportantPropertiesOfClassesSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndUriAndUriListList> listMostImportantPropertiesOfClassesTarget = new ArrayList<UriAndUriAndUriListList>();
		Path pathNameListMostImportantPropertiesOfClassesTarget = Paths.get(pathForTargetResults, nameOfListMostImportantPropertiesOfClasses + ".json");
	    try {
			listMostImportantPropertiesOfClassesTarget = ProfilingUtil.makeArrayListUriAndUriAndUriListList(pathNameListMostImportantPropertiesOfClassesTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Source size : " + resultsSource.getNumberOfTriples());
		System.out.println("Target size : " + resultsTarget.getNumberOfTriples());
		//System.out.println("ListPropertyUsageCountSource size : " + listPropertyUsageCountSource.size());	
		
		
		///////////////////////////////////////////////////////////////
		// Recherche de problèmes liés au langages                   //
		///////////////////////////////////////////////////////////////

		listLptsTemp = TraitListLanguagesPredicat.makeList(listOfLanguagesPredicatSource, listOfLanguagesPredicatTarget);
		listLpts.addAll(listLptsTemp);

		listLptsTemp = TraitListLanguagesPredicatValue.makeList(listOfLanguagesPredicatValueSource, listOfLanguagesPredicatValueTarget);
		listLpts.addAll(listLptsTemp);
		
		listLptsTemp = TraitListLanguagesClass.makeList(listOfLanguagesClassSource, listOfLanguagesClassTarget);
		listLpts.addAll(listLptsTemp);
		
		///////////////////////////////////////////////////////////////
		// Recherche de problèmes liés aux datatypes                 //
		///////////////////////////////////////////////////////////////

		listLptsTemp = TraitListDatatypes.makeList(listOfDatatypesMostUsedSource, listOfDatatypesMostUsedTarget);
		listLpts.addAll(listLptsTemp);
		
		
		///////////////////////////////////////////////////////////////
		// Transfert de la liste des LPT extrait dans fichers .json  //
		///////////////////////////////////////////////////////////////

		listLpts.forEach((lpt) -> {
			System.out.println("Lpt : " + lpt.getLpt());
		});

		String nameOfResultsLPTsFile = "resultingLPTs";
		try {
			ProfilingUtil.makeJsonLptFile(listLpts, nameOfResultsLPTsFile + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		///////////////////////////////////////////////////////////////
		// Création fichier CSV pour ML                              //
		///////////////////////////////////////////////////////////////
		
		Object[][] tableauDeuxD = new Object[47][3];
		tableauDeuxD = TraitResultsForML.makeResultsForML(idPair, resultsSource, resultsTarget,
		listOfDatatypesSource, listOfDatatypesTarget,
		listOfLanguagesPredicatSource, listOfLanguagesPredicatTarget,
		listOfLanguagesPredicatValueSource, listOfLanguagesPredicatValueTarget,
		listOfLanguagesClassSource, listOfLanguagesClassTarget,
		listLinksSource, listLinksTarget,
		listSubjectVocabularySource, listSubjectVocabularyTarget,
		listPredicatVocabularySource, listPredicatVocabularyTarget,
		listObjectVocabularySource, listObjectVocabularyTarget,
		listOfNewClassWithPropertiesCombinaisonSource, listOfNewClassWithPropertiesCombinaisonTarget,
		listOfRelationshipsDomainSource, listOfRelationshipsDomainTarget,
		listOfRelationshipsRangeSource, listOfRelationshipsRangeTarget,
		listMostImportantClassesSource, listMostImportantClassesTarget,
		listMostImportantRelationshipsBetweenClassesSource, listMostImportantRelationshipsBetweenClassesTarget,
		listMostImportantPropertiesOfClassesSource, listMostImportantPropertiesOfClassesTarget,
		listPropertyUsagePerObjectSource, listPropertyUsagePerObjectTarget,
		listPropertyUsagePerSubjectSource, listPropertyUsagePerSubjectTarget,
		listClassAndSubclassSource, listClassAndSubclassTarget,
		listPropertyAndSubpropertySource, listPropertyAndSubpropertyTarget
		);
		String nameOfResultsForMLfile = "resultsForMLfile";
		ProfilingUtil.saveTwoDimensionalTableInCSV(tableauDeuxD, nameOfResultsForMLfile + ".csv");
	}
}