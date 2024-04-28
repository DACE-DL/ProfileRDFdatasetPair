package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListMostUsedPropertyWithClassDomain {
	
	// Création d'une liste des propriétés les plus utilisées avec datatypes et classes range.
	public static ArrayList<UriAndUriListAndNumberListAndNumber> makeList(Model model, ArrayList<UriAndNumber> listMostUsedProperty, ArrayList<UriAndNumber> listClassMostUsed) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriListAndNumberListAndNumber> ListResources = new ArrayList<UriAndUriListAndNumberListAndNumber>();
		
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
							convertToSPARQLFilterProperties(listMostUsedProperty) +
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
				ListResources.add(UriAndUriListAndNumberListAndNumber) ;	
				
			}
		}
		
		return ListResources;
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
}