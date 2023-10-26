package inferencesAndQueries;

import java.util.ArrayList;
import profiling.util.*;

public class ProfilingPostProcessing{
	
	// On effectue les post-traitements
	public static void makeTreatements() {

		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();

		

		
		
		///////////////////////////////////////////////////////////////
		// Transfert de la liste des LPT extrait dans fichers .json  //
		///////////////////////////////////////////////////////////////


		String nameOfResultsFile = "resultingLPTs";
		try {
			ProfilingUtil.makeJsonLptFile(listLpts, nameOfResultsFile + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}