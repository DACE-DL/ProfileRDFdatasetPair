package profiling.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.lang.reflect.Field;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.sparql.expr.nodevalue.NodeFunctions;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProfilingUtil {
	
	// Retourne un tableau contenant les noms de fichier à traiter	
	public static ArrayList<String> makeListFileName(String nameJsonFile) throws Exception {
		ArrayList<String> listFile = new ArrayList<String>();
		File file  = new File(nameJsonFile);
		if (file.exists()) {
		   //on récupère le noms des fichier à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(nameJsonFile);

			ObjectMapper objectMapper = new ObjectMapper();

			List<FileName> fileNames = objectMapper.readValue(jsonArray, new TypeReference<List<FileName>>(){});
			
			fileNames.forEach(x -> listFile.add(x.getName()));
				
		} else {
			System.out.println("Le fichier " + nameJsonFile +  " est inexistant !"); 
		}
		
		return listFile;
	}

	// Retourne un tableau contenant les paires de noms de fichier à traiter	
	public static ArrayList<PairOfDatasets> makeListPairFileName(String nameJsonFile) throws Exception {
		ArrayList<PairOfDatasets> listPairFiles = new ArrayList<PairOfDatasets>();
		File file  = new File(nameJsonFile);
		if (file.exists()) {
		   //on récupère le noms des fichier à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(nameJsonFile);

			ObjectMapper objectMapper = new ObjectMapper();

			List<PairOfDatasets> fileNames = objectMapper.readValue(jsonArray, new TypeReference<List<PairOfDatasets>>(){});
			
			fileNames.forEach(x -> listPairFiles.add(new PairOfDatasets(x.getIdPair(), x.getPairActive(), x.getFilesSource(), x.getFilesTarget())));
				
		} else {
			System.out.println("Le fichier " + nameJsonFile +  " est inexistant !"); 
		}
		
		return listPairFiles;
	}

	// Retourne un nom de fichier à traiter	
		public static String makeFileName(String nameJsonFile) throws Exception {
			String nameOfFile = null;
			File file  = new File(nameJsonFile);
			if (file.exists()) {
			   //on récupère le noms des fichier à traiter dans le fichier JSON
				String jsonArray = ProfilingUtil.readFileAsString(nameJsonFile);

				ObjectMapper objectMapper = new ObjectMapper();

				FileName fileName = objectMapper.readValue(jsonArray, new TypeReference<FileName>(){});
				
				nameOfFile = fileName.getName();
			} else {
				System.out.println("Le fichier " + nameJsonFile +  " est inexistant !"); 
			}
			
			
			return nameOfFile ;
		}
	
	// Retourne un top	
	public static String extractParameter(String nameJsonFile, String keyParameter) throws Exception {
		String valueParameter = "";
		File file  = new File(nameJsonFile);
		if (file.exists()) {
			//on récupère le contenu du fichier
			String jsonString = ProfilingUtil.readFileAsString(nameJsonFile);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(jsonString);
			valueParameter = jsonNode.get(keyParameter).textValue();
			
		} else {
			System.out.println("Le fichier " + nameJsonFile +  " est inexistant !"); 
		}

		return valueParameter;
	}

	public static String readFileAsString(String file)throws Exception
    {
		String myString = "";
		try {
		myString = new String(Files.readAllBytes(Paths.get(file)));
		}
		catch (IOException e) {System.out.println("IO problem readFileAsString : " + e );}
		catch (Exception e) {System.out.println("big problem in readFileAsString : " + e.toString());}
        return myString;
	}
	
	/**
	 * For a given subject resource and a given collection of (label/comment) properties this finds the most
	 * suitable value of either property for a given list of languages (usually from the current user's preferences).
	 * For example, if the user's languages are [ "en-AU" ] then the function will prefer "mate"@en-AU over
	 * "friend"@en and never return "freund"@de.  The function falls back to literals that have no language
	 * if no better literal has been found.
	 * @param resource  the subject resource
	 * @param langs  the allowed languages
	 * @param properties  the properties to check
	 * @return the best suitable value or null
	 */
	
	public static Literal getBestStringLiteral(Resource resource) {
		
		// Initialisation de la configuration
		// Chemin d'acc�s, noms fichiers...
		new ProfilingConf();  

		
		List<String> langs = Arrays.asList(ProfilingConf.listPreferredLanguages);
		List<Property> properties = new ArrayList<>();
		List<String> stringProperties = Arrays.asList(ProfilingConf.listLabelProperties);
		Property labelProperty = null ;
		for (String stringProperty : stringProperties) {
			labelProperty = ResourceFactory.createProperty(stringProperty);
			properties.add(labelProperty);
		}
		
		
		return getBestStringLiteral(resource, langs, properties);
	}
	

	
	public static Literal getBestStringLiteral(Resource resource, List<String> langs, Iterable<Property> properties) {
		return getBestStringLiteral(resource, langs, properties, (r,p) -> r.listProperties(p));
	}
	
	
	public static Literal getBestStringLiteral(Resource resource, List<String> langs, Iterable<Property> properties, BiFunction<Resource,Property,Iterator<Statement>> getter) {
		Literal label = null;
		int bestLang = -1;
		for(Property predicate : properties) {
			Iterator<Statement> it = getter.apply(resource, predicate);
			while(it.hasNext()) {
				RDFNode object = it.next().getObject();
				if(object.isLiteral()) {
					Literal literal = (Literal)object;
					String lang = literal.getLanguage();
					if(lang.length() == 0 && label == null) {
						label = literal;
					}
					else {
						// 1) Never use a less suitable language
						// 2) Never replace an already existing label (esp: skos:prefLabel) unless new lang is better
						// 3) Fall back to more special languages if no other was found (e.g. use en-GB if only "en" is accepted)
						int startLang = bestLang < 0 ? langs.size() - 1 : (label != null ? bestLang - 1 : bestLang);
						for(int i = startLang; i >= 0; i--) {
							String langi = langs.get(i);
							if(langi.equals(lang)) {
								label = literal;
								bestLang = i;
							}
							else if(lang.contains("-") && NodeFunctions.langMatches(lang, langi) && label == null) {
								label = literal;
							}
						}
					}
				}
			}
		}
		return label;
	}

	public static String decodeHexToStringUTF8(String hex){

        String[] list=hex.split("(?<=\\G.{2})");
        ByteBuffer buffer= ByteBuffer.allocate(list.length);
        for(String str: list)
            buffer.put(Byte.parseByte(str,16)); 
        String strUTF8 = "";
		try {
			strUTF8 = new String(buffer.array(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
        return strUTF8;

	}
	public static String controlNameSpace(String uri){
		String str = "";
		String lastLetterURI = uri.substring(uri.length()-1);
			if	(lastLetterURI.equals("/") || lastLetterURI.equals("#")) {
				str = uri;
			}	else {
				//System.out.println("NS : " + uri);
				int indexSlach = uri.lastIndexOf("/");
				int indexDiese = uri.lastIndexOf("#");
				if(indexDiese > indexSlach) {
					//Il y a un dièse (#) aprés un slach (/)
					str = uri.substring(0,indexDiese + 1);
				} else {
					if (indexSlach > -1) { 
						str = uri.substring(0,indexSlach + 1);
					}
				}
				//System.out.println("NS2 : " + str);
			}
		return str;
	}
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriFile(ArrayList<Uri> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<Uri> makeArrayListUri(String nameJsonObjectFile) throws Exception {
		ArrayList<Uri> listObjects = new ArrayList<Uri>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<Uri>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndNumberFile(ArrayList<UriAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        // System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndNumber> makeArrayListUriAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndNumber> listObjects = new ArrayList<UriAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	
	
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndUriAndNumberListAndUriAndNumberListAndNumberFile(ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        // System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber> makeArrayListUriAndUriAndNumberListAndUriAndNumberListAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber> listObjects = new ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndUriListAndNumberListAndUriListAndNumberListAndNumberFile(ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        // System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> makeArrayListUriAndUriListAndNumberListAndUriListAndNumberListAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listObjects = new ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndNumberAndNumberAndNumberFile(ArrayList<UriAndNumberAndNumberAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        // System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndNumberAndNumberAndNumber> makeArrayListUriAndNumberAndNumberAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndNumberAndNumberAndNumber> listObjects = new ArrayList<UriAndNumberAndNumberAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndNumberAndNumberAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriListAndNumberFile(ArrayList<UriListAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        // System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriListAndNumber> makeArrayListUriListAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriListAndNumber> listObjects = new ArrayList<UriListAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriListAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriListAndUriAndNumberListAndNumberFile(ArrayList<UriListAndUriAndNumberListAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        // System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriListAndUriAndNumberListAndNumber> makeArrayListUriListAndUriAndNumberListAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriListAndUriAndNumberListAndNumber> listObjects = new ArrayList<UriListAndUriAndNumberListAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriListAndUriAndNumberListAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriListAndUriListAndNumberListAndNumberFile(ArrayList<UriListAndUriListAndNumberListAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        // System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriListAndUriListAndNumberListAndNumber> makeArrayListUriListAndUriListAndNumberListAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriListAndUriListAndNumberListAndNumber> listObjects = new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriListAndUriListAndNumberListAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndUriAndValueFile(ArrayList<UriAndUriAndValue> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndUriAndValue> makeArrayListUriAndUriAndValue(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndUriAndValue> listObjects = new ArrayList<UriAndUriAndValue>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndUriAndValue>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndStringAndBigNumberFile(ArrayList<UriAndStringAndBigNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndStringAndBigNumber> makeArrayListUriAndStringAndBigNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndStringAndBigNumber> listObjects = new ArrayList<UriAndStringAndBigNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndStringAndBigNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndUriFile(ArrayList<UriAndUri> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndUri> makeArrayListUriAndUri(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndUri> listObjects = new ArrayList<UriAndUri>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndUri>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndUriListFile(ArrayList<UriAndUriList> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndUriList> makeArrayListUriAndUriList(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndUriList> listObjects = new ArrayList<UriAndUriList>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndUriList>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriListAndUriList2File(ArrayList<UriListAndUriList2> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriListAndUriList2> makeArrayListUriListAndUriList2(String nameJsonObjectFile) throws Exception {
		ArrayList<UriListAndUriList2> listObjects = new ArrayList<UriListAndUriList2>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriListAndUriList2>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndUriAndNumberFile(ArrayList<UriAndUriAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndUriAndNumber> makeArrayListUriAndUriAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndUriAndNumber> listObjects = new ArrayList<UriAndUriAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndUriAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndUriAndUriFile(ArrayList<UriAndUriAndUri> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndUriAndUri> makeArrayListUriAndUriAndUri(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndUriAndUri> listObjects = new ArrayList<UriAndUriAndUri>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndUriAndUri>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndUriAndUriAndNumberFile(ArrayList<UriAndUriAndUriAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndUriAndUriAndNumber> makeArrayListUriAndUriAndUriAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndUriAndUriAndNumber> listObjects = new ArrayList<UriAndUriAndUriAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndUriAndUriAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriListAndUriAndUriListAndNumberFile(ArrayList<UriListAndUriAndUriListAndNumber> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriListAndUriAndUriListAndNumber> makeArrayListUriListAndUriAndUriListAndNumber(String nameJsonObjectFile) throws Exception {
		ArrayList<UriListAndUriAndUriListAndNumber> listObjects = new ArrayList<UriListAndUriAndUriListAndNumber>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriListAndUriAndUriListAndNumber>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriListAndUriAndUriListFile(ArrayList<UriListAndUriAndUriList> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriListAndUriAndUriList> makeArrayListUriListAndUriAndUriList(String nameJsonObjectFile) throws Exception {
		ArrayList<UriListAndUriAndUriList> listObjects = new ArrayList<UriListAndUriAndUriList>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriListAndUriAndUriList>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Déplacer les fichiers d'un répertoire dans un autre répertoire
	public static void ChangeDirectoryFiles(String oldDirectory, String newDirectory) throws Exception {
		File file = new File(oldDirectory);
		File newFile = new File(newDirectory);
		//System.out.println(newDirectory);
		//System.out.println(newFile.getName());
		boolean dirCreated = newFile.mkdirs();
		if (dirCreated) {
			System.out.println("Directory creation : " + newFile.getName()); 
		}
        File[] files = file.listFiles();
		ArrayList<String> listFiles = new ArrayList<String>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory() == true) {

                } else {
                    listFiles.add(files[i].getName());
                }
            }
        }
		for (String fileName : listFiles) {
			Path transfert = Files.move(Paths.get(oldDirectory, fileName), Paths.get(newDirectory, fileName), StandardCopyOption.REPLACE_EXISTING); 
			if(!(transfert != null)) {
				System.out.println("Unable to move file : " + fileName); 
			} 
		}	
	}

	//  Créé un fichier JSON partir d'un tableau de string
	public static void makeJsonStringFile(List<String> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau de string à partir d'un fichier JSON
	public static List<String> makeArrayListString(String nameJsonObjectFile) throws Exception {
		List<String> listObjects = new ArrayList<String>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<String>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau (arrayList) de string
	public static void makeJsonStringFile2(ArrayList<String> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	// Retourne un tableau de string (arrayList) à partir d'un fichier JSON
	public static ArrayList<String> makeArrayListString2(String nameJsonObjectFile) throws Exception {
		ArrayList<String> listObjects = new ArrayList<String>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<String>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un object de type ProfilingResultsObjet
	public static void makeJsonResultsFile(ProfilingResultsObject profilingResultsObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        // System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, profilingResultsObject);		
	}

	// Retourne un object ProfilingResultsObject à partir d'un fichier JSON
	public static ProfilingResultsObject makeResultsObject(String nameJsonObjectFile) throws Exception {
		ProfilingResultsObject resultsObject = new ProfilingResultsObject();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			resultsObject = objectMapper.readValue(jsonArray, new TypeReference<ProfilingResultsObject>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return resultsObject;
	}
	

	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<Lpt> makeArrayListLpt(String nameJsonObjectFile) throws Exception {
		ArrayList<Lpt> listObjects = new ArrayList<Lpt>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<Lpt>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonLptFile(ArrayList<Lpt> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriListAndUriListFile(ArrayList<UriListAndUriList> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriListAndUriList> makeArrayListUriListAndUriList(String nameJsonObjectFile) throws Exception {
		ArrayList<UriListAndUriList> listObjects = new ArrayList<UriListAndUriList>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriListAndUriList>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}

	//  Créé un fichier JSON partir d'un tableau d'objects
	public static void makeJsonUriAndUriAndUriListListFile(ArrayList<UriAndUriAndUriListList> listObject, String nameJsonObjectFile) throws Exception {
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
        //System.out.println(pathOfTheFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();	
		objectMapper.writeValue(file, listObject);		
	}
	
	// Retourne un tableau d'objects à partir d'un fichier JSON
	public static ArrayList<UriAndUriAndUriListList> makeArrayListUriAndUriAndUriListList(String nameJsonObjectFile) throws Exception {
		ArrayList<UriAndUriAndUriListList> listObjects = new ArrayList<UriAndUriAndUriListList>();
		// Récupération du chemin du fichier.
		Path pathOfTheFile = Paths.get(nameJsonObjectFile);
		File file  = new File(pathOfTheFile.toString());
		if (file.exists()) {
		   //on récupère les objects à traiter dans le fichier JSON
			String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriAndUriAndUriListList>>(){});	 
		} else {
			System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
		}	
		return listObjects;
	}
	
		//  Créé un fichier JSON partir d'un tableau d'objects
		public static void makeJsonUriListAndUriAndUriListListFile(ArrayList<UriListAndUriAndUriListList> listObject, String nameJsonObjectFile) throws Exception {
			// Récupération du chemin du fichier.
			Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameJsonObjectFile);
			File file  = new File(pathOfTheFile.toString());
			//System.out.println(pathOfTheFile.toString());
			ObjectMapper objectMapper = new ObjectMapper();	
			objectMapper.writeValue(file, listObject);		
		}
		
		// Retourne un tableau d'objects à partir d'un fichier JSON
		public static ArrayList<UriListAndUriAndUriListList> makeArrayListUriListAndUriAndUriListList(String nameJsonObjectFile) throws Exception {
			ArrayList<UriListAndUriAndUriListList> listObjects = new ArrayList<UriListAndUriAndUriListList>();
			// Récupération du chemin du fichier.
			Path pathOfTheFile = Paths.get(nameJsonObjectFile);
			File file  = new File(pathOfTheFile.toString());
			if (file.exists()) {
			   //on récupère les objects à traiter dans le fichier JSON
				String jsonArray = ProfilingUtil.readFileAsString(pathOfTheFile.toString());
				ObjectMapper objectMapper = new ObjectMapper();	
				listObjects = objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UriListAndUriAndUriListList>>(){});	 
			} else {
				System.out.println("Le fichier " + nameJsonObjectFile +  " est inexistant !"); 
			}	
			return listObjects;
		}
		

    public static void saveTwoDimensionalTableInCSV(Object[][] tableau, String nameFile) {
        // Récupération du chemin du fichier.
        Path pathOfTheFile = Paths.get(ProfilingConf.folderForTmp, nameFile);
        try (BufferedWriter writer = Files.newBufferedWriter(pathOfTheFile, StandardCharsets.UTF_8)) {
            // Parcours du tableau et écriture dans le fichier CSV
            for (int i = 0; i < tableau.length; i++) {
                for (int j = 0; j < tableau[i].length; j++) {
                    if (tableau[i][j] instanceof List<?>) {
                        // Si l'élément est une liste, convertir en chaîne et utiliser un caractère spécial comme séparateur
                        List<?> liste = (List<?>) tableau[i][j];
                        String listeStr = String.join(",", liste.toString());
                        writer.write(listeStr);
                    } else {
                        // Sinon, écrire l'élément directement
                        writer.write(String.valueOf(tableau[i][j]));
                    }

                    if (j < tableau[i].length - 1) {
                        writer.write(';'); // Ajout d'une virgule sauf pour le dernier élément de chaque ligne
                    }
                }
                writer.newLine(); // Nouvelle ligne pour chaque ligne du tableau
            }
            // System.out.println("Le tableau a été sauvegardé dans le fichier CSV avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



	public static void listCharacteristics(Object obj, List<String> characteristics, List<Object> values) {
        // Obtenir la classe de l'objet
        Class<?> clazz = obj.getClass();
        // Liste des champs (fields)
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            characteristics.add(field.getName());
            field.setAccessible(true); // Permet d'accéder aux champs privés
            try {
                values.add(field.get(obj));
            } catch (IllegalAccessException e) {
                values.add("N/A");
            }
        }
    }

	public static String getDurationAsString(long milliseconds) {
        long hours = milliseconds / 3600000;
        long minutes = (milliseconds % 3600000) / 60000;
        long seconds = ((milliseconds % 3600000) % 60000) / 1000;
        long remainingMilliseconds = milliseconds % 1000;

        if (hours > 0) {
            return hours + " hours, " + minutes + " minutes, " + seconds + " seconds";
        } else if (minutes > 0) {
            return minutes + " minutes, " + seconds + " seconds";
        } else if (seconds > 0) {
            return seconds + " seconds";
        } else {
            return remainingMilliseconds + " milliseconds";
        }
    }
}