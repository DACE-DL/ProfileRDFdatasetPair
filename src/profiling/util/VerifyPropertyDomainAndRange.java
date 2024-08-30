package profiling.util;

import java.io.IOException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.OWL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.ArrayList;

public class VerifyPropertyDomainAndRange {
	
	// On passe par les fichiers du serveur en fournissant une liste de noms de fichiers contenant des requettes
	public static ArrayList<UriAndX2UriAndUriListAndUriListAndUriList> makeList(Model model) throws JsonParseException, JsonMappingException, IOException {

		ArrayList<UriAndX2UriAndUriListAndUriListAndUriList> listPropertyDomainAndRange = new ArrayList<UriAndX2UriAndUriListAndUriListAndUriList>();
        
		listPropertyDomainAndRange = listPropertiesWithDomainAndRange(model);

        return listPropertyDomainAndRange;
	}
	

    public static ArrayList<UriAndX2UriAndUriListAndUriListAndUriList> listPropertiesWithDomainAndRange(Model model) {
        ArrayList<UriAndX2UriAndUriListAndUriListAndUriList> listPropertyDomainAndRange = new ArrayList<UriAndX2UriAndUriListAndUriListAndUriList>();

        // Create Dataset from Model
        Dataset dataset = DatasetFactory.create(model);

        // Define SPARQL query to retrieve properties with their domain and range
        String queryString = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
            "SELECT ?property ?domain ?range WHERE { " +
            "  OPTIONAL { ?property rdfs:domain ?domain } . " +
            "  OPTIONAL { ?property rdfs:range ?range } " +
            "}";

        // Execute SPARQL query
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
            ResultSet results = qexec.execSelect();

            UriAndUriListAndUriListAndUriList nullInfo = new UriAndUriListAndUriListAndUriList();
            nullInfo.setUri(new Uri(""));
            nullInfo.setOneOf(new UriList());
            nullInfo.setUnionOf(new UriList());
            nullInfo.setIntersectionOf(new UriList());
            
            // Process each result
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode property = soln.get("property");
                RDFNode domain = soln.get("domain");
                RDFNode range = soln.get("range");

                UriAndX2UriAndUriListAndUriListAndUriList propertyDomainAndRange = new UriAndX2UriAndUriListAndUriListAndUriList();

                if (property != null) {
                    propertyDomainAndRange.setUri(new Uri(property.toString()));
                    
                    if (domain != null) {
                        if (domain.isAnon()) {
                            handleAnonymousDomain(domain, dataset, propertyDomainAndRange);
                        } else {
                            UriAndUriListAndUriListAndUriList domainInfo = new UriAndUriListAndUriListAndUriList();
                            domainInfo.setUri(new Uri(domain.toString()));
                            domainInfo.setOneOf(new UriList());
                            domainInfo.setUnionOf(new UriList());
                            domainInfo.setIntersectionOf(new UriList());
                            propertyDomainAndRange.setDomain(domainInfo);
                        }
                    } else {
                        propertyDomainAndRange.setDomain(nullInfo);
                    }

                    if (range != null) {
                        if (range.isAnon()) {
                            handleAnonymousRange(range, dataset, propertyDomainAndRange);
                        } else {
                            UriAndUriListAndUriListAndUriList rangeInfo = new UriAndUriListAndUriListAndUriList();
                            rangeInfo.setUri(new Uri(range.toString()));
                            rangeInfo.setOneOf(new UriList());
                            rangeInfo.setUnionOf(new UriList());
                            rangeInfo.setIntersectionOf(new UriList());
                            propertyDomainAndRange.setRange(rangeInfo);
                        }   
                    } else {
                        propertyDomainAndRange.setRange(nullInfo);
                    }
        
                    listPropertyDomainAndRange.add(propertyDomainAndRange);
                }    
            }
        }

        return listPropertyDomainAndRange;
    }

    private static void handleAnonymousDomain(RDFNode domain, Dataset dataset, UriAndX2UriAndUriListAndUriListAndUriList propertyDomainAndRange) {
        // Check for owl:oneOf, owl:unionOf, owl:intersectionOf
        handleOwlListDomain(domain, OWL.oneOf, propertyDomainAndRange, dataset);
        handleOwlListDomain(domain, OWL.unionOf, propertyDomainAndRange, dataset);
        handleOwlListDomain(domain, OWL.intersectionOf, propertyDomainAndRange, dataset);
    }

    private static void handleAnonymousRange(RDFNode range, Dataset dataset, UriAndX2UriAndUriListAndUriListAndUriList propertyDomainAndRange) {
        // Check for owl:oneOf, owl:unionOf, owl:intersectionOf
        handleOwlListRange(range, OWL.oneOf, propertyDomainAndRange, dataset);
        handleOwlListRange(range, OWL.unionOf, propertyDomainAndRange, dataset);
        handleOwlListRange(range, OWL.intersectionOf, propertyDomainAndRange, dataset);
    }

	private static void handleOwlListDomain(RDFNode domain, Property property, UriAndX2UriAndUriListAndUriListAndUriList propertyDomainAndRange, Dataset dataset) {
        // Define SPARQL query to retrieve the list for the property
        String queryString = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
            "SELECT ?list WHERE { " +
            "  ?domain <" + property.getURI() + "> ?list " +
            "}";

        // Create QuerySolutionMap to bind the blank node to the query variable
        QuerySolutionMap initialBindings = new QuerySolutionMap();
        initialBindings.add("domain", domain);

        // Execute SPARQL query for the anonymous node with initial bindings
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset, initialBindings)) {
            ResultSet results = qexec.execSelect();

            // Add results to listContainer
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode listNode = soln.get("list");

                if (listNode != null && listNode.canAs(RDFList.class)) {
                    RDFList rdfList = listNode.as(RDFList.class);
                    UriAndUriListAndUriListAndUriList domainInfo = new UriAndUriListAndUriListAndUriList();
                    
                    if (property.getURI() == OWL.oneOf.getURI()) {
                        domainInfo.setUri(new Uri(""));
                        domainInfo.setOneOf(new UriList(rdfList));
                        domainInfo.setUnionOf(new UriList());
                        domainInfo.setIntersectionOf(new UriList());
                    } 
                    if (property.getURI() == OWL.unionOf.getURI()) {
                        domainInfo.setUri(new Uri(""));
                        domainInfo.setOneOf(new UriList());
                        domainInfo.setUnionOf(new UriList(rdfList));
                        domainInfo.setIntersectionOf(new UriList());
                    } 
                    if (property.getURI() == OWL.intersectionOf.getURI()) {
                        domainInfo.setUri(new Uri(""));
                        domainInfo.setOneOf(new UriList());
                        domainInfo.setUnionOf(new UriList());
                        domainInfo.setIntersectionOf(new UriList(rdfList));
                    } 
                    propertyDomainAndRange.setDomain(domainInfo);
                }
            }
        }
    }
    private static void handleOwlListRange(RDFNode range, Property property, UriAndX2UriAndUriListAndUriListAndUriList propertyDomainAndRange, Dataset dataset) {
        // Define SPARQL query to retrieve the list for the property
        String queryString = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
            "SELECT ?list WHERE { " +
            "  ?range <" + property.getURI() + "> ?list " +
            "}";

        // Create QuerySolutionMap to bind the blank node to the query variable
        QuerySolutionMap initialBindings = new QuerySolutionMap();
        initialBindings.add("range", range);

        // Execute SPARQL query for the anonymous node with initial bindings
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset, initialBindings)) {
            ResultSet results = qexec.execSelect();

            // Add results to listContainer
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode listNode = soln.get("list");

                if (listNode != null && listNode.canAs(RDFList.class)) {
                    
                    RDFList rdfList = listNode.as(RDFList.class);
                    UriAndUriListAndUriListAndUriList rangeInfo = new UriAndUriListAndUriListAndUriList();
                    
                    if (property.getURI() == OWL.oneOf.getURI()) {
                        rangeInfo.setUri(new Uri(""));
                        rangeInfo.setOneOf(new UriList(rdfList));
                        rangeInfo.setUnionOf(new UriList());
                        rangeInfo.setIntersectionOf(new UriList());
                    } 
                    if (property.getURI() == OWL.unionOf.getURI()) {
                        rangeInfo.setUri(new Uri(""));
                        rangeInfo.setOneOf(new UriList());
                        rangeInfo.setUnionOf(new UriList(rdfList));
                        rangeInfo.setIntersectionOf(new UriList());
                    } 
                    if (property.getURI() == OWL.intersectionOf.getURI()) {
                        rangeInfo.setUri(new Uri(""));
                        rangeInfo.setOneOf(new UriList());
                        rangeInfo.setUnionOf(new UriList());
                        rangeInfo.setIntersectionOf(new UriList(rdfList));
                    } 
                    propertyDomainAndRange.setRange(rangeInfo);
                }
            }
        }
    }
}