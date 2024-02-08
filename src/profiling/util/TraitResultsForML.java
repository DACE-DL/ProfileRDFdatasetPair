package profiling.util;

import java.util.ArrayList;
import java.util.List;

import org.opengis.metadata.Datatype;


public class TraitResultsForML {
	// Constitution d'un tableau à deux dimensions pour le Machine learning
	public static Object[][] makeResultsForML(String idPair, ProfilingResultsObject resultsSource, ProfilingResultsObject resultsTarget, 
	ArrayList<Uri> listDatatypesSource, ArrayList<Uri> listDatatypesTarget, 
	ArrayList<Uri> listOfLanguagesPredicatSource, ArrayList<Uri> listOfLanguagesPredicatTarget,
	ArrayList<Uri> listOfLanguagesPredicatValueSource, ArrayList<Uri> listOfLanguagesPredicatValueTarget,
	ArrayList<Uri> listOfLanguagesClassSource, ArrayList<Uri> listOfLanguagesClassTarget,
	ArrayList<UriAndUri> listLinksSource, ArrayList<UriAndUri> listLinksTarget,
	List<String> listSubjectVocabularySource, List<String> listSubjectVocabularyTarget,
	List<String> listPredicatVocabularySource, List<String> listPredicatVocabularyTarget,
	List<String> listObjectVocabularySource, List<String> listObjectVocabularyTarget,
	ArrayList<UriAndUriList> listOfNewClassWithPropertiesCombinaisonSource, ArrayList<UriAndUriList> listOfNewClassWithPropertiesCombinaisonTarget,
	ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClassesSource, ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClassesTarget,
	ArrayList<UriListAndUriList> listOfRelationshipsDomainSource, ArrayList<UriListAndUriList> listOfRelationshipsDomainTarget,
	ArrayList<UriListAndUriList> listOfRelationshipsRangeSource, ArrayList<UriListAndUriList> listOfRelationshipsRangeTarget,	
	ArrayList<String> listMostImportantClassesSource, ArrayList<String> listMostImportantClassesTarget,
	ArrayList<String> listMostImportantRelationshipsBetweenClassesSource, ArrayList<String> listMostImportantRelationshipsBetweenClassesTarget,
	ArrayList<UriAndUriAndUriListList> listMostImportantPropertiesOfClassesSource, ArrayList<UriAndUriAndUriListList> listMostImportantPropertiesOfClassesTarget
	) {
		// Déclaration et initialisation d'un tableau en deux dimensions avec des chiffres et des listes
        Object[][] tableauDeuxD = new Object[40][3];
		Integer numberLine = 0;
		ArrayList<String> characteristics = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        ProfilingUtil.listCharacteristics(resultsSource, characteristics, values);

		tableauDeuxD[0][0] = idPair;
		tableauDeuxD[0][1] = "Source";
		tableauDeuxD[0][2] = "Target";

        for (int i = 0; i < characteristics.size(); i++) {
			tableauDeuxD[i+1][0] = characteristics.get(i);
			numberLine = i + 2;
		}
		// Pour le dataset source
		for (int i = 0; i < values.size(); i++) {
			tableauDeuxD[i+1][1] = values.get(i);
		}
		
		ArrayList<String> aList;
		ArrayList<ArrayList<String>> aListOfList;
		
		tableauDeuxD[numberLine][0] = "List datatypes";
		aList = new ArrayList<>();
		for (Uri datatype : listDatatypesSource) {
			aList.add(datatype.getUri());
		}
		tableauDeuxD[numberLine][1] = new ArrayList<>(aList);
		
		tableauDeuxD[numberLine + 1][0] = "List of languages predicat";
		aList.clear();
		aList = new ArrayList<>();
		for (Uri element : listOfLanguagesPredicatSource) {
			aList.add(element.getUri());
		}
		tableauDeuxD[numberLine + 1][1] = new ArrayList<>(aList);
		
		tableauDeuxD[numberLine + 2][0] = "List of languages predicat value";
		aList.clear();
		aList = new ArrayList<>();
		for (Uri element : listOfLanguagesPredicatValueSource) {
			aList.add(element.getUri());
		}
		tableauDeuxD[numberLine + 2][1] = new ArrayList<>(aList);

		tableauDeuxD[numberLine + 3][0] = "List of class languages";
		aList.clear();
		aList = new ArrayList<>();
		for (Uri element : listOfLanguagesClassSource) {
			aList.add(element.getUri());
		}
		tableauDeuxD[numberLine + 3][1] = new ArrayList<>(aList);

		aListOfList = new ArrayList<>();
		tableauDeuxD[numberLine + 4][0] = "List of links";
		for (UriAndUri element : listLinksSource) {
			aList.clear();
			aList = new ArrayList<>();
			aList.add(element.getUri1());
			aList.add(element.getUri2());
			aListOfList.add(new ArrayList<>(aList));
		}
		tableauDeuxD[numberLine + 4][1] = new ArrayList<>(aListOfList);

		tableauDeuxD[numberLine + 5][0] = "List of subject vocabulary";
		aList.clear();
		aList = new ArrayList<>();
		for (String element : listSubjectVocabularySource) {
			aList.add(element);
		}
		tableauDeuxD[numberLine + 5][1] = new ArrayList<>(aList);

		tableauDeuxD[numberLine + 6][0] = "List of predicat vocabulary";
		aList.clear();
		aList = new ArrayList<>();
		for (String element : listPredicatVocabularySource) {
			aList.add(element);
		}
		tableauDeuxD[numberLine + 6][1] = new ArrayList<>(aList);
		
		tableauDeuxD[numberLine + 7][0] = "List of object vocabulary";
		aList.clear();
		aList = new ArrayList<>();
		for (String element : listObjectVocabularySource) {
			aList.add(element);
		}
		tableauDeuxD[numberLine + 7][1] = new ArrayList<>(aList);

		tableauDeuxD[numberLine + 8][0] = "List of combinaison of properties";
		aList.clear();
		aList = new ArrayList<>();
		for (UriAndUriList element : listOfNewClassWithPropertiesCombinaisonSource) {
			aList.add(element.toString());
		}
		tableauDeuxD[numberLine + 8][1] = new ArrayList<>(aList);
		
		tableauDeuxD[numberLine + 9][0] = "List of domain relationships";
		aList.clear();
		aList = new ArrayList<>();
		for (UriListAndUriList element : listOfRelationshipsDomainSource) {
			aList.add(element.toString());
		}
		tableauDeuxD[numberLine + 9][1] = new ArrayList<>(aList);

		tableauDeuxD[numberLine + 10][0] = "List of range relationships";
		aList.clear();
		aList = new ArrayList<>();
		for (UriListAndUriList element : listOfRelationshipsRangeSource) {
			aList.add(element.toString());
		}
		tableauDeuxD[numberLine + 10][1] = new ArrayList<>(aList);



		// Pour le dataset target 
		values.clear();
		ProfilingUtil.listCharacteristics(resultsTarget, characteristics, values);
		for (int i = 0; i < values.size(); i++) {
			tableauDeuxD[i+1][2] = values.get(i);
		}
		
		aList.clear();
		aList = new ArrayList<>();
		for (Uri datatype : listDatatypesTarget) {
			aList.add(datatype.getUri());
		}
		tableauDeuxD[numberLine][2] = new ArrayList<>(aList);
		
		aList.clear();
		aList = new ArrayList<>();
		for (Uri element : listOfLanguagesPredicatTarget) {
			aList.add(element.getUri());
		}
		tableauDeuxD[numberLine + 1][2] = new ArrayList<>(aList);

		aList.clear();
		aList = new ArrayList<>();
		for (Uri element : listOfLanguagesPredicatValueTarget) {
			aList.add(element.getUri());
		}
		tableauDeuxD[numberLine + 2][2] = new ArrayList<>(aList);

		aList.clear();
		aList = new ArrayList<>();
		for (Uri element : listOfLanguagesClassTarget) {
			aList.add(element.getUri());
		}
		tableauDeuxD[numberLine + 3][2] = new ArrayList<>(aList);

		aListOfList.clear();
		aListOfList = new ArrayList<>();
		for (UriAndUri element : listLinksTarget) {
			aList.clear();
			aList = new ArrayList<>();
			aList.add(element.getUri1());
			aList.add(element.getUri2());
			aListOfList.add(new ArrayList<>(aList));
		}
		tableauDeuxD[numberLine + 4][2] = new ArrayList<>(aListOfList);

		aList.clear();
		aList = new ArrayList<>();
		for (String element : listSubjectVocabularyTarget) {
			aList.add(element);
		}
		tableauDeuxD[numberLine + 5][2] = new ArrayList<>(aList);

		aList.clear();
		aList = new ArrayList<>();
		for (String element : listPredicatVocabularyTarget) {
			aList.add(element);
		}
		tableauDeuxD[numberLine + 6][2] = new ArrayList<>(aList);

		aList.clear();
		aList = new ArrayList<>();
		for (String element : listObjectVocabularyTarget) {
			aList.add(element);
		}
		tableauDeuxD[numberLine + 7][2] = new ArrayList<>(aList);

		aList.clear();
		aList = new ArrayList<>();
		for (UriAndUriList element : listOfNewClassWithPropertiesCombinaisonTarget) {
			aList.add(element.toString());
		}
		tableauDeuxD[numberLine + 8][2] = new ArrayList<>(aList);
		
		aList.clear();
		aList = new ArrayList<>();
		for (UriListAndUriList element : listOfRelationshipsDomainTarget) {
			aList.add(element.toString());
		}
		tableauDeuxD[numberLine + 9][2] = new ArrayList<>(aList);

		aList.clear();
		aList = new ArrayList<>();
		for (UriListAndUriList element : listOfRelationshipsRangeTarget) {
			aList.add(element.toString());
		}
		tableauDeuxD[numberLine + 10][2] = new ArrayList<>(aList);

		
		return tableauDeuxD;
	}

}