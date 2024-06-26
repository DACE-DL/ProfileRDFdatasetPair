package profiling.util;

import java.util.ArrayList;


public class TraitsHierarchiesDeep {
	// Recherche des LPT liés à la hiérarchie
	public static ArrayList<Lpt> makeList(ProfilingResultsObject resultsSource, ProfilingResultsObject resultsTarget) {
		
		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();
		Boolean hierarchiesProblem = false;
		
		if (!(resultsSource.getClassHierarchyDeep() == resultsTarget.getClassHierarchyDeep())) {
			hierarchiesProblem	= true;
		}

		// Si au moins un probléme pour un des datasets source ou target
		if (hierarchiesProblem) {
			// LPT 3.2: Class generalization/specialization problem	
			listLpts.add(new Lpt("LPT3.2"));
		}
	
		return listLpts;
	}

	
}