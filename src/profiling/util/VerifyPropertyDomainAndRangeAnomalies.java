package profiling.util;

import java.io.IOException;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.ArrayList;

public class VerifyPropertyDomainAndRangeAnomalies {
	
	public static ArrayList<UriAndStringListAndStringList> makeList(Model model, ArrayList<UriAndX2UriAndUriListAndUriListAndUriList> listPropertyDomainAndRange) throws JsonParseException, JsonMappingException, IOException {

		ArrayList<UriAndStringListAndStringList> listPropertyDomainAndRangeAnomalies = new ArrayList<UriAndStringListAndStringList>();
        
		listPropertyDomainAndRangeAnomalies = listPropertiesWithDomainAndRange(model, listPropertyDomainAndRange);

        return listPropertyDomainAndRangeAnomalies;
	}
	

    public static ArrayList<UriAndStringListAndStringList> listPropertiesWithDomainAndRange(Model model, ArrayList<UriAndX2UriAndUriListAndUriListAndUriList> listPropertyDomainAndRange) {
        ArrayList<UriAndStringListAndStringList> listPropertyDomainAndRangeAnomalies = new ArrayList<UriAndStringListAndStringList>();

        // Create Dataset from Model
        Dataset dataset = DatasetFactory.create(model);
        

        listPropertyDomainAndRange.forEach((property) -> {
            UriAndStringListAndStringList anomalies = new UriAndStringListAndStringList();
            ArrayList<String> subjectAnomalies = new ArrayList<String>();
            ArrayList<String> objectAnomalies = new ArrayList<String>();
            anomalies.setUri(property.getUri().toString());
            // On vérifie qu'il y a un domaine déclaré
            if (property.getDomain().getUri().toString() != "" ||
                property.getDomain().getOneOf().size() > 0 ||
                property.getDomain().getUnionOf().size() > 0 ||
                property.getDomain().getIntersectionOf().size() > 0
                ) {
                   // Define SPARQL query to retrieve properties with their domain and range
                String queryString = 
                " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                " SELECT DISTINCT ?subject WHERE { " +
                " ?subject <" + property.getUri() + "> ?object   . " +
                " }";

                // Execute SPARQL query
                Query query = QueryFactory.create(queryString);
                try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
                    ResultSet results = qexec.execSelect();
                    if (results.hasNext()) {
                        // Process each result
                        while (results.hasNext()) {
                            QuerySolution qSol = results.nextSolution();
                            RDFNode subject = qSol.get("subject");
                            if (subject.isAnon()) {
                                subjectAnomalies.add("Blank node");
                            }
                            else if (property.getDomain().getUri().toString() != "" ) {
                                if (!isOfType(subject, property.getDomain().getUri().toString(), dataset)) {
                                    subjectAnomalies.add(subject.toString());
                                }
                            } 
                            else if (property.getDomain().getOneOf().size() > 0 ) {
                                if (!isInList(subject, property.getDomain().getOneOf())) {
                                    subjectAnomalies.add(subject.toString());
                                }
                            }
                            else if (property.getDomain().getUnionOf().size() > 0 ) {
                                if (!isInUnion(subject, property.getDomain().getUnionOf(), dataset)) {
                                    subjectAnomalies.add(subject.toString());
                                }
                            }
                            else if (property.getDomain().getIntersectionOf().size() > 0 ) {
                                if (!isInIntersection(subject, property.getDomain().getIntersectionOf(), dataset)) {
                                    subjectAnomalies.add(subject.toString());
                                }
                            }
                        }  
                        
                    } else { // Aucun triplets trouvé avec la propriété
                        // System.out.println("Pas de triplets !");
                    }   
                }
            }
            // On vérifie qu'il y a un range déclaré
            if (property.getRange().getUri().toString() != "" ||
                property.getRange().getOneOf().size() > 0 ||
                property.getRange().getUnionOf().size() > 0 ||
                property.getRange().getIntersectionOf().size() > 0
                ) {
                // Define SPARQL query to retrieve properties with their domain and range
                String queryString = 
                " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                " SELECT DISTINCT ?object WHERE { " +
                " ?subject <" + property.getUri() + "> ?object   . " +
                " }";

                // Execute SPARQL query
                Query query = QueryFactory.create(queryString);
                try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
                    ResultSet results = qexec.execSelect();
                    if (results.hasNext()) {
                        // Process each result
                        while (results.hasNext()) {
                            QuerySolution qSol = results.nextSolution();
                            RDFNode object = qSol.get("object");
                            if (object.isAnon()) {
                                objectAnomalies.add("Blank node");
                            }
                            else if (property.getRange().getUri().toString() != "" ) {
                                if (!isOfType(object, property.getRange().getUri().toString(), dataset)) {
                                    objectAnomalies.add(object.toString());
                                }
                            } 
                            else if (property.getRange().getOneOf().size() > 0 ) {
                                if (!isInList(object, property.getRange().getOneOf())) {
                                    objectAnomalies.add(object.toString());
                                }
                            }
                            else if (property.getRange().getUnionOf().size() > 0 ) {
                                if (!isInUnion(object, property.getRange().getUnionOf(), dataset)) {
                                    objectAnomalies.add(object.toString());
                                }
                            }
                            else if (property.getRange().getIntersectionOf().size() > 0 ) {
                                if (!isInIntersection(object, property.getRange().getIntersectionOf(), dataset)) {
                                    objectAnomalies.add(object.toString());
                                }
                            }
                        }  
                       
                    } else { // Aucun triplets trouvé avec la propriété
                        // System.out.println("Pas de triplets !");
                    }   
                }
            } 

            if (!objectAnomalies.isEmpty() || !subjectAnomalies.isEmpty()) {
                anomalies.setListDomainAnomalies(subjectAnomalies);
                anomalies.setListRangeAnomalies(objectAnomalies);
                listPropertyDomainAndRangeAnomalies.add(anomalies);
            }    

        });     

        return listPropertyDomainAndRangeAnomalies;
    }
    private static boolean isOfType(RDFNode node, String typeUri, Dataset dataset) {
        if (node.isLiteral()) {
            Literal literal = node.asLiteral();
            String literalDatatypeUri = literal.getDatatypeURI();
            return literalDatatypeUri.equals(typeUri);
        }

        String queryString =
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
            "ASK WHERE { " +
            "  <" + node.toString() + "> rdf:type <" + typeUri + "> " +
            "}";
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
            if (qexec.execAsk()) {
                return true;
            }
        }

        // Vérifier si le type de node est une sous-classe ou une sous-sous-classe de typeUri
        queryString =
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
            "ASK WHERE { " +
            "  <" + node.toString() + "> rdf:type/rdfs:subClassOf* <" + typeUri + "> " +
            "}";
        query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
            return qexec.execAsk();
        }
    }

    private static boolean isInList(RDFNode node, UriList list) {
        return list.getUriList().contains(new Uri(node.toString()));
    }

    private static boolean isInUnion(RDFNode node, UriList unionOf, Dataset dataset) {
        for (Uri uri : unionOf.getUriList()) {
            if (isOfType(node, uri.getUri(), dataset)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInIntersection(RDFNode node, UriList intersectionOf, Dataset dataset) {
        for (Uri uri : intersectionOf.getUriList()) {
            if (!isOfType(node, uri.getUri(), dataset)) {
                return false;
            }
        }
        return true;
    }
}