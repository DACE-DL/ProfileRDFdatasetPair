package profiling.util;

import java.net.URI;
import java.net.URISyntaxException;

public class GiveLocalname {
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static String giveName(String uriString) {
		String localname = " ";
		try {
            URI uri = new URI(uriString);
            // Obtenez le localname à partir de l'URI
            localname = uri.getFragment();
			if (localname == null) {
				localname = "a";
				System.out.println("uri : " + uri);
			}
            // Affichez le localname
            System.out.println("Localname : " + localname);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
		
		return localname;
	}	
}