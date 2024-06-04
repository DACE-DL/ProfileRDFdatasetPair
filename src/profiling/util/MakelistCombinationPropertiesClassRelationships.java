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

public class MakelistCombinationPropertiesClassRelationships {
	// Détermination des relations entre les classes
	public static ArrayList<UriListAndUriAndUriListAndNumber> makeList(Model model, ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClass) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClassTemp = new ArrayList<UriListAndUriList>();
		listCombinationPropertiesWithNewClassTemp.addAll(listCombinationPropertiesWithNewClass);
		ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships = new ArrayList<UriListAndUriAndUriListAndNumber>();
		ArrayList<UriListAndUriAndUriListAndNumber> ReducedListCombinationPropertiesClassRelationships = new ArrayList<UriListAndUriAndUriListAndNumber>();
		ArrayList<String> listOfUriListAndUriListTreated = new ArrayList<String>();
		
		Instant start0 = Instant.now();	
		// System.out.println("listCombinationPropertiesWithNewClass size: " + listCombinationPropertiesWithNewClass.size());	
		for (UriListAndUriList resource : listCombinationPropertiesWithNewClass) {
			Instant start1 = Instant.now();	
			for (UriListAndUriList resourceTemp : listCombinationPropertiesWithNewClassTemp) {
				String uriListAndUriListTreated = resource.getUriList1().toString() + "|" + resourceTemp.getUriList1().toString();
				if (!listOfUriListAndUriListTreated.contains(uriListAndUriListTreated)) {
					listOfUriListAndUriListTreated.add(uriListAndUriListTreated);
					// String queryString = buildSparqlString(resource.getUriList1(), resourceTemp.getUriList1(), resource.getUriList2());
					String queryString = buildSparqlString(resource.getUriList1(), resourceTemp.getUriList1(), resource.getUriList2(), resourceTemp.getUriList2());
					Query query = QueryFactory.create(prefix + queryString);
					// System.out.println(queryString);	
					QueryExecution qe = QueryExecutionFactory.create(query, model);	
					ResultSet result = qe.execSelect();
					if (result.hasNext()) {
						while( result.hasNext() ) {
							QuerySolution querySolution = result.next() ;
							UriListAndUriAndUriListAndNumber relationshipsBetweenNewClasses = new UriListAndUriAndUriListAndNumber(resource.getUriList1(),new Uri(querySolution.getResource("property").getURI()),resourceTemp.getUriList1(),querySolution.getLiteral("number").getInt());
							listCombinationPropertiesClassRelationships.add(relationshipsBetweenNewClasses);	
						}
					}
				}					
			}
			Instant end1 = Instant.now();
			//System.out.println("Running time for one: " + ProfilingUtil.getDurationAsString(Duration.between(start1, end1).toMillis()));
		}
		// tri de la liste
		Collections.sort(listCombinationPropertiesClassRelationships, new UriListAndUriAndUriListAndNumberComparator());
		// On réduit la taille de la liste a 100 relations
		Integer n = 0;
		Integer number = 0;
		for (UriListAndUriAndUriListAndNumber relation : listCombinationPropertiesClassRelationships) {
			n++;
			if (n==1) {
				number = relation.getNumber();
				ReducedListCombinationPropertiesClassRelationships.add(relation);
			} else {
				// Si le nombre d'instances de la classe suivante représente plus 
				//  de 1% de la première.
				if (((relation.getNumber() * 100) / number) > 1) {
					ReducedListCombinationPropertiesClassRelationships.add(relation);
				}
			}
			if (n>99) {
				break;
		   	}		
		}
		
		Instant end0 = Instant.now();
		System.out.println("Running time for ListCombinationPropertiesClassRelationships: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return ReducedListCombinationPropertiesClassRelationships;
	}
	static class UriListAndUriAndUriListAndNumberComparator implements java.util.Comparator<UriListAndUriAndUriListAndNumber> {
		@Override
		public int compare(UriListAndUriAndUriListAndNumber a, UriListAndUriAndUriListAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
	

	private static String buildSparqlString(ArrayList<Uri> uriList1, ArrayList<Uri> uriList2 , ArrayList<Uri> uriList3, ArrayList<Uri> uriList4) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT (?relation AS ?property) (COUNT(?relation) AS ?number) ");
		queryBuilder.append(" WHERE { ");
		queryBuilder.append(" ?subject ?relation ?object . ");
		queryBuilder.append(" ?object rdf:type ?typeObject . ");
		queryBuilder.append(" ?subject rdf:type ?typeSubject . ");
		queryBuilder.append(" FILTER ( ");
		queryBuilder.append(" ?relation IN ( ");
		Boolean first1 = true;
		for (Uri uriProperty : uriList3) {
			if (first1) { 
				first1 = false;
				queryBuilder.append(" <").append(uriProperty.toString()).append(">");
			} else {
				queryBuilder.append(" , <").append(uriProperty.toString()).append(">");
			}
		}
		queryBuilder.append(" ) ");
		queryBuilder.append(" ) ");
		queryBuilder.append(" FILTER ( ");
		Boolean first2 = true;
		for (Uri uriClass : uriList1) {
			if (first2) { 
				first2 = false;
				queryBuilder.append(" ?typeSubject = <").append(uriClass.toString()).append(">");
			} else {
				queryBuilder.append(" && ?typeSubject = <").append(uriClass.toString()).append(">");
			}
		}
		queryBuilder.append(" && ");
		// queryBuilder.append(" ) ");
		// queryBuilder.append(" FILTER ( ");
		Boolean first3 = true;
		for (Uri uriClass : uriList2) {
			if (first3) { 
				first3 = false;
				queryBuilder.append(" ?typeObject = <").append(uriClass.toString()).append(">");
			} else {
				queryBuilder.append(" && ?typeObject = <").append(uriClass.toString()).append(">");
			}
		}
		queryBuilder.append(" ) ");
		
		queryBuilder.append(" } GROUP BY ?relation");
		
	    // System.out.println(queryBuilder.toString());	
        // Retourne la requête en tant que chaîne de caractères
        return queryBuilder.toString();
    }

	
}

