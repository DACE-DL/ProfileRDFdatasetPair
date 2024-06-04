package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListMostUsedPropertyWithDatatypeAndClassRange {
	static boolean first = false; 
	// Création d'une liste des propriétés les plus utilisées avec datatypes et classes range.
	public static ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> makeList(Model model, ArrayList<UriAndNumber> listMostUsedProperty) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> ListResources = new ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber>();
		
		// System.out.println("Nombre de propriétés: " + listMostUsedProperty.size()) ;

		for (UriAndNumber uriAndNumber : listMostUsedProperty) {
		// Instant start0 = Instant.now();	
            
		String property = uriAndNumber.getUri();

		Query query = QueryFactory.create(prefix + 
		
		" SELECT ?property " +
		" (GROUP_CONCAT(DISTINCT ?datatypeListAndCount; separator=\"*\") AS ?datatypeListAndCountList)  "	+ 
		" (GROUP_CONCAT(DISTINCT ?classListAndCount; separator=\"*\") AS ?classListAndCountList)  "	+ 
		" (SUM(?propertyUsage) AS ?propertyCount) " +
		" WHERE {      " +
		" 	SELECT ?property " +
		"   ((CONCAT(?datatypeList, ';', STR(?propertyUsage))) AS ?datatypeListAndCount)  "	+ 
		" 	((CONCAT(?classList, ';', STR(?propertyUsage))) AS ?classListAndCount)  "	+ 
		"   ?propertyUsage  " +
		" 	WHERE {      " +
				"SELECT ?property (COUNT(?property) AS ?propertyUsage) ?datatypeList ?classList" +
				" WHERE { " +
					"SELECT ?s ?property ?o " + 
					" (GROUP_CONCAT(DISTINCT ?datatype; separator=\"|\") AS ?datatypeList) (GROUP_CONCAT(DISTINCT ?class; separator=\"|\") AS ?classList) " +
					" WHERE { " +
						"SELECT ?s ?property ?o ?datatype ?class" +
						" WHERE { " +
							" BIND( '' AS ?defaultDatatype) " +
							" BIND( '' AS ?defaultClass) " +
							" BIND( '' AS ?defaultOrder) " +
							" ?s ?property ?o ." +
							" BIND (datatype(?o) AS ?objectDatatype) " +
							" OPTIONAL { "+
							" ?o rdf:type ?objectClass " +
							" } " +
							" BIND(COALESCE(?objectClass, ?defaultClass) AS ?class) " +
							" BIND(COALESCE(?objectDatatype, ?defaultDatatype) AS ?datatype) " +
							" BIND(COALESCE(?objectDatatype, ?objectClass , ?defaultOrder) AS ?order) " +
							" FILTER (?property = <" + property + "> )" +
							//convertToSPARQLFilter(listMostUsedProperty) +
						" } ORDER BY ?s ?property ?o ?order" +
					" } GROUP BY ?s ?property ?o " +
				" } GROUP BY ?property ?datatypeList ?classList" +
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

				ArrayList<UriListAndNumber> datatypeListAndNumberListTemp = new ArrayList<UriListAndNumber>();
				if (!(querySolution.getLiteral("datatypeListAndCountList")==null)) {
					String[] datatypeListAndCountList = querySolution.getLiteral("datatypeListAndCountList").getString().split("\\*");
					// En fait pour les datatypes il ne peux y avoir q'un seul datatype dans la liste	
					for (String datatypeListAndCount : datatypeListAndCountList) {
						String[] datatypeListAndCountString = datatypeListAndCount.split("\\;");
						Integer number = Integer.valueOf(datatypeListAndCountString[1]);
						if (!datatypeListAndCountString[0].equals("")) {
							ArrayList<Uri> uriList = new ArrayList<Uri>();
							String[] datatypeListString = datatypeListAndCountString[0].split("\\|");
							for (String datatypeString : datatypeListString) {
								Uri datatypeUri = new Uri(datatypeString);
								uriList.add(datatypeUri);
							}
							UriListAndNumber uriListAndNumber = new UriListAndNumber(uriList, number);
							datatypeListAndNumberListTemp.add(uriListAndNumber);
						}
					}	
				}
				ArrayList<UriListAndNumber> classListAndNumberListTemp = new ArrayList<UriListAndNumber>();
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
							classListAndNumberListTemp.add(uriListAndNumber);	
						}
					}
				}		
			
				// on nettoie les listes avec la régle des 1%.
				ArrayList<UriListAndNumber> datatypeListAndNumberList = new ArrayList<UriListAndNumber>();
				if (datatypeListAndNumberListTemp.size()<= 1) { // Il n'y a qu'une liste de classes pour la combinaison de propriétés, donc on sélectione 
				    datatypeListAndNumberList.addAll(datatypeListAndNumberListTemp);
				} else {
					first = true;
					Integer totalNumber = propertyNumber;
					Integer numberToSubtract = 0;
					for (UriListAndNumber uriListAndNumber : datatypeListAndNumberListTemp) {
						if (first) {
							datatypeListAndNumberList.add(new UriListAndNumber(uriListAndNumber));
							first = false;
						} else {
							// Si le nombre d'instances de la liste de classes suivante représente plus 
							//  de 1% de l'ensemble des instances des liste de classes déjà selectionnées pour une propriété donnée.
							if (((uriListAndNumber.getNumber() * 100) / totalNumber) > 1) {
								datatypeListAndNumberList.add(new UriListAndNumber(uriListAndNumber));
								totalNumber = totalNumber + uriListAndNumber.getNumber();
							} else {
								numberToSubtract = numberToSubtract + uriListAndNumber.getNumber();
							}
						}
					}
					if (numberToSubtract > 0) {
						propertyNumber = propertyNumber - numberToSubtract;
					}	
				}
				
				ArrayList<UriListAndNumber> classListAndNumberList = new ArrayList<UriListAndNumber>();
				if (classListAndNumberListTemp.size()<= 1) { // Il n'y a qu'une liste de classes pour la combinaison de propriétés, donc on sélectione 
				    classListAndNumberList.addAll(classListAndNumberListTemp);
				} else {
					first = true;
					Integer totalNumber = propertyNumber;
					Integer numberToSubtract = 0;
					for (UriListAndNumber uriListAndNumber : classListAndNumberListTemp) {
						if (first) {
							classListAndNumberList.add(new UriListAndNumber(uriListAndNumber));
							first = false;
						} else {
							// Si le nombre d'instances de la liste de classes suivante représente plus 
							//  de 1% de l'ensemble des instances des liste de classes déjà selectionnées pour une propriété donnée.
							if (((uriListAndNumber.getNumber() * 100) / totalNumber) > 1) {
								classListAndNumberList.add(new UriListAndNumber(uriListAndNumber));
								totalNumber = totalNumber + uriListAndNumber.getNumber();
							} else {
								numberToSubtract = numberToSubtract + uriListAndNumber.getNumber();
							}
						}
					}
					if (numberToSubtract > 0) {
						propertyNumber = propertyNumber - numberToSubtract;
					}	
				}
				
				UriAndUriListAndNumberListAndUriListAndNumberListAndNumber UriAndUriListAndNumberListAndUriListAndNumberListAndNumber = new UriAndUriListAndNumberListAndUriListAndNumberListAndNumber();
				UriAndUriListAndNumberListAndUriListAndNumberListAndNumber.setUri(uri);
				UriAndUriListAndNumberListAndUriListAndNumberListAndNumber.setUriListAndNumberList1(datatypeListAndNumberList);
				UriAndUriListAndNumberListAndUriListAndNumberListAndNumber.setUriListAndNumberList2(classListAndNumberList);
				UriAndUriListAndNumberListAndUriListAndNumberListAndNumber.setNumber(propertyNumber);
				ListResources.add(UriAndUriListAndNumberListAndUriListAndNumberListAndNumber) ;	
				
			}
			// Instant end0 = Instant.now();
        	// System.out.println("Running time for" + property + ": " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		}
		}
		
		return ListResources;
	}

	public static String convertToSPARQLFilter(ArrayList<UriAndNumber> listUriAndNumber) {
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
}