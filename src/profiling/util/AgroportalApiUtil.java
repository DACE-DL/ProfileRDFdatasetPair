package profiling.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class AgroportalApiUtil {
	
	static final String REST_URL = "http://data.agroportal.lirmm.fr/";
    static final String API_KEY = "5592185f-9951-49c4-903a-135e68aaa6f3";
    static final ObjectMapper mapper = new ObjectMapper();
    static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
    
    public static void main(String[] args) throws JsonProcessingException  {
    	Instant start0 = Instant.now();	
    	ArrayList<String> ListResources = new ArrayList<String>();
    	// Recherche des ressource par rapport au terme
    	//ListResources = NcboApiUtil.resourceAndDecendants("Fagales");
    	//ListResources = AgroportalApiUtil.resourceAndDecendants("river");
    	ListResources = AgroportalApiUtil.resourceAndDecendants("Juglandaceae");
    	
        
		for (String resource : ListResources) {
            System.out.println(resource);
        }
		Instant end0 = Instant.now();
    	System.out.println("Durée d'exécution Totale: " + Duration.between(start0, end0).getSeconds() + " secondes");    
    }

    public static ArrayList<String> resourceAndDecendants(String term) throws JsonProcessingException {
    	
    	ArrayList<String> ListResources = new ArrayList<String>();
    	ArrayList<String> ListDescendants = new ArrayList<String>();
    	
    	// Première recherche par rapport au terme
        JsonNode searchResult = jsonToNode(get(REST_URL + "/search?q=" + term));
            
            JsonNode searchResultCollection = searchResult.path("collection");
            if (searchResultCollection.isArray()) {
               //If this node an Arrray?
            }

            System.out.println(searchResult);
            System.out.println(searchResultCollection);
            if (searchResult.isArray()) {
               //If this node an Arrray?
               System.out.println("searchResultArrray");
            }
            if (searchResultCollection.isArray()) {
                //If this node an Arrray?
            	System.out.println("searchResultCollectionArrray");
             }
            
            for (JsonNode node : searchResultCollection) {
                String id = node.path("@id").asText();
                String descendants = "";
                JsonNode linksNode = node.path("links");
                if (linksNode.isMissingNode()) {
                   //si "links" node manquant
                } else {
                   descendants = linksNode.path("parents").asText();
                   //missing node, just return empty string
                   System.out.println(descendants);
                };
                if (!ListResources.contains(id)) { 
                	// Récupération des resources
                	ListResources.add(id);
                	// Récupération des liens vers les descendants
                	ListDescendants.add(descendants);
                }
            }
   
        
        // On traite chaque lien vers les descendants
        for (String descendant : ListDescendants) {
        	JsonNode searchResultDescendants = jsonToNode(get(descendant) + "&display_context=false" +
        			"&display_links=false"	);
        //System.out.println(writer.writeValueAsString(searchResultDescendants));
    
        	if (!searchResultDescendants.isEmpty(null)) {
        		// A partir de la page retournée, on obtient le lien hypermédia vers la page suivante
        		String nextPage = searchResultDescendants.get("links").get("nextPage").asText();
        		// Itération sur les pages disponibles
        		while (nextPage.length() != 0) {
        			for (JsonNode cls : searchResultDescendants.get("collection")) {
        				if (!cls.get("@id").isNull()) {
        					if (!ListResources.contains(cls.get("@id").asText())) { 
        	                	ListResources.add(cls.get("@id").asText());
        					}
        				}
        			}

        			if (!searchResultDescendants.get("links").get("nextPage").isNull()) {
        				nextPage = searchResultDescendants.get("links").get("nextPage").asText();
        				searchResultDescendants = jsonToNode(get(nextPage));
        			} else {
        				nextPage = "";
        			}
        		}
        	}

        }   
       
        /*for (String resource : ListResources) {
            System.out.println(writer.writeValueAsString(resource));
        }*/
		return ListResources;
        
    }

    private static JsonNode jsonToNode(String json) {
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    private static String get(String urlToGet) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + API_KEY);
            conn.setRequestProperty("Accept", "application/json");
           // conn.setRequestProperty("display_context", "false");
           // conn.setRequestProperty("display_links", "false");
            rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	

}