package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListMostUsedPropertyType {
	
	// Création liste des types des propriétés les plus utilisées.
	public static ArrayList<UriAndUri> makeList(Model model, ArrayList<UriAndNumber> listMostUsedProperty) {
		
		String prefix = ProfilingConf.queryPrefix;
		String owl = ProfilingConf.owl;
		String rdfs = ProfilingConf.rdfs;


		ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
	
		Query query = QueryFactory.create(prefix + 
			"SELECT ?property ?type WHERE { " +
			" ?property rdf:type ?type ." +
			" FILTER (?type = rdf:property || ?type = owl:ObjectProperty || ?type = owl:DatatypeProperty || ?type = owl:AnnotationProperty) " +
			convertToSPARQLFilter(listMostUsedProperty) +
			" } ORDER BY ?property "
		);
		
 		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new UriAndUri(querySolution.getResource("property").toString(), querySolution.getResource("type").toString()));
			}
		}
		// Five annotation properties are predefined by OWL, namely:
		// owl:versionInfo
		// rdfs:label
		// rdfs:comment
		// rdfs:seeAlso
		// rdfs:isDefinedBy
		ListResources.add(new UriAndUri( owl + "backwardCompatibleWith", owl + "AnnotationProperty"));
		ListResources.add(new UriAndUri( owl + "deprecated", owl + "AnnotationProperty"));
		ListResources.add(new UriAndUri( owl + "incompatibleWith", owl + "AnnotationProperty"));
		ListResources.add(new UriAndUri( owl + "priorVersion", owl + "AnnotationProperty"));
		ListResources.add(new UriAndUri( owl + "versionInfo", owl + "AnnotationProperty"));
		ListResources.add(new UriAndUri( rdfs + "comment", owl + "AnnotationProperty"));
		ListResources.add(new UriAndUri( rdfs + "isDefinedBy", owl + "AnnotationProperty"));
		ListResources.add(new UriAndUri( rdfs + "label", owl + "AnnotationProperty"));
		ListResources.add(new UriAndUri( rdfs + "seeAlso", owl + "AnnotationProperty"));
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