package profiling.util;

import java.util.ArrayList;


public class TraitLabels {
	// Recherche des LPT liés aux labels
	public static ArrayList<Lpt> makeList(ProfilingResultsObject resultsSource, ProfilingResultsObject resultsTarget) {
		
		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();
		Boolean labelsSourceProblem = false;
		Boolean labelsTargetProblem = false;

		if (resultsSource.getNumberLabeledSubjects() == 0 &
			resultsSource.getNumberLabeledPredicates() == 0 &
			resultsSource.getNumberLabeledObjects() == 0
			) {
			labelsSourceProblem	= true;
		}

		if (resultsTarget.getNumberLabeledSubjects() == 0 &
			resultsTarget.getNumberLabeledPredicates() == 0 &
			resultsTarget.getNumberLabeledObjects() == 0
			) {
			labelsTargetProblem	= true;
		}
		
		// Si au moins un probléme de label pour un des datasets source ou target
		if (labelsSourceProblem || labelsTargetProblem) {
			// LPT 4.2: Subgraph no textual description problem	
			listLpts.add(new Lpt("LPT4.2"));
		}
	
		return listLpts;
	}

	
}