package profiling.util;

import java.util.ArrayList;


public class TraitsOntologyDomain {
	// Recherche des LPT liés à l'absence de modèle descriptif
	public static ArrayList<Lpt> makeList(ArrayList<Uri> listClassDefinedSource, ArrayList<Uri> listClassDefinedTarget,ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationshipsSource, ArrayList<UriListAndUriAndUriListAndNumber>listCombinationPropertiesClassRelationshipsTarget) {
		
		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();
		Boolean ontologyDomainProblem = false;

		// On estime que si il n'y à pas de classes definie en tant que telles (rdf:type rdfs:Class||?o=owl:Class)
		//  il y peu de chance qu'un modèle descriptif est été définie dans le graphe.
		if (listClassDefinedSource.size() == 0) {
			ontologyDomainProblem	= true;
		}

		if (listClassDefinedTarget.size() == 0) {
			ontologyDomainProblem	= true;
		}
		
		//  idem si pas de relation entre classes.
		if (listCombinationPropertiesClassRelationshipsSource.size() == 0) {
			ontologyDomainProblem	= true;
		}

		if (listCombinationPropertiesClassRelationshipsTarget.size() == 0) {
			ontologyDomainProblem	= true;
		}
		
		// Si au moins un probléme pour un des datasets source ou target
		if (ontologyDomainProblem) {
			// LPT 5.8: Graph lack of domain ontology Problem	
			listLpts.add(new Lpt("LPT5.8"));
		}
	
		return listLpts;
	}

	
}