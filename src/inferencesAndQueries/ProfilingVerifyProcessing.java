package inferencesAndQueries;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.apache.jena.rdf.model.Model;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import profiling.util.*;

public class ProfilingVerifyProcessing {
	
	// On effectue les pré-traitements
	public static void makeTreatements(Model model) throws JsonParseException, JsonMappingException, IOException {
		
		Instant start0 = Instant.now();

		ProfilingResultsObject results = new ProfilingResultsObject();

		////////////////////////////////////
		//// Pour le dataset en général ////
		////////////////////////////////////
		

		// Liste des propriétés et de leur usage.
		String nameOfListPropertyUsageCount = "listPropertyUsageCount";
		ArrayList<UriAndNumber> listPropertyUsageCount = new ArrayList<UriAndNumber>();
		listPropertyUsageCount = MakeListPropertyUsageCount.makeList(model);

		// Liste des propriétés déclarées.
		String nameOfListPropertyDeclared = "listPropertyDeclared";
		ArrayList<UriAndUri> listPropertyDeclared = new ArrayList<UriAndUri>();
		listPropertyDeclared = VerifyPropertyDeclared.makeList(model);

		// Liste des propriétés et leur domain and range déclarés.
		String nameOfListPropertyDomainAndRange = "listPropertyDomainAndRange";
		ArrayList<UriAndX2UriAndUriListAndUriListAndUriList> listPropertyDomainAndRange = new ArrayList<UriAndX2UriAndUriListAndUriListAndUriList>();
		listPropertyDomainAndRange = VerifyPropertyDomainAndRange.makeList(model);

		// Liste des propriétés des propriétés (Transitive, reflective, ...).
		String nameOfListPropertyProperties = "listPropertyProperties";
		ArrayList<UriAndUri> listPropertyProperties = new ArrayList<UriAndUri>();
		listPropertyProperties = VerifyPropertyProperties.makeList(model);

		// Liste des ressources liées par une relation sameAs.
		String nameOfListResourcesSameAs = "listResourcesSameAs";
		ArrayList<UriAndUri> listResourcesSameAs = new ArrayList<UriAndUri>();
		listResourcesSameAs = VerifyResourcesSameAs.makeList(model);

		// Liste des ressources dépréciées.
		String nameOfListResourceDeprecated = "listResourceDeprecated";
		ArrayList<UriAndUri> listResourceDeprecated = new ArrayList<UriAndUri>();
		listResourceDeprecated = VerifyResourceDeprecated.makeList(model);

		// List of base and empty names space.
		String nameOfListModelBaseAndEmptyNameSpace = "listModelBaseAndEmptyNameSpace";
		ArrayList<UriAndString> listModelBaseAndEmptyNameSpace = new ArrayList<UriAndString>();
		listModelBaseAndEmptyNameSpace = VerifyModelBaseAndEmptyNameSpace.makeList(model);

		// List of model names space.
		String nameOfListModelNameSpace = "listModelNameSpace";
		ArrayList<Uri> listModelNameSpace = new ArrayList<Uri>();
		listModelNameSpace = VerifyModelNameSpace.makeList(model);

		// List of links between domain names.
		String nameOfListLinks = "listLinks";
		ArrayList<UriAndUriAndNumber> listLinks = new ArrayList<UriAndUriAndNumber>();
		listLinks = VerifyLinks.makeList(model, listPropertyUsageCount, listModelNameSpace);

		//////////////////////////////////////////////////////////////////////////////////////
		// En comparant les deux listes, on peut observer la différence
		//  sur le nombre de propriétés declarées et celles utilisées dans des triplets
		//  OM versus IM
		//////////////////////////////////////////////////////////////////////////////////////

		// Liste des anomalies pour les propriétés dont le domain et le range sont déclarés.
		String nameOfListPropertyDomainAndRangeAnomalies = "listPropertyDomainAndRangeAnomalies";
		ArrayList<UriAndStringListAndStringList> listPropertyDomainAndRangeAnomalies = new ArrayList<UriAndStringListAndStringList>();
		listPropertyDomainAndRangeAnomalies = VerifyPropertyDomainAndRangeAnomalies.makeList(model, listPropertyDomainAndRange);

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
			ProfilingUtil.makeJsonUriAndUriFile(listPropertyDeclared, nameOfListPropertyDeclared + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ProfilingUtil.makeJsonUriAndX2UriAndUriListAndUriListAndUriListFile(listPropertyDomainAndRange, nameOfListPropertyDomainAndRange + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ProfilingUtil.makeJsonUriAndStringListAndStringListFile(listPropertyDomainAndRangeAnomalies, nameOfListPropertyDomainAndRangeAnomalies + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriFile(listPropertyProperties, nameOfListPropertyProperties + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriFile(listResourcesSameAs, nameOfListResourcesSameAs + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndUriFile(listResourceDeprecated, nameOfListResourceDeprecated + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriAndStringFile(listModelBaseAndEmptyNameSpace, nameOfListModelBaseAndEmptyNameSpace + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ProfilingUtil.makeJsonUriFile(listModelNameSpace, nameOfListModelNameSpace + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}


		try {
			ProfilingUtil.makeJsonUriAndUriAndNumberFile(listLinks, nameOfListLinks + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}