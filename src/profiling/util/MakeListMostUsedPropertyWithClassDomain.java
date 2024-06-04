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

public class MakeListMostUsedPropertyWithClassDomain {
	static boolean first = false; 
	// Création d'une liste des propriétés les plus utilisées avec datatypes et classes range.
	public static ArrayList<UriAndUriListAndNumberListAndNumber> makeList(Model model, ArrayList<UriAndNumber> listMostUsedProperty, ArrayList<UriAndNumber> listClassMostUsed) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriListAndNumberListAndNumber> listResources = new ArrayList<UriAndUriListAndNumberListAndNumber>();
		ArrayList<UriAndUriListAndNumberListAndNumber> listResourcesTemp = new ArrayList<UriAndUriListAndNumberListAndNumber>();
		ArrayList<UriListAndNumber> ListUriListAndNumber =  new ArrayList<UriListAndNumber>();
		
		// System.out.println("Nombre de propriétés: " + listMostUsedProperty.size()) ;

		for (UriAndNumber uriAndNumber : listMostUsedProperty) {
		// Instant start0 = Instant.now();	
            
		String property = uriAndNumber.getUri();

		Query query = QueryFactory.create(prefix + 
		
		" SELECT ?property " +
		" (GROUP_CONCAT(DISTINCT ?classListAndCount; separator=\"*\") AS ?classListAndCountList)  "	+ 
		" (SUM(?propertyUsage) AS ?propertyCount) " +
		" WHERE {      " +
		" 	SELECT ?property " +
		" 	((CONCAT(?classList, ';', STR(?propertyUsage))) AS ?classListAndCount)  "	+ 
		"   ?propertyUsage  " +
		" 	WHERE {      " +
				"SELECT ?property (COUNT(?property) AS ?propertyUsage) ?classList" +
				" WHERE { " +
					"SELECT ?s ?property ?o " + 
					" (GROUP_CONCAT(DISTINCT ?class; separator=\"|\") AS ?classList) " +
					" WHERE { " +
						"SELECT ?s ?property ?o ?datatype ?class" +
						" WHERE { " +
							" BIND( '' AS ?defaultClass) " +
							" BIND( '' AS ?defaultOrder) " +
							" ?s ?property ?o ." +
							" OPTIONAL { "+
							" ?s rdf:type ?subjectClass " +
							" } " +
							" BIND(COALESCE(?subjectClass, ?defaultClass) AS ?class) " +
							" BIND(COALESCE(?subjectClass , ?defaultOrder) AS ?order) " +
							" FILTER (?property = <" + property + "> )" +
							// convertToSPARQLFilterProperties(listMostUsedProperty) +
							// convertToSPARQLFilterClasses(listClassMostUsed) +
						" } ORDER BY ?s ?property ?o ?order" +
					" } GROUP BY ?s ?property ?o " +
				" } GROUP BY ?property ?classList" +
			" } ORDER BY DESC (?propertyUsage)" +
		" } GROUP BY ?property ORDER BY DESC (?propertyCount) "
		);		
 		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;

				Uri uri = new Uri();
				uri.setUri(querySolution.getResource("property").getURI());
				Integer propertyNumber = querySolution.getLiteral("?propertyCount").getInt();

				ArrayList<UriListAndNumber> classListAndNumberList = new ArrayList<UriListAndNumber>();
				if (!(querySolution.getLiteral("classListAndCountList")==null)) {
					String[] classListAndCountList = querySolution.getLiteral("classListAndCountList").getString().split("\\*");
						
					for (String classListAndCount : classListAndCountList) {
						String[] classListAndCountString = classListAndCount.split("\\;");
						Integer number = Integer.valueOf(classListAndCountString[1]);
						if (!classListAndCountString[0].equals("")) {
							ArrayList<Uri> uriList = new ArrayList<Uri>();
							String[] classListString = classListAndCountString[0].split("\\|");
							for (String classString : classListString) {
								Uri classUri = new Uri(classString);
								uriList.add(classUri);
							}
							UriListAndNumber uriListAndNumber = new UriListAndNumber(uriList, number);
							classListAndNumberList.add(uriListAndNumber);	
						}
					}
				}		
			
				UriAndUriListAndNumberListAndNumber UriAndUriListAndNumberListAndNumber = new UriAndUriListAndNumberListAndNumber();
				UriAndUriListAndNumberListAndNumber.setUri(uri);
				UriAndUriListAndNumberListAndNumber.setUriListAndNumberList(classListAndNumberList);
				UriAndUriListAndNumberListAndNumber.setNumber(propertyNumber);
				listResourcesTemp.add(UriAndUriListAndNumberListAndNumber) ;	
				
			}
			// Instant end0 = Instant.now();
        	// System.out.println("Running for" + property + ": " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		}
		}

		// On nettoye la liste en suprimant dans la liste des listes de classes
		//  les listes de classes qui représentes moins de 1% de l'ensemble des instances des classes pour une propriété donnée 
		for (UriAndUriListAndNumberListAndNumber resource : listResourcesTemp) {
			ArrayList<UriListAndNumber> ListUriListAndNumberTemp =  new ArrayList<UriListAndNumber>();
			if (resource.getUriListAndNumberList().size()<= 1) { // Il n'y a qu'une liste de classes pour la combinaison de propriétés, donc on sélectione 
			listResources.add(new UriAndUriListAndNumberListAndNumber(resource));
			} else {
				ListUriListAndNumber = new ArrayList<UriListAndNumber>(resource.getUriListAndNumberList());
				first = true;
				Integer totalNumber = resource.getNumber();
				Integer numberToSubtract = 0;
				for (UriListAndNumber uriListAndNumber : ListUriListAndNumber) {
					if (first) {
						ListUriListAndNumberTemp.add(new UriListAndNumber(uriListAndNumber));
						first = false;
					} else {
						// Si le nombre d'instances de la liste de classes suivante représente plus 
						//  de 1% de l'ensemble des instances des liste de classes déjà selectionnées pour une combinaison données.
						if (((uriListAndNumber.getNumber() * 100) / totalNumber) > 1) {
							ListUriListAndNumberTemp.add(new UriListAndNumber(uriListAndNumber));
							totalNumber = totalNumber + uriListAndNumber.getNumber();
						} else {
							numberToSubtract = numberToSubtract + uriListAndNumber.getNumber();
						}
					}
				}
				if (numberToSubtract == 0) {
					listResources.add(new UriAndUriListAndNumberListAndNumber(resource));
				} else {
					UriAndUriListAndNumberListAndNumber resourceTemp = 
					new UriAndUriListAndNumberListAndNumber(resource.getUri(), ListUriListAndNumberTemp, resource.getNumber() - numberToSubtract);
					listResources.add(resourceTemp);
				}	
			}
		}
		// Tri de la liste pour que les combinaisons les plus utilisées en priorité
		// L'ordre a pu être changé lors de la suppression de certaines classe pour une combinaison donnée
		Collections.sort(listResources, new UriAndUriListAndNumberListAndNumberComparator()); 

		return listResources;
	}

	public static String convertToSPARQLFilterProperties(ArrayList<UriAndNumber> listUriAndNumber) {
        StringBuilder filterClause = new StringBuilder();
        filterClause.append("FILTER ( ");

        for (UriAndNumber uriAndNumber : listUriAndNumber) {
            filterClause.append( " ?property = <" + uriAndNumber.getUri() + "> ||" );
        }

        if (!listUriAndNumber.isEmpty()) {
            // Supprimer le dernier "||" si la liste n'est pas vide
            filterClause.delete(filterClause.length() - 3, filterClause.length());
        }

        filterClause.append( " ) " );

        return filterClause.toString();
    }

	public static String convertToSPARQLFilterClasses(ArrayList<UriAndNumber> listUriAndNumber) {
        StringBuilder filterClause = new StringBuilder();
        filterClause.append("FILTER ( ");

        for (UriAndNumber uriAndNumber : listUriAndNumber) {
            filterClause.append( " ?subjectClass = <" + uriAndNumber.getUri() + "> ||" );
        }

        if (!listUriAndNumber.isEmpty()) {
            // Supprimer le dernier "||" si la liste n'est pas vide
            filterClause.delete(filterClause.length() - 3, filterClause.length());
        }

        filterClause.append( " ) " );

        return filterClause.toString();
    }


	static class UriAndUriListAndNumberListAndNumberComparator implements java.util.Comparator<UriAndUriListAndNumberListAndNumber> {
		@Override
		public int compare(UriAndUriListAndNumberListAndNumber a, UriAndUriListAndNumberListAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}