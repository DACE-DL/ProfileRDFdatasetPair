package profiling.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class AgrovocApiUtil {

	static final String REST_URL = "https://agrovoc.uniroma2.it/agrovoc/rest/v1/";
	static final ObjectMapper mapper = new ObjectMapper();
	static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

	public static void main(String[] args) throws JsonProcessingException  {
		Instant start0 = Instant.now();	
		ArrayList<String> ListResources = new ArrayList<String>();
		/*ListResources = resourcesWithTerm("Juglans");
		for (String resource : ListResources) {
			System.out.println(writer.writeValueAsString(resource));
		}*/
		ListResources = resourcesAndDecendants("Juglandaceae");
		//ListResources = resourcesAndDecendants("Juglans");
		System.out.println("Decendants");
		for (String resource : ListResources) {
			System.out.println(writer.writeValueAsString(resource));
		}
		
		Instant end0 = Instant.now();
    	System.out.println("Durée d'exécution Totale: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));    
    	
    	Instant start2 = Instant.now();	
    	ListResources = resourcesAndExactMatch("Juglandaceae");
		//ListResources = resourcesAndDecendants("Juglans");
    	System.out.println("ExactMatch");
		for (String resource : ListResources) {
			System.out.println(writer.writeValueAsString(resource));
		}
		
		Instant end2 = Instant.now();
    	System.out.println("Durée d'exécution Totale: " + Duration.between(start2, end2).getSeconds() + " secondes");    

    	
    	Instant start3 = Instant.now();	
    	ListResources = resourcesAndCloseMatch("Juglandaceae");
    	System.out.println("CloseMatch");
		for (String resource : ListResources) {
			System.out.println(writer.writeValueAsString(resource));
		}
		
		Instant end3 = Instant.now();
    	System.out.println("Durée d'exécution Totale: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));    

    	Instant start4 = Instant.now();	
    	ListResources = resourcesAndNarrower("Juglandaceae");
    	System.out.println("Narrower");
		for (String resource : ListResources) {
			System.out.println(writer.writeValueAsString(resource));
		}
		
		Instant end4 = Instant.now();
    	System.out.println("Durée d'exécution Totale: " + Duration.between(start4, end4).getSeconds() + " secondes");    

    	Instant start5 = Instant.now();	
    	ListResources = resourcesAndBroader("Juglandaceae");
    	System.out.println("Broader");
		for (String resource : ListResources) {
			System.out.println(writer.writeValueAsString(resource));
		}
		
		Instant end5 = Instant.now();
    	System.out.println("Durée d'exécution Totale: " + Duration.between(start5, end5).getSeconds() + " secondes");    

    	
    	/*
		 * Instant start1 = Instant.now(); ListResources = resourcesWithTerm("river*");
		 * for (String resource : ListResources) {
		 * System.out.println(writer.writeValueAsString(resource)); }
		 * 
		 * Instant end1 = Instant.now(); System.out.println("Durée d'exécution Totale: "
		 * + Duration.between(start1, end1).getSeconds() + " secondes");
		 */
	
	}


	public static ArrayList<String> resourcesWithTerm(String term) throws JsonProcessingException {

		ArrayList<String> ListResources = new ArrayList<String>();
		// Recherche par rapport au terme
		JsonNode searchResult = jsonToNode(get(REST_URL + "/search/?query=" + term));
		//System.out.println(searchResult);
		JsonNode searchResultCollection = searchResult.path("results");
		if (searchResultCollection.isArray()) {
			//If this node an Arrray?
		}
		for (JsonNode node : searchResultCollection) {
			String uri= node.path("uri").asText();
			// Récupération des resources
			if (!ListResources.contains(uri)) { 
				ListResources.add(uri);
			}
		}
		return ListResources;
	}


	public static ArrayList<String> resourcesAndDecendants(String term) throws JsonProcessingException {

		ArrayList<String> ListResources = new ArrayList<String>();
		ArrayList<String> ListDescendantsResources = new ArrayList<String>();
		
		// Recherche par rapport au terme
		//System.out.println(REST_URL);
		//System.out.println(get(REST_URL + "/search/?query=" + term));
		JsonNode searchResult = jsonToNode(get(REST_URL + "/search/?query=" + term));

		JsonNode searchResultCollection = searchResult.path("results");
		if (searchResultCollection.isArray()) {
			//If this node an Arrray?
			//System.out.println(searchResultCollection);
		}

		for (JsonNode node : searchResultCollection) {
			String uri= node.path("uri").asText();
			// Récupération des resources
			if (!ListResources.contains(uri)) { 
				ListResources.add(uri);
			}
		}

		ListDescendantsResources = otherResourcesRelatedToResource(ListResources);
		
		return ListDescendantsResources;

	}
	
	public static ArrayList<String> resourcesAndBroader(String term) throws JsonProcessingException {

		ArrayList<String> ListResources = new ArrayList<String>();
		ArrayList<String> ListDescendantsResources = new ArrayList<String>();
		
		// Recherche par rapport au terme
		//System.out.println(REST_URL);
		//System.out.println(get(REST_URL + "/search/?query=" + term));
		JsonNode searchResult = jsonToNode(get(REST_URL + "/search/?query=" + term));

		JsonNode searchResultCollection = searchResult.path("results");
		if (searchResultCollection.isArray()) {
			//If this node an Arrray?
			//System.out.println(searchResultCollection);
		}

		for (JsonNode node : searchResultCollection) {
			String uri= node.path("uri").asText();
			// Récupération des resources
			if (!ListResources.contains(uri)) { 
				ListResources.add(uri);
			}
		}

		ListDescendantsResources = otherResourcesRelatedToResourceWithBroader(ListResources);
		
		return ListDescendantsResources;

	}
	
	
	public static ArrayList<String> resourcesAndNarrower(String term) throws JsonProcessingException {

		ArrayList<String> ListResources = new ArrayList<String>();
		ArrayList<String> ListDescendantsResources = new ArrayList<String>();
		
		// Recherche par rapport au terme
		//System.out.println(REST_URL);
		//System.out.println(get(REST_URL + "/search/?query=" + term));
		JsonNode searchResult = jsonToNode(get(REST_URL + "/search/?query=" + term));

		JsonNode searchResultCollection = searchResult.path("results");
		if (searchResultCollection.isArray()) {
			//If this node an Arrray?
			//System.out.println(searchResultCollection);
		}

		for (JsonNode node : searchResultCollection) {
			String uri= node.path("uri").asText();
			// Récupération des resources
			if (!ListResources.contains(uri)) { 
				ListResources.add(uri);
			}
		}

		ListDescendantsResources = otherResourcesRelatedToResourceWithNarrower(ListResources);
		
		return ListDescendantsResources;

	}
	
	public static ArrayList<String> NarrowerToURI(String uri) throws JsonProcessingException {

		ArrayList<String> ListResources = new ArrayList<String>();
		ArrayList<String> ListDescendantsResources = new ArrayList<String>();

		ListResources.add(uri);

		ListDescendantsResources = otherResourcesRelatedToResourceWithNarrower(ListResources);
		
		return ListDescendantsResources;

	}
	public static ArrayList<String> resourcesAndCloseMatch(String term) throws JsonProcessingException {

		ArrayList<String> ListResources = new ArrayList<String>();
		ArrayList<String> ListDescendantsResources = new ArrayList<String>();
		
		// Recherche par rapport au terme
		//System.out.println(REST_URL);
		//System.out.println(get(REST_URL + "/search/?query=" + term));
		JsonNode searchResult = jsonToNode(get(REST_URL + "/search/?query=" + term));

		JsonNode searchResultCollection = searchResult.path("results");
		if (searchResultCollection.isArray()) {
			//If this node an Arrray?
			//System.out.println(searchResultCollection);
		}

		for (JsonNode node : searchResultCollection) {
			String uri= node.path("uri").asText();
			// Récupération des resources
			if (!ListResources.contains(uri)) { 
				ListResources.add(uri);
			}
		}

		ListDescendantsResources = otherResourcesRelatedToResourceWithCloseMatch(ListResources);
		
		return ListDescendantsResources;

	}
	
	public static ArrayList<String> resourcesAndExactMatch(String term) throws JsonProcessingException {

		ArrayList<String> ListResources = new ArrayList<String>();
		ArrayList<String> ListDescendantsResources = new ArrayList<String>();
		
		// Recherche par rapport au terme
		//System.out.println(REST_URL);
		//System.out.println(get(REST_URL + "/search/?query=" + term));
		JsonNode searchResult = jsonToNode(get(REST_URL + "/search/?query=" + term));

		JsonNode searchResultCollection = searchResult.path("results");
		if (searchResultCollection.isArray()) {
			//If this node an Arrray?
			//System.out.println(searchResultCollection);
		}

		for (JsonNode node : searchResultCollection) {
			String uri= node.path("uri").asText();
			// Récupération des resources
			if (!ListResources.contains(uri)) { 
				ListResources.add(uri);
			}
		}

		ListDescendantsResources = otherResourcesRelatedToResourceWithExactMatch(ListResources);
		
		return ListDescendantsResources;

	}
	
	
	public static ArrayList<String> descendantsResources(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		// On traite chaque ressource obtenue par la recherche textuelle
		for (String resource : listResources) {
			if (!newlistResources.contains(resource)) { 
            	newlistResources.add(resource);
			}		
			JsonNode searchResultResource = jsonToNode(get(REST_URL +"/data/?uri=" + resource));
			JsonNode graphNode = searchResultResource.path("graph");
			if (graphNode.isMissingNode()) {
				//si node manquant
			} else {
				if(graphNode.isArray()){
					ArrayNode arrayNode = (ArrayNode) graphNode;
					for(int i = 0; i < arrayNode.size(); i++) {
						JsonNode arrayElement = arrayNode.get(i);
						Iterator<String> fieldNames = arrayElement.fieldNames();
						while(fieldNames.hasNext()) {
							String fieldName = fieldNames.next();
							String field = arrayElement.get(fieldName).asText();
						    if (fieldName.contentEquals("uri") & field.contentEquals(resource)) {
								JsonNode narrowerNode = arrayElement.path("narrower");
								if(narrowerNode.isObject()){
							        Iterator<String> fieldNamesnarrowerNode = narrowerNode.fieldNames();
							        while(fieldNamesnarrowerNode.hasNext()) {
							            String fieldNamenarrowerNode = fieldNamesnarrowerNode.next();
							            String fieldnarrowerNode = narrowerNode.get(fieldNamenarrowerNode).asText();
							            if (!newlistResources.contains(fieldnarrowerNode)) { 
							            	newlistResources.add(fieldnarrowerNode);
										}				            
							        }
							    } else if(narrowerNode.isArray()){
							        ArrayNode arrayNodenarrowerNode = (ArrayNode) narrowerNode;
							        for(int ii = 0; ii < arrayNodenarrowerNode.size(); ii++) {
							            JsonNode arrayElementnarrowerNode = arrayNodenarrowerNode.get(ii);
							            String arrayElementnarrowerNodeUri = arrayElementnarrowerNode.path("uri").asText();
							            if (!newlistResources.contains(arrayElementnarrowerNodeUri)) { 
							            	newlistResources.add(arrayElementnarrowerNodeUri);
										}	
							        }
							    } 
								
								JsonNode closeMatchNode = arrayElement.path("closeMatch");
								if(closeMatchNode.isObject()){
							        Iterator<String> fieldNamescloseMatchNode = closeMatchNode.fieldNames();
							        while(fieldNamescloseMatchNode.hasNext()) {
							            String fieldNamecloseMatchNode = fieldNamescloseMatchNode.next();
							            String fieldcloseMatchNode = closeMatchNode.get(fieldNamecloseMatchNode).asText();
							            if (!newlistResources.contains(fieldcloseMatchNode)) { 
							            	newlistResources.add(fieldcloseMatchNode);
										}	
							        }
							    } else if(closeMatchNode.isArray()){
							        ArrayNode arrayNodecloseMatchNode = (ArrayNode) closeMatchNode;
							        for(int ii = 0; ii < arrayNodecloseMatchNode.size(); ii++) {
							            JsonNode arrayElementcloseMatchNode = arrayNodecloseMatchNode.get(ii);
							            String arrayElementcloseMatchNodeUri = arrayElementcloseMatchNode.path("uri").asText();
							            if (!newlistResources.contains(arrayElementcloseMatchNodeUri)) { 
							            	newlistResources.add(arrayElementcloseMatchNodeUri);
										}	
							        }
							    } 
								
								JsonNode exactMatchNode = arrayElement.path("exactMatch");
								if(exactMatchNode.isObject()){
							        Iterator<String> fieldNamesexactMatchNode = exactMatchNode.fieldNames();
							        while(fieldNamesexactMatchNode.hasNext()) {
							            String fieldNameexactMatchNode = fieldNamesexactMatchNode.next();
							            String fieldexactMatchNode = exactMatchNode.get(fieldNameexactMatchNode).asText();
							            if (!newlistResources.contains(fieldexactMatchNode)) { 
							            	newlistResources.add(fieldexactMatchNode);
										}	
							        }
							    } else if(exactMatchNode.isArray()){
							        ArrayNode arrayNodeexactMatchNode = (ArrayNode) exactMatchNode;
							        for(int ii = 0; ii < arrayNodeexactMatchNode.size(); ii++) {
							            JsonNode arrayElementexactMatchNode = arrayNodeexactMatchNode.get(ii);
							            String arrayElementexactMatchNodeUri = arrayElementexactMatchNode.path("uri").asText();
							            if (!newlistResources.contains(arrayElementexactMatchNodeUri)) { 
							            	newlistResources.add(arrayElementexactMatchNodeUri);
										}	
							        }
							    } 
						    }	
						}	    
					}
				} 
			}
		}
		//System.out.println(writer.writeValueAsString(newlistResources));
		return newlistResources;
	}
	
	public static ArrayList<String> broaderResources(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		// On traite chaque ressource obtenue par la recherche textuelle
		for (String resource : listResources) {
			if (!newlistResources.contains(resource)) { 
            	newlistResources.add(resource);
			}		
			JsonNode searchResultResource = jsonToNode(get(REST_URL +"/data/?uri=" + resource));
			JsonNode graphNode = searchResultResource.path("graph");
			if (graphNode.isMissingNode()) {
				//si node manquant
			} else {
				if(graphNode.isArray()){
					ArrayNode arrayNode = (ArrayNode) graphNode;
					for(int i = 0; i < arrayNode.size(); i++) {
						JsonNode arrayElement = arrayNode.get(i);
						Iterator<String> fieldNames = arrayElement.fieldNames();
						while(fieldNames.hasNext()) {
							String fieldName = fieldNames.next();
							String field = arrayElement.get(fieldName).asText();
						    if (fieldName.contentEquals("uri") & field.contentEquals(resource)) {
								JsonNode narrowerNode = arrayElement.path("broader");
								if(narrowerNode.isObject()){
							        Iterator<String> fieldNamesnarrowerNode = narrowerNode.fieldNames();
							        while(fieldNamesnarrowerNode.hasNext()) {
							            String fieldNamenarrowerNode = fieldNamesnarrowerNode.next();
							            String fieldnarrowerNode = narrowerNode.get(fieldNamenarrowerNode).asText();
							            if (!newlistResources.contains(fieldnarrowerNode)) { 
							            	newlistResources.add(fieldnarrowerNode);
										}				            
							        }
							    } else if(narrowerNode.isArray()){
							        ArrayNode arrayNodenarrowerNode = (ArrayNode) narrowerNode;
							        for(int ii = 0; ii < arrayNodenarrowerNode.size(); ii++) {
							            JsonNode arrayElementnarrowerNode = arrayNodenarrowerNode.get(ii);
							            String arrayElementnarrowerNodeUri = arrayElementnarrowerNode.path("uri").asText();
							            if (!newlistResources.contains(arrayElementnarrowerNodeUri)) { 
							            	newlistResources.add(arrayElementnarrowerNodeUri);
										}	
							        }
							    } 
						    }	
						}	    
					}
				} 
			}
		}
		//System.out.println(writer.writeValueAsString(newlistResources));
		return newlistResources;
	}
	
	
	public static ArrayList<String> narrowerResources(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		// On traite chaque ressource obtenue par la recherche textuelle
		for (String resource : listResources) {
			if (!newlistResources.contains(resource)) { 
            	newlistResources.add(resource);
			}		
			JsonNode searchResultResource = jsonToNode(get(REST_URL +"/data/?uri=" + resource));
			JsonNode graphNode = searchResultResource.path("graph");
			if (graphNode.isMissingNode()) {
				//si node manquant
			} else {
				if(graphNode.isArray()){
					ArrayNode arrayNode = (ArrayNode) graphNode;
					for(int i = 0; i < arrayNode.size(); i++) {
						JsonNode arrayElement = arrayNode.get(i);
						Iterator<String> fieldNames = arrayElement.fieldNames();
						while(fieldNames.hasNext()) {
							String fieldName = fieldNames.next();
							String field = arrayElement.get(fieldName).asText();
						    if (fieldName.contentEquals("uri") & field.contentEquals(resource)) {
								JsonNode narrowerNode = arrayElement.path("narrower");
								if(narrowerNode.isObject()){
							        Iterator<String> fieldNamesnarrowerNode = narrowerNode.fieldNames();
							        while(fieldNamesnarrowerNode.hasNext()) {
							            String fieldNamenarrowerNode = fieldNamesnarrowerNode.next();
							            String fieldnarrowerNode = narrowerNode.get(fieldNamenarrowerNode).asText();
							            if (!newlistResources.contains(fieldnarrowerNode)) { 
							            	newlistResources.add(fieldnarrowerNode);
										}				            
							        }
							    } else if(narrowerNode.isArray()){
							        ArrayNode arrayNodenarrowerNode = (ArrayNode) narrowerNode;
							        for(int ii = 0; ii < arrayNodenarrowerNode.size(); ii++) {
							            JsonNode arrayElementnarrowerNode = arrayNodenarrowerNode.get(ii);
							            String arrayElementnarrowerNodeUri = arrayElementnarrowerNode.path("uri").asText();
							            if (!newlistResources.contains(arrayElementnarrowerNodeUri)) { 
							            	newlistResources.add(arrayElementnarrowerNodeUri);
										}	
							        }
							    } 
								
								JsonNode exactMatchNode = arrayElement.path("exactMatch");
								if(exactMatchNode.isObject()){
							        Iterator<String> fieldNamesexactMatchNode = exactMatchNode.fieldNames();
							        while(fieldNamesexactMatchNode.hasNext()) {
							            String fieldNameexactMatchNode = fieldNamesexactMatchNode.next();
							            String fieldexactMatchNode = exactMatchNode.get(fieldNameexactMatchNode).asText();
							            if (!newlistResources.contains(fieldexactMatchNode)) { 
							            	newlistResources.add(fieldexactMatchNode);
										}	
							        }
							    } else if(exactMatchNode.isArray()){
							        ArrayNode arrayNodeexactMatchNode = (ArrayNode) exactMatchNode;
							        for(int ii = 0; ii < arrayNodeexactMatchNode.size(); ii++) {
							            JsonNode arrayElementexactMatchNode = arrayNodeexactMatchNode.get(ii);
							            String arrayElementexactMatchNodeUri = arrayElementexactMatchNode.path("uri").asText();
							            if (!newlistResources.contains(arrayElementexactMatchNodeUri)) { 
							            	newlistResources.add(arrayElementexactMatchNodeUri);
										}	
							        }
							    } 
						    }	
						}	    
					}
				} 
			}
		}
		//System.out.println(writer.writeValueAsString(newlistResources));
		return newlistResources;
	}
	
	public static ArrayList<String> closeMatchResources(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		// On traite chaque ressource obtenue par la recherche textuelle
		for (String resource : listResources) {
			if (!newlistResources.contains(resource)) { 
            	newlistResources.add(resource);
			}		
			JsonNode searchResultResource = jsonToNode(get(REST_URL +"/data/?uri=" + resource));
			JsonNode graphNode = searchResultResource.path("graph");
			if (graphNode.isMissingNode()) {
				//si node manquant
			} else {
				if(graphNode.isArray()){
					ArrayNode arrayNode = (ArrayNode) graphNode;
					for(int i = 0; i < arrayNode.size(); i++) {
						JsonNode arrayElement = arrayNode.get(i);
						Iterator<String> fieldNames = arrayElement.fieldNames();
						while(fieldNames.hasNext()) {
							String fieldName = fieldNames.next();
							String field = arrayElement.get(fieldName).asText();
						    if (fieldName.contentEquals("uri") & field.contentEquals(resource)) {
								JsonNode closeMatchNode = arrayElement.path("closeMatch");
								//System.out.println(closeMatchNode);
								if(closeMatchNode.isObject()){
							        Iterator<String> fieldNamescloseMatchNode = closeMatchNode.fieldNames();
							        while(fieldNamescloseMatchNode.hasNext()) {
							            String fieldNamecloseMatchNode = fieldNamescloseMatchNode.next();
							            String fieldcloseMatchNode = closeMatchNode.get(fieldNamecloseMatchNode).asText();
							            if (!newlistResources.contains(fieldcloseMatchNode)) { 
							            	newlistResources.add(fieldcloseMatchNode);
										}	
							        }
							    } else if(closeMatchNode.isArray()){
							        ArrayNode arrayNodecloseMatchNode = (ArrayNode) closeMatchNode;
							        for(int ii = 0; ii < arrayNodecloseMatchNode.size(); ii++) {
							            JsonNode arrayElementcloseMatchNode = arrayNodecloseMatchNode.get(ii);
							            String arrayElementcloseMatchNodeUri = arrayElementcloseMatchNode.path("uri").asText();
							            if (!newlistResources.contains(arrayElementcloseMatchNodeUri)) { 
							            	newlistResources.add(arrayElementcloseMatchNodeUri);
										}	
							        }
							    } 
						    }	
						}	    
					}
				} 
			}
		}
		//System.out.println(writer.writeValueAsString(newlistResources));
		return newlistResources;
	}
	
	public static ArrayList<String> exactMatchResources(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		// On traite chaque ressource obtenue par la recherche textuelle
		for (String resource : listResources) {
			if (!newlistResources.contains(resource)) { 
            	newlistResources.add(resource);
			}		
			JsonNode searchResultResource = jsonToNode(get(REST_URL +"/data/?uri=" + resource));
			JsonNode graphNode = searchResultResource.path("graph");
			if (graphNode.isMissingNode()) {
				//si node manquant
			} else {
				if(graphNode.isArray()){
					ArrayNode arrayNode = (ArrayNode) graphNode;
					for(int i = 0; i < arrayNode.size(); i++) {
						JsonNode arrayElement = arrayNode.get(i);
						Iterator<String> fieldNames = arrayElement.fieldNames();
						while(fieldNames.hasNext()) {
							String fieldName = fieldNames.next();
							String field = arrayElement.get(fieldName).asText();
						    if (fieldName.contentEquals("uri") & field.contentEquals(resource)) {
								JsonNode exactMatchNode = arrayElement.path("exactMatch");
								if(exactMatchNode.isObject()){
							        Iterator<String> fieldNamesexactMatchNode = exactMatchNode.fieldNames();
							        while(fieldNamesexactMatchNode.hasNext()) {
							            String fieldNameexactMatchNode = fieldNamesexactMatchNode.next();
							            String fieldexactMatchNode = exactMatchNode.get(fieldNameexactMatchNode).asText();
							            if (!newlistResources.contains(fieldexactMatchNode)) { 
							            	newlistResources.add(fieldexactMatchNode);
										}	
							        }
							    } else if(exactMatchNode.isArray()){
							        ArrayNode arrayNodeexactMatchNode = (ArrayNode) exactMatchNode;
							        for(int ii = 0; ii < arrayNodeexactMatchNode.size(); ii++) {
							            JsonNode arrayElementexactMatchNode = arrayNodeexactMatchNode.get(ii);
							            String arrayElementexactMatchNodeUri = arrayElementexactMatchNode.path("uri").asText();
							            if (!newlistResources.contains(arrayElementexactMatchNodeUri)) { 
							            	newlistResources.add(arrayElementexactMatchNodeUri);
										}	
							        }
							    } 
						    }	
						}				    
					}
				} 
			}
		}
		//System.out.println(writer.writeValueAsString(newlistResources));
		return newlistResources;
	}
	
	public static ArrayList<String> otherResourcesRelatedToResourceWithBroader(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		String resource = null;
		Boolean endLoop = false;
		Boolean listEqual = false;
		while (!endLoop) {
			listEqual = true;
			newlistResources = broaderResources(listResources);
			// Vérification de l'égalité des listes pour savoir si la liste continue à grandir
			for (int i = 0; i < newlistResources.size(); i++) {
	        	resource = (String) newlistResources.get(i);
	        	if (!listResources.contains(resource)) {
	        		listEqual = false;
	        	}
			}
			if (!listEqual){
				listResources.clear();
				for (int i = 0; i < newlistResources.size(); i++) {
		        	resource = (String) newlistResources.get(i);
		        	listResources.add(resource);
				}
				newlistResources.clear();
			}
			else {
				endLoop = true;
			}	
		}		
		return newlistResources;
	}
	
	
	public static ArrayList<String> otherResourcesRelatedToResourceWithNarrower(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		String resource = null;
		Boolean endLoop = false;
		Boolean listEqual = false;
		while (!endLoop) {
			listEqual = true;
			newlistResources = narrowerResources(listResources);
			// Vérification de l'égalité des listes pour savoir si la liste continue à grandir
			for (int i = 0; i < newlistResources.size(); i++) {
	        	resource = (String) newlistResources.get(i);
	        	if (!listResources.contains(resource)) {
	        		listEqual = false;
	        	}
			}
			if (!listEqual){
				listResources.clear();
				for (int i = 0; i < newlistResources.size(); i++) {
		        	resource = (String) newlistResources.get(i);
		        	listResources.add(resource);
				}
				newlistResources.clear();
			}
			else {
				endLoop = true;
			}	
		}		
		return newlistResources;
	}
	
	
	public static ArrayList<String> otherResourcesRelatedToResourceWithCloseMatch(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		String resource = null;
		Boolean endLoop = false;
		Boolean listEqual = false;
		while (!endLoop) {
			listEqual = true;
			newlistResources = closeMatchResources(listResources);
			// Vérification de l'égalité des listes pour savoir si la liste continue à grandir
			for (int i = 0; i < newlistResources.size(); i++) {
	        	resource = (String) newlistResources.get(i);
	        	if (!listResources.contains(resource)) {
	        		listEqual = false;
	        	}
			}
			if (!listEqual){
				listResources.clear();
				for (int i = 0; i < newlistResources.size(); i++) {
		        	resource = (String) newlistResources.get(i);
		        	listResources.add(resource);
				}
				newlistResources.clear();
			}
			else {
				endLoop = true;
			}	
		}		
		return newlistResources;
	}
	
	
	public static ArrayList<String> otherResourcesRelatedToResourceWithExactMatch(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		String resource = null;
		Boolean endLoop = false;
		Boolean listEqual = false;
		while (!endLoop) {
			listEqual = true;
			newlistResources = exactMatchResources(listResources);
			// Vérification de l'égalité des listes pour savoir si la liste continue à grandir
			for (int i = 0; i < newlistResources.size(); i++) {
	        	resource = (String) newlistResources.get(i);
	        	if (!listResources.contains(resource)) {
	        		listEqual = false;
	        	}
			}
			if (!listEqual){
				listResources.clear();
				for (int i = 0; i < newlistResources.size(); i++) {
		        	resource = (String) newlistResources.get(i);
		        	listResources.add(resource);
				}
				newlistResources.clear();
			}
			else {
				endLoop = true;
			}	
		}		
		return newlistResources;
	}
	
	public static ArrayList<String> otherResourcesRelatedToResource(ArrayList<String> listResources) throws JsonProcessingException {
		ArrayList<String> newlistResources = new ArrayList<String>();
		String resource = null;
		Boolean endLoop = false;
		Boolean listEqual = false;
		while (!endLoop) {
			listEqual = true;
			newlistResources = descendantsResources(listResources);
			// Vérification de l'égalité des listes pour savoir si la liste continue à grandir
			for (int i = 0; i < newlistResources.size(); i++) {
	        	resource = (String) newlistResources.get(i);
	        	if (!listResources.contains(resource)) {
	        		listEqual = false;
	        	}
			}
			if (!listEqual){
				listResources.clear();
				for (int i = 0; i < newlistResources.size(); i++) {
		        	resource = (String) newlistResources.get(i);
		        	listResources.add(resource);
				}
				newlistResources.clear();
			}
			else {
				endLoop = true;
			}	
		}		
		return newlistResources;
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
			conn.setRequestProperty("Accept", "application/json");
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