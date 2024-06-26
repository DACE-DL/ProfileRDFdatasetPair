package profiling.util;

import java.util.ArrayList;


public class TraitsIntraAgregation {
	// Recherche des LPT liés à présence de noeuds anonymes
	public static ArrayList<Lpt> makeList(ProfilingResultsObject resultsSource, ProfilingResultsObject resultsTarget) {
		
		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();
		Boolean intraAgregationProblem = false;
		
		if (!(resultsSource.getNumberBlanksAsObj() == resultsTarget.getNumberBlanksAsObj())
			|| !(resultsSource.getNumberBlanksAsSubj() == resultsTarget.getNumberBlanksAsSubj())
		) {
			intraAgregationProblem	= true;
		}

		// Si au moins un probléme pour un des datasets source ou target
		if (intraAgregationProblem) {
			// LPT 2.2.1: Predicate structural intra-aggregation problem	
			listLpts.add(new Lpt("LPT2.2.1"));
		}
	
		return listLpts;
	}

	
}