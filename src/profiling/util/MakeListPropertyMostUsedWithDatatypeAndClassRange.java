package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class MakeListPropertyMostUsedWithDatatypeAndClassRange {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber> makeList(Model model, String nameOfList) {
		
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber> ListResources = new ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber>();
		
		Query query = QueryFactory.create(prefix + 
		"SELECT ?property (SUM(?usage) AS ?propertyUsage) (GROUP_CONCAT(DISTINCT ?datatypeAndCount; separator=\"|\") AS ?datatypeAndCountList) (GROUP_CONCAT(DISTINCT ?classAndCount; separator=\"|\") AS ?classAndCountList) " +
		" WHERE { " +
			"SELECT ?property (COUNT(?s) AS ?usage) (CONCAT(STR( ?datatype ), ';', STR(COUNT(?datatype))) AS ?datatypeAndCount) (CONCAT(STR( ?class ), ';', STR(COUNT(?class))) AS ?classAndCount) " +
			" WHERE { " +
				" BIND( '' AS ?defaultDatatype) " +
				" BIND( '' AS ?defaultClass) " +
				" ?s ?property ?o ." +
				" BIND (datatype(?o) AS ?objectDatatype) " +
				" OPTIONAL { "+
				" ?o rdf:type ?objectClass " +
				" } " +
				" BIND(COALESCE(?objectClass, ?defaultClass) AS ?class) " +
				" BIND(COALESCE(?objectDatatype, ?defaultDatatype) AS ?datatype) " +
				" FILTER EXISTS { " +
				" dsp:listPropertyMostUsed rdf:rest*/rdf:first ?element ." +
				" ?element dsp:asURI ?property ." +
				" } " +	
				" " +
			" } GROUP BY ?property ?datatype ?class ORDER BY DESC (?usage) " +
		" } GROUP BY ?property ORDER BY DESC (?propertyUsage)"
		);			
 		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;

				Uri uri = new Uri();
				uri.setUri(querySolution.getResource("property").getURI());
				Integer propertyNumber = querySolution.getLiteral("?propertyUsage").getInt();

				ArrayList<UriAndNumber> ListDatatype = new ArrayList<UriAndNumber>();
				if (!(querySolution.getLiteral("datatypeAndCountList")==null)) {
					String[] datatypeAndCounts = querySolution.getLiteral("datatypeAndCountList").getString().split("\\|");
						
					for (String datatypeAndCount : datatypeAndCounts) {
						String[] datatypeCount = datatypeAndCount.split("\\;");
						Integer number = Integer.valueOf(datatypeCount[1]);
						if (!datatypeCount[0].equals("")) {
							UriAndNumber uriAndNumber = new UriAndNumber(datatypeCount[0], number);
							ListDatatype.add(uriAndNumber);
						}
					}	
				}
				ArrayList<UriAndNumber> ListClass = new ArrayList<UriAndNumber>();
				if (!(querySolution.getLiteral("classAndCountList")==null)) {
					String[] classAndCounts = querySolution.getLiteral("classAndCountList").getString().split("\\|");
						
					for (String classAndCount : classAndCounts) {
						String[] classCount = classAndCount.split("\\;");
						Integer number = Integer.valueOf(classCount[1]);
						if (!classCount[0].equals("")) {
							UriAndNumber uriAndNumber = new UriAndNumber(classCount[0], number);
							ListClass.add(uriAndNumber);	
						}
					}
				}		
			
				UriAndUriAndNumberListAndUriAndNumberListAndNumber uriAndUriAndNumberListAndUriAndNumberListAndNumber = new UriAndUriAndNumberListAndUriAndNumberListAndNumber();
				uriAndUriAndNumberListAndUriAndNumberListAndNumber.setUri(uri);
				uriAndUriAndNumberListAndUriAndNumberListAndNumber.setUriAndNumberList1(ListDatatype);
				uriAndUriAndNumberListAndUriAndNumberListAndNumber.setUriAndNumberList2(ListClass);
				uriAndUriAndNumberListAndUriAndNumberListAndNumber.setNumber(propertyNumber);
				ListResources.add(uriAndUriAndNumberListAndUriAndNumberListAndNumber) ;	
				
			}
		}
		
		return ListResources;
	}

}