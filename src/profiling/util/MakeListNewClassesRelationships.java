package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListNewClassesRelationships {
	// Détermination des relations entre les classes
	public static ArrayList<UriAndUriAndUriAndNumber> makeList(Model model, ArrayList<UriAndUriList> listOfNewClassWithPropertiesCombinaison) {
		
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriList> listOfNewClassWithPropertiesCombinaisonTemp = new ArrayList<UriAndUriList>();
		listOfNewClassWithPropertiesCombinaisonTemp.addAll(listOfNewClassWithPropertiesCombinaison);
		ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClasses = new ArrayList<UriAndUriAndUriAndNumber>();
		ArrayList<UriAndUriAndUriAndNumber> ReducedListOfRelationshipsBetweenNewClasses = new ArrayList<UriAndUriAndUriAndNumber>();
		ArrayList<UriAndUri> listOfUriAndUriTreated = new ArrayList<UriAndUri>();
		
		Instant start0 = Instant.now();	
		for (UriAndUriList resource : listOfNewClassWithPropertiesCombinaison) {
			for (UriAndUriList resourceTemp : listOfNewClassWithPropertiesCombinaisonTemp) {
				UriAndUri uriAndUriTreated = new UriAndUri(resource.getUri(), resourceTemp.getUri());
				if (!listOfUriAndUriTreated.contains(uriAndUriTreated)) {
					listOfUriAndUriTreated.add(uriAndUriTreated);
					Query query = QueryFactory.create(prefix + 
					"SELECT DISTINCT ?class1 ?class2 ?property (COUNT(?property) AS ?number) " +
					" WHERE { " +
					" ?subject1 ?property ?subject2 ." +
					" ?subject1 rdf:type ?class1." +	
					" ?subject2 rdf:type ?class2." +	
					" FILTER (?class1=<" + resource.getUri() + "> && ?class2=<"+ resourceTemp.getUri() + "> ) ." +
					" } GROUP BY ?class1 ?class2 ?property "
					);		
					QueryExecution qe = QueryExecutionFactory.create(query, model);		
					ResultSet result = qe.execSelect();
					if (result.hasNext()) {
						while( result.hasNext() ) {
							QuerySolution querySolution = result.next() ;
							UriAndUriAndUriAndNumber relationshipsBetweenNewClasses = new UriAndUriAndUriAndNumber(resource.getUri(),querySolution.getResource("property").getURI(),resourceTemp.getUri(),querySolution.getLiteral("number").getInt());
							listOfRelationshipsBetweenNewClasses.add(relationshipsBetweenNewClasses);	
						}
					}
				}					
			}
		}
		// tri de la liste
		Collections.sort(listOfRelationshipsBetweenNewClasses, new UriAndUriAndUriAndNumberComparator());
		// On réduit la taille de la liste a 100 relations
		Integer n = 0;
		Integer number = 0;
		for (UriAndUriAndUriAndNumber relation : listOfRelationshipsBetweenNewClasses) {
			n++;
			if (n==1) {
				number = relation.getNumber();
				ReducedListOfRelationshipsBetweenNewClasses.add(relation);
			} else {
				// Si le nombre d'instances de la classe suivante représente plus 
				//  de 1% de la première.
				if (((relation.getNumber() * 100) / number) > 1) {
					ReducedListOfRelationshipsBetweenNewClasses.add(relation);
				}
			}
			if (n>99) {
				break;
		   	}		
		}

		
		Instant end0 = Instant.now();
		System.out.println("Running time for classes relationships: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return ReducedListOfRelationshipsBetweenNewClasses;
	}
	static class UriAndUriAndUriAndNumberComparator implements java.util.Comparator<UriAndUriAndUriAndNumber> {
		@Override
		public int compare(UriAndUriAndUriAndNumber a, UriAndUriAndUriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}

