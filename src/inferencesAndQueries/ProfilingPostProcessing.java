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
		String nameOfListMostUsedPropertyUsagePerSubject = "listMostUsedPropertyUsagePerSubject";
		String nameOfListMostUsedPropertyUsagePerObject = "listMostUsedPropertyUsagePerObject";
		String nameOfListPropertyAndSubproperty = "listPropertyAndSubproperty";
		String nameOfListClassUsageCount = "listClassUsageCount";
		String nameOfListDatatypes = "listOfDatatypes"; // dans CSV
		String nameOfListLanguagesPredicat = "listOfLanguagesPredicat"; // dans CSV
		String nameOfListLanguagesPredicatValue = "listOfLanguagesPredicatValue"; // dans CSV
		String nameOfListLanguagesClass = "listClassLanguages"; // dans CSV
		String nameOfListLinks = "listLinks"; // dans CSV
		String nameOfListMaxPerProperty = "listMaxPerProperty"; 
		String nameOfListPerProperty = "listPerProperty"; 
		String nameOfListSubjectVocabulary = "listSubjectVocabulary"; // dans CSV
		String nameOfListPredicatVocabulary = "listPredicatVocabulary"; // dans CSV
		String nameOfListObjectVocabulary = "listObjectVocabulary"; // dans CSV
		String nameOfListMostUsedProperty = "listMostUsedProperty";
		String nameOfListClassDefined = "listClassDefined";
		String nameOfListClassNotDefined = "listClassNotDefined";
		String nameOfListClassMostUsed = "listClassMostUsed";
		String nameOfListDatatypesOfInterest = "listMostUsedPropertyDatatypes";
		String nameOfListCombinationPropertiesWithNewClass = "listCombinationPropertiesWithNewClass";
		String nameOfListCombinationPropertiesClassRelationshipsClasses = "listCombinationPropertiesClassRelationshipsClasses";
		String nameOfListCombinationPropertiesClassRelationshipsRelationships = "listCombinationPropertiesClassRelationshipsRelationships";
		String nameOfListCombinationPropertiesClassRelationshipsPropertiesOfClasses = "listCombinationPropertiesClassRelationshipsPropertiesOfClasses";
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
		
		ArrayList<UriAndNumberAndNumberAndNumber> listMostUsedPropertyUsagePerObjectSource = new ArrayList<UriAndNumberAndNumberAndNumber>();
		Path pathNameListMostUsedPropertyUsagePerObjectSource = Paths.get(pathForSourceResults, nameOfListMostUsedPropertyUsagePerObject + ".json");
	    try {
			listMostUsedPropertyUsagePerObjectSource = ProfilingUtil.makeArrayListUriAndNumberAndNumberAndNumber(pathNameListMostUsedPropertyUsagePerObjectSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumberAndNumberAndNumber> listMostUsedPropertyUsagePerObjectTarget = new ArrayList<UriAndNumberAndNumberAndNumber>();
		Path pathNameListMostUsedPropertyUsagePerObjectTarget = Paths.get(pathForTargetResults, nameOfListMostUsedPropertyUsagePerObject + ".json");
	    try {
			listMostUsedPropertyUsagePerObjectTarget = ProfilingUtil.makeArrayListUriAndNumberAndNumberAndNumber(pathNameListMostUsedPropertyUsagePerObjectTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriAndNumberAndNumberAndNumber> listMostUsedPropertyUsagePerSubjectSource = new ArrayList<UriAndNumberAndNumberAndNumber>();
		Path pathNameListMostUsedPropertyUsagePerSubjectSource = Paths.get(pathForSourceResults, nameOfListMostUsedPropertyUsagePerSubject + ".json");
	    try {
			listMostUsedPropertyUsagePerSubjectSource = ProfilingUtil.makeArrayListUriAndNumberAndNumberAndNumber(pathNameListMostUsedPropertyUsagePerSubjectSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumberAndNumberAndNumber> listMostUsedPropertyUsagePerSubjectTarget = new ArrayList<UriAndNumberAndNumberAndNumber>();
		Path pathNameListMostUsedPropertyUsagePerSubjectTarget = Paths.get(pathForTargetResults, nameOfListMostUsedPropertyUsagePerSubject + ".json");
	    try {
			listMostUsedPropertyUsagePerSubjectTarget = ProfilingUtil.makeArrayListUriAndNumberAndNumberAndNumber(pathNameListMostUsedPropertyUsagePerSubjectTarget.toString());
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

		ArrayList<Uri> listClassLanguagesSource = new ArrayList<Uri>();
		Path pathNamelistClassLanguagesSource = Paths.get(pathForSourceResults, nameOfListLanguagesClass + ".json");
	    try {
			listClassLanguagesSource = ProfilingUtil.makeArrayListUri(pathNamelistClassLanguagesSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Uri> listClassLanguagesTarget = new ArrayList<Uri>();
		Path pathNamelistClassLanguagesTarget = Paths.get(pathForTargetResults, nameOfListLanguagesClass + ".json");
	    try {
			listClassLanguagesTarget = ProfilingUtil.makeArrayListUri(pathNamelistClassLanguagesTarget.toString());
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
		
		ArrayList<UriAndUriAndValue> listMaxPerPropertySource = new ArrayList<UriAndUriAndValue>();
		Path pathNamelistMaxPerPropertySource = Paths.get(pathForSourceResults, nameOfListMaxPerProperty + ".json");
	    try {
			listMaxPerPropertySource = ProfilingUtil.makeArrayListUriAndUriAndValue(pathNamelistMaxPerPropertySource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndUriAndValue> listMaxPerPropertyTarget = new ArrayList<UriAndUriAndValue>();
		Path pathNamelistMaxPerPropertyTarget = Paths.get(pathForTargetResults, nameOfListMaxPerProperty + ".json");
	    try {
			listMaxPerPropertyTarget = ProfilingUtil.makeArrayListUriAndUriAndValue(pathNamelistMaxPerPropertyTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriAndUriAndValue> listPerPropertySource = new ArrayList<UriAndUriAndValue>();
		Path pathNamelistPerPropertySource = Paths.get(pathForSourceResults, nameOfListPerProperty + ".json");
	    try {
			listPerPropertySource = ProfilingUtil.makeArrayListUriAndUriAndValue(pathNamelistPerPropertySource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndUriAndValue> listPerPropertyTarget = new ArrayList<UriAndUriAndValue>();
		Path pathNamelistPerPropertyTarget = Paths.get(pathForTargetResults, nameOfListPerProperty + ".json");
	    try {
			listPerPropertyTarget = ProfilingUtil.makeArrayListUriAndUriAndValue(pathNamelistPerPropertyTarget.toString());
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
		
		ArrayList<UriAndNumber> listMostUsedPropertySource = new ArrayList<UriAndNumber>();
		Path pathNamelistMostUsedPropertySource = Paths.get(pathForSourceResults, nameOfListMostUsedProperty + ".json");
	    try {
			listMostUsedPropertySource = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistMostUsedPropertySource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<UriAndNumber> listMostUsedPropertyTarget = new ArrayList<UriAndNumber>();
		Path pathNamelistMostUsedPropertyTarget = Paths.get(pathForTargetResults, nameOfListMostUsedProperty + ".json");
	    try {
			listMostUsedPropertyTarget = ProfilingUtil.makeArrayListUriAndNumber(pathNamelistMostUsedPropertyTarget.toString());
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
		
		// ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestCountSource = new ArrayList<UriAndUriAndUriAndNumber>();
		// Path pathNamelistClassAndPropertyOfInterestCountSource = Paths.get(pathForSourceResults, nameOfListClassAndPropertyOfInterestCount + ".json");
	    // try {
		// 	listClassAndPropertyOfInterestCountSource = ProfilingUtil.makeArrayListUriAndUriAndUriAndNumber(pathNamelistClassAndPropertyOfInterestCountSource.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		// ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestCountTarget = new ArrayList<UriAndUriAndUriAndNumber>();
		// Path pathNamelistClassAndPropertyOfInterestCountTarget = Paths.get(pathForTargetResults, nameOfListClassAndPropertyOfInterestCount + ".json");
	    // try {
		// 	listClassAndPropertyOfInterestCountTarget = ProfilingUtil.makeArrayListUriAndUriAndUriAndNumber(pathNamelistClassAndPropertyOfInterestCountTarget.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }

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
		
		ArrayList<Uri> listMostUsedPropertyDatatypesSource = new ArrayList<Uri>();
		Path pathNamelistMostUsedPropertyDatatypesSource = Paths.get(pathForSourceResults, nameOfListDatatypesOfInterest + ".json");
	    try {
			listMostUsedPropertyDatatypesSource = ProfilingUtil.makeArrayListUri(pathNamelistMostUsedPropertyDatatypesSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Uri> listMostUsedPropertyDatatypesTarget = new ArrayList<Uri>();
		Path pathNamelistMostUsedPropertyDatatypesTarget = Paths.get(pathForTargetResults, nameOfListDatatypesOfInterest + ".json");
	    try {
			listMostUsedPropertyDatatypesTarget = ProfilingUtil.makeArrayListUri(pathNamelistMostUsedPropertyDatatypesTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClassSource = new ArrayList<UriListAndUriList>();
		Path pathNamelistCombinationPropertiesWithNewClassSource = Paths.get(pathForSourceResults, nameOfListCombinationPropertiesWithNewClass + ".json");
	    try {
			listCombinationPropertiesWithNewClassSource = ProfilingUtil.makeArrayListUriListAndUriList2(pathNamelistCombinationPropertiesWithNewClassSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClassTarget = new ArrayList<UriListAndUriList>();
		Path pathNamelistCombinationPropertiesWithNewClassTarget = Paths.get(pathForTargetResults, nameOfListCombinationPropertiesWithNewClass + ".json");
	    try {
			listCombinationPropertiesWithNewClassTarget = ProfilingUtil.makeArrayListUriListAndUriList2(pathNamelistCombinationPropertiesWithNewClassTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// ArrayList<UriAndUriAndUriAndNumber> listCombinationPropertiesClassRelationshipsSource = new ArrayList<UriAndUriAndUriAndNumber>();
		// Path pathNameListCombinationPropertiesClassRelationshipsSource = Paths.get(pathForTargetResults, nameOfListCombinationPropertiesWithNewClass + ".json");
	    // try {
		// 	listCombinationPropertiesClassRelationshipsSource = ProfilingUtil.makeArrayListUriAndUriAndUriAndNumber(pathNameListCombinationPropertiesClassRelationshipsSource.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		
		// ArrayList<UriAndUriAndUriAndNumber> listCombinationPropertiesClassRelationshipsTarget = new ArrayList<UriAndUriAndUriAndNumber>();
		// Path pathNameListCombinationPropertiesClassRelationshipsTarget = Paths.get(pathForTargetResults, nameOfListCombinationPropertiesClassRelationships + ".json");
	    // try {
		// 	listCombinationPropertiesClassRelationshipsTarget = ProfilingUtil.makeArrayListUriAndUriAndUriAndNumber(pathNameListCombinationPropertiesClassRelationshipsTarget.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }

		// ArrayList<UriListAndUriList> listOfRelationshipsDomainSource = new ArrayList<UriListAndUriList>();
		// Path pathNameListOfRelationshipsDomainSource = Paths.get(pathForSourceResults, nameOfListOfRelationshipsDomain + ".json");
	    // try {
		// 	listOfRelationshipsDomainSource = ProfilingUtil.makeArrayListUriListAndUriList(pathNameListOfRelationshipsDomainSource.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		
		// ArrayList<UriListAndUriList> listOfRelationshipsDomainTarget = new ArrayList<UriListAndUriList>();
		// Path pathNameListOfRelationshipsDomainTarget = Paths.get(pathForTargetResults, nameOfListOfRelationshipsDomain + ".json");
	    // try {
		// 	listOfRelationshipsDomainTarget = ProfilingUtil.makeArrayListUriListAndUriList(pathNameListOfRelationshipsDomainTarget.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }

		// ArrayList<UriListAndUriList> listOfRelationshipsRangeSource = new ArrayList<UriListAndUriList>();
		// Path pathNamelistOfRelationshipsRangeSource = Paths.get(pathForSourceResults, nameOfListOfRelationshipsRange + ".json");
	    // try {
		// 	listOfRelationshipsRangeSource = ProfilingUtil.makeArrayListUriListAndUriList(pathNamelistOfRelationshipsRangeSource.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		
		// ArrayList<UriListAndUriList> listOfRelationshipsRangeTarget = new ArrayList<UriListAndUriList>();
		// Path pathNamelistOfRelationshipsRangeTarget = Paths.get(pathForTargetResults, nameOfListOfRelationshipsRange + ".json");
	    // try {
		// 	listOfRelationshipsRangeTarget = ProfilingUtil.makeArrayListUriListAndUriList(pathNamelistOfRelationshipsRangeTarget.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }

		ArrayList<String> listCombinationPropertiesClassRelationshipsClassesSource = new ArrayList<String>();
		Path pathNameListCombinationPropertiesClassRelationshipsClassesSource = Paths.get(pathForSourceResults, nameOfListCombinationPropertiesClassRelationshipsClasses + ".json");
	    try {
			listCombinationPropertiesClassRelationshipsClassesSource = ProfilingUtil.makeArrayListString2(pathNameListCombinationPropertiesClassRelationshipsClassesSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<String> listCombinationPropertiesClassRelationshipsClassesTarget = new ArrayList<String>();
		Path pathNameListCombinationPropertiesClassRelationshipsClassesTarget = Paths.get(pathForTargetResults, nameOfListCombinationPropertiesClassRelationshipsClasses + ".json");
	    try {
			listCombinationPropertiesClassRelationshipsClassesTarget = ProfilingUtil.makeArrayListString2(pathNameListCombinationPropertiesClassRelationshipsClassesTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriListAndUriAndUriList> listCombinationPropertiesClassRelationshipsRelationshipsSource = new ArrayList<UriListAndUriAndUriList>();
		Path pathNameListCombinationPropertiesClassRelationshipsRelationshipsSource = Paths.get(pathForSourceResults, nameOfListCombinationPropertiesClassRelationshipsRelationships + ".json");
	    try {
			listCombinationPropertiesClassRelationshipsRelationshipsSource = ProfilingUtil.makeArrayListUriListAndUriAndUriList(pathNameListCombinationPropertiesClassRelationshipsRelationshipsSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriListAndUriAndUriList> listCombinationPropertiesClassRelationshipsRelationshipsTarget = new ArrayList<UriListAndUriAndUriList>();
		Path pathNameListCombinationPropertiesClassRelationshipsRelationshipsTarget = Paths.get(pathForTargetResults, nameOfListCombinationPropertiesClassRelationshipsRelationships + ".json");
	    try {
			listCombinationPropertiesClassRelationshipsRelationshipsTarget = ProfilingUtil.makeArrayListUriListAndUriAndUriList(pathNameListCombinationPropertiesClassRelationshipsRelationshipsTarget.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<UriListAndUriAndUriListList> listCombinationPropertiesClassRelationshipsPropertiesOfClassesSource = new ArrayList<UriListAndUriAndUriListList>();
		Path pathNameListCombinationPropertiesClassRelationshipsPropertiesOfClassesSource = Paths.get(pathForSourceResults, nameOfListCombinationPropertiesClassRelationshipsPropertiesOfClasses + ".json");
	    try {
			listCombinationPropertiesClassRelationshipsPropertiesOfClassesSource = ProfilingUtil.makeArrayListUriListAndUriAndUriListList(pathNameListCombinationPropertiesClassRelationshipsPropertiesOfClassesSource.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<UriListAndUriAndUriListList> listCombinationPropertiesClassRelationshipsPropertiesOfClassesTarget = new ArrayList<UriListAndUriAndUriListList>();
		Path pathNameListCombinationPropertiesClassRelationshipsPropertiesOfClassesTarget = Paths.get(pathForTargetResults, nameOfListCombinationPropertiesClassRelationshipsPropertiesOfClasses + ".json");
	    try {
			listCombinationPropertiesClassRelationshipsPropertiesOfClassesTarget = ProfilingUtil.makeArrayListUriListAndUriAndUriListList(pathNameListCombinationPropertiesClassRelationshipsPropertiesOfClassesTarget.toString());
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
		
		listLptsTemp = TraitListLanguagesClass.makeList(listClassLanguagesSource, listClassLanguagesTarget);
		listLpts.addAll(listLptsTemp);
		
		///////////////////////////////////////////////////////////////
		// Recherche de problèmes liés aux datatypes                 //
		///////////////////////////////////////////////////////////////

		listLptsTemp = TraitListDatatypes.makeList(listMostUsedPropertyDatatypesSource, listMostUsedPropertyDatatypesTarget);
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
		
		Object[][] tableauDeuxD = new Object[46 
		][3];
		tableauDeuxD = TraitResultsForML.makeResultsForML(idPair, resultsSource, resultsTarget,
		listOfDatatypesSource, listOfDatatypesTarget,
		listOfLanguagesPredicatSource, listOfLanguagesPredicatTarget,
		listOfLanguagesPredicatValueSource, listOfLanguagesPredicatValueTarget,
		listClassLanguagesSource, listClassLanguagesTarget,
		listLinksSource, listLinksTarget,
		listSubjectVocabularySource, listSubjectVocabularyTarget,
		listPredicatVocabularySource, listPredicatVocabularyTarget,
		listObjectVocabularySource, listObjectVocabularyTarget,
		listCombinationPropertiesWithNewClassSource, listCombinationPropertiesWithNewClassTarget,
		// listOfRelationshipsDomainSource, listOfRelationshipsDomainTarget,
		// listOfRelationshipsRangeSource, listOfRelationshipsRangeTarget,
		listCombinationPropertiesClassRelationshipsClassesSource, listCombinationPropertiesClassRelationshipsClassesTarget,
		listCombinationPropertiesClassRelationshipsRelationshipsSource, listCombinationPropertiesClassRelationshipsRelationshipsTarget,
		listCombinationPropertiesClassRelationshipsPropertiesOfClassesSource, listCombinationPropertiesClassRelationshipsPropertiesOfClassesTarget,
		listMostUsedPropertyUsagePerObjectSource, listMostUsedPropertyUsagePerObjectTarget,
		listMostUsedPropertyUsagePerSubjectSource, listMostUsedPropertyUsagePerSubjectTarget,
		listClassAndSubclassSource, listClassAndSubclassTarget,
		listPropertyAndSubpropertySource, listPropertyAndSubpropertyTarget
		);
		String nameOfResultsForMLfile = "resultsForMLfile";
		ProfilingUtil.saveTwoDimensionalTableInCSV(tableauDeuxD, nameOfResultsForMLfile + ".csv");
	}
}