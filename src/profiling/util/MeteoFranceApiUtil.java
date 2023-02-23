package profiling.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MeteoFranceApiUtil {
	
	static final String REST_URL = "https://geoservices.meteofrance.fr/api/__mKXdROcIOyugfsjdnGksB6JAG3R68ckzmUWn53pPUsU__/";
    static final String SOS = "MF-CLIM-MONTHLY-WMOESS-METROPOLE-SOS?";
    
    public static void main(String[] args) throws JsonProcessingException  {
    	Instant start0 = Instant.now();	
    	ArrayList<String> ListResources = new ArrayList<String>();
    	// Test
    	ListResources = MeteoFranceApiUtil.test("service=SOS&version=2.0.0&request=GetObservation&featureOfInterest=http://geoservices.meteofrance.fr/Station/IIiii/07630&namespaces=xmlns(om,http://www.opengis.net/om/2.0)&temporalFilter=om:phenomenonTime,2015-01/2015-12");
    	
        
		for (String resource : ListResources) {
            System.out.println(resource);
        }
		Instant end0 = Instant.now();
    	System.out.println("Durée d'exécution Totale: " + Duration.between(start0, end0).getSeconds() + " secondes");    
    }

public static ArrayList<String> test(String strtest) throws JsonProcessingException {
    	
    	ArrayList<String> ListResources = new ArrayList<String>();
    	
    	String resultGet = get(REST_URL + SOS + strtest);
    	System.out.println(resultGet);
            
		return ListResources;
        
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
            rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
                System.out.println(line);
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	

}