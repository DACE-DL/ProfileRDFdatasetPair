package profiling.util;

import java.util.ArrayList;


public class TraitsHierarchiesLoop {
	// Recherche des LPT liés à la hiérarchie
	public static ArrayList<Lpt> makeList(ProfilingResultsObject resultsSource, ProfilingResultsObject resultsTarget) {
		
		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();
		Boolean hierarchiesSourceProblem = false;
		Boolean hierarchiesTargetProblem = false;

		if (resultsSource.isClassHierarchyLoop()) {
			hierarchiesSourceProblem	= true;
		}

		if (resultsTarget.isClassHierarchyLoop()) {
			hierarchiesTargetProblem	= true;
		}
		
		// Si au moins un probléme pour un des datasets source ou target
		if (hierarchiesSourceProblem || hierarchiesTargetProblem) {
			// LPT 3.2: Class generalization/specialization problem	
			listLpts.add(new Lpt("LPT3.2"));
		}
	
		return listLpts;
	}

	
}