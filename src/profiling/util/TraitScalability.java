package profiling.util;

import java.util.ArrayList;


public class TraitScalability {
	static Integer commonDatatypes = 0;
	// Recherche des LPT liés aux datatypes
	public static ArrayList<Lpt> makeList(ProfilingResultsObject resultsSource, ProfilingResultsObject resultsTarget) {
		
		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();
		Boolean scalabilitySourceProblem = false;
		Boolean scalabilityTargetProblem = false;
		Integer numberOfTriplesSource = resultsSource.getNumberOfTriples(); 
		Integer numberOfTriplesTarget = resultsTarget.getNumberOfTriples(); 
		Long runningTimeSource = resultsSource.getRunningTimeInSecond(); 
		Long runningTimeTarget = resultsTarget.getRunningTimeInSecond(); 
		Long runningTimeMax = (long) 1800;
		Integer scalabilityScoreMin = 2000;
		Integer scalabilityScoreSource = 0;
		Integer scalabilityScoreTarget = 0; 
		
		if (runningTimeSource > 0) {
			scalabilityScoreSource = (int) (numberOfTriplesSource/runningTimeSource);
		}
		if (runningTimeTarget > 0) {	
			scalabilityScoreTarget = (int) (numberOfTriplesTarget/runningTimeTarget);
		}	
        // System.out.println("scalabilityScoreSource : " + scalabilityScoreSource);
		// System.out.println("scalabilityScoreTarget : " + scalabilityScoreTarget);

		// if (scalabilityScoreSource <= scalabilityScoreMin) {
		// 	scalabilitySourceProblem	= true;
		// }

		// if (scalabilityScoreTarget <= scalabilityScoreMin) {
		// 	scalabilityTargetProblem	= true;
		// }
		// Trouver un autre moyen
		if (runningTimeSource >= runningTimeMax) {
			scalabilitySourceProblem = true;
		}

		if (runningTimeTarget >= runningTimeMax) {
			scalabilityTargetProblem = true;
		}

		// Si au moins un probléme de scalabilité pour un des datasets source ou target
		if (scalabilitySourceProblem || scalabilityTargetProblem) {
			// LPT 5.7: Graph scalability Problem	
			listLpts.add(new Lpt("LPT5.7"));
		}
	
		return listLpts;
	}

	
}