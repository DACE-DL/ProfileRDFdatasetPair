package profiling.util;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class ProfilingConf { 
	
	private static Properties prop = null;
	private static String filename = "C:\\Users\\conde\\Documents\\GitHub\\ProfileRDFdatasetPair\\config\\profiling.properties";
	
	public static String preferredLanguage = null;
	public static String preferredLanguages = null;
	public static String labelProperties = null;
	public static String[] listPreferredLanguages = null;
	public static String[] listLabelProperties = null;
	
	public static String dskForWindows = null;
	public static String mainfolder = null;
	public static String mainFolderProfiling = null;
	
	public static String fileNameListDatasetsForInitTDB = null;
	public static String fileNameListDatasetsForJSONLDconversion = null;
	public static String fileNameListSourceDatasets = null;
	public static String fileNameListTargetDatasets = null;
	public static String fileNameListPairDatasets = null;
	public static String fileNameListRules = null;
	public static String fileNameListQueries = null;
	public static String fileNameParameters = null;
	public static String fileNameSourceResultsQueries = null;
	public static String fileNameTargetResultsQueries = null;
	
	public static String folderForDatasets = null;
	public static String folderForRules = null;
	public static String folderForQueries = null;
	public static String folderForResults = null;
	public static String folderForSourceResults = null;
	public static String folderForTargetResults = null;
	public static String folderForScriptR = null;
	public static String folderForTmp = null;
	public static String fileNameScriptRSf = null;
	
	public static String folderForTDB = null;
	public static String fileNameTDBdatabase = null;
	
	//query prefix
	public static String dsp = null;
	public static String skos = null;
	public static String skosXL = null;
	public static String vs = null;
	public static String dc = null;
	public static String foaf = null;
	public static String xsd = null;
	public static String owl = null;
	public static String dcterms = null;
	public static String rdfs = null;
	public static String rdf = null;
	public static String geo = null;
	public static String geof = null;
	public static String sf = null;
	public static String sosa = null;
	public static String ssn = null;
	public static String bfo = null;
	public static String om = null;
	public static String wpo = null;
	public static String ncbi = null;
	public static String time = null;
	public static String spatialF = null;
	public static String spatial = null;
	public static String wgs = null;
	public static String sio = null;
	public static String uom = null;
	public static String vid = null;
	
	public static String queryPrefix = null;
	
	/**
	 * Constructor
	*/
	public ProfilingConf() {
		
		Properties prop = new Properties();
		InputStream input = null;
		// InputStream in = null;
		try {
			//input = getClass().getClassLoader().getResourceAsStream(ProfilingConf.filename);
			input = new FileInputStream(ProfilingConf.filename);
			//System.out.println("input : " + input);
			
			prop.load(input);
			
			preferredLanguage = prop.getProperty("preferredLanguage");
			preferredLanguages = prop.getProperty("preferredLanguages");
			labelProperties = prop.getProperty("labelProperties");
			listPreferredLanguages =  preferredLanguages.split(",", 0);
			listLabelProperties = labelProperties.split(",", 0);
			
			Path pathRoot = Paths.get("/");
			Path pathC = Paths.get("C:/");
			if (pathRoot.toAbsolutePath().startsWith(pathC)) {
				mainfolder = prop.getProperty("dskForWindows") + prop.getProperty("mainfolder");
				Path pathmainfolder = Paths.get(mainfolder).toAbsolutePath().normalize();
				mainfolder = pathmainfolder.toString();
			}
			else {
				mainfolder = prop.getProperty("mainfolder");
				Path pathmainfolder = Paths.get(mainfolder).toAbsolutePath().normalize();
				mainfolder = pathmainfolder.toString();
			}
				
			if (pathRoot.toAbsolutePath().startsWith(pathC)) {
				mainFolderProfiling = prop.getProperty("dskForWindows") + prop.getProperty("mainFolderProfiling");
				Path pathmainfolder = Paths.get(mainFolderProfiling).toAbsolutePath().normalize();
				mainFolderProfiling = pathmainfolder.toString();
			}
			else {
				mainFolderProfiling = prop.getProperty("mainFolderProfiling");
				Path pathmainfolder = Paths.get(mainFolderProfiling).toAbsolutePath().normalize();
				mainFolderProfiling = pathmainfolder.toString();
			}
			
			fileNameListDatasetsForInitTDB = prop.getProperty("fileNameListDatasetsForInitTDB");	
			fileNameListDatasetsForJSONLDconversion = prop.getProperty("fileNameListDatasetsForJSONLDconversion");	
			fileNameListSourceDatasets = prop.getProperty("fileNameListSourceDatasets");
			fileNameListTargetDatasets = prop.getProperty("fileNameListTargetDatasets");
			fileNameListPairDatasets = prop.getProperty("fileNameListPairDatasets");
			fileNameListRules = prop.getProperty("fileNameListRules");
			fileNameListQueries = prop.getProperty("fileNameListQueries");
			fileNameParameters = prop.getProperty("fileNameParameters");
			fileNameSourceResultsQueries = prop.getProperty("fileNameSourceResultsQueries");
			fileNameTargetResultsQueries = prop.getProperty("fileNameTargetResultsQueries");
			
			folderForDatasets = prop.getProperty("folderForDatasets");
			Path pathfolderForDatasets = Paths.get(mainFolderProfiling + folderForDatasets);
			folderForDatasets = pathfolderForDatasets.toString();
			
			folderForRules = prop.getProperty("folderForRules");
			Path pathfolderForRules = Paths.get(mainFolderProfiling + folderForRules);
			folderForRules = pathfolderForRules.toString();
			
			folderForQueries = prop.getProperty("folderForQueries");
			Path pathfolderForQueries = Paths.get(mainFolderProfiling + folderForQueries);
			folderForQueries = pathfolderForQueries.toString();
			
			folderForResults = prop.getProperty("folderForResults");
			Path pathfolderForResults = Paths.get(mainFolderProfiling + folderForResults);
			folderForResults = pathfolderForResults.toString();
			
			folderForSourceResults = prop.getProperty("folderForSourceResults");
			Path pathfolderForSourceResults = Paths.get(mainFolderProfiling + folderForSourceResults);
			folderForSourceResults = pathfolderForSourceResults.toString();
			
			folderForTargetResults = prop.getProperty("folderForTargetResults");
			Path pathfolderForTargetResults = Paths.get(mainFolderProfiling + folderForTargetResults);
			folderForTargetResults = pathfolderForTargetResults.toString();
		
			folderForScriptR = prop.getProperty("folderForScriptR");
			Path pathfolderForScriptR = Paths.get(mainFolderProfiling + folderForScriptR);
			folderForScriptR = pathfolderForScriptR.toString();
			
			fileNameScriptRSf = prop.getProperty("fileNameScriptRSf");	
			
			folderForTDB = prop.getProperty("folderForTDB");
			Path pathfolderForTDB = Paths.get(mainFolderProfiling + folderForTDB);
			folderForTDB = pathfolderForTDB.toString();
			fileNameTDBdatabase = prop.getProperty("fileNameTDBdatabase");

			folderForTmp = prop.getProperty("folderForTmp");
			Path pathfolderForTmp = Paths.get(mainFolderProfiling + folderForTmp);
			folderForTmp = pathfolderForTmp.toString();
			
			dsp = prop.getProperty("dsp");
			skos = prop.getProperty("skos");
			skosXL = prop.getProperty("skosXL");
			vs = prop.getProperty("vs");
			dc = prop.getProperty("dc");
			foaf = prop.getProperty("foaf");
			xsd = prop.getProperty("xsd");
			owl = prop.getProperty("owl");
			dcterms = prop.getProperty("dcterms");
			rdfs = prop.getProperty("rdfs");
			rdf = prop.getProperty("rdf");
			geo = prop.getProperty("geo");
			geof = prop.getProperty("geof");
			sf = prop.getProperty("sf");
			sosa = prop.getProperty("sosa");
			ssn = prop.getProperty("ssn");
			bfo = prop.getProperty("bfo");
			om = prop.getProperty("om");
			wpo = prop.getProperty("wpo");
			ncbi = prop.getProperty("ncbi");
			time = prop.getProperty("time");
			spatialF = prop.getProperty("spatialF");
			spatial = prop.getProperty("spatial");
			wgs = prop.getProperty("wgs");
			sio = prop.getProperty("sio");
			uom = prop.getProperty("uom");
			vid = prop.getProperty("void");
			
			// Prefix pour les query
			
			queryPrefix =  	
					"prefix dsp: <" + dsp + ">\n" +
					"prefix skos: <" + skos + ">\n" +
					"prefix skosXL: <" + skosXL + ">\n" +
					"prefix vs: <" + vs + ">\n" +
					"prefix dc: <" + dc + ">\n" +
					"prefix foaf: <" + foaf + ">\n" +
					"prefix xsd: <" + xsd + ">\n" +
					"prefix owl: <" + owl + ">\n" +
					"prefix dcterms: <" + dcterms + ">\n" +
					"prefix rdfs: <" + rdfs + ">\n" +
					"prefix rdf: <" + rdf + ">\n" +
					"prefix geo: <" + geo + ">\n" +
					"prefix geof: <" + geof + ">\n" +
					"prefix sf: <" + sf + ">\n" +
					"prefix sosa: <" + sosa + ">\n" +
					"prefix ssn: <" + ssn + ">\n" +
					"prefix om: <" + om + ">\n" +
					"prefix time: <" + time + ">\n" +
					"prefix spatialF: <" + spatialF + ">\n" +
					"prefix spatial: <" + spatial + ">\n" +
					"prefix wgs: <" + wgs + ">\n" +
					"prefix sio: <" + sio + ">\n" +
					"prefix uom: <" + uom + ">\n" +
					"prefix bfo: <" + bfo + ">\n" +
					"prefix void: <" + vid + ">\n" ;
							
			ProfilingConf.prop = prop;
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMyProperty(String key) {
		return (ProfilingConf.prop.getProperty(key));
	}
	
	/**
	 * Update property
	 */
	public boolean setMyProperty(String key, String value) {
		boolean success = false;
		try {
			ProfilingConf.prop.setProperty(key, value);
			URL resource = getClass().getClassLoader().getResource(ProfilingConf.filename);
			BufferedWriter out = new BufferedWriter(new FileWriter(Paths.get(resource.toURI()).toFile()));
			ProfilingConf.prop.store(out, null);
			out.close();
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
}
