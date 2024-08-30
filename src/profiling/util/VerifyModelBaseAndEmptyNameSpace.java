package profiling.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.jena.rdf.model.Model;

public class VerifyModelBaseAndEmptyNameSpace {
    
    // Création d'une liste des espaces de noms dans un modèle
    public static ArrayList<UriAndString> makeList(Model model) {
 
        ArrayList<UriAndString> listResources = new ArrayList<UriAndString>();
        Boolean baseOK = false;

        // Récupère la carte des préfixes et des espaces de noms
        Map<String, String> prefixMap = model.getNsPrefixMap();

        // Recherche du prefix "xml:base"
        for (Entry<String, String> prefixAndNamespace : prefixMap.entrySet()) {
            System.out.println(prefixAndNamespace.getKey());
            System.out.println(prefixAndNamespace.getValue());
            if(prefixAndNamespace.getKey()=="base") {
                baseOK = true;
                listResources.add(new UriAndString(prefixAndNamespace.getValue(),prefixAndNamespace.getKey()));
                //break;
            }
        }
        if( baseOK == false) {
            listResources.add(new UriAndString("","base"));
        }

        String prefixURI = model.getNsPrefixURI("");
        if( prefixURI != null) {
            listResources.add(new UriAndString(prefixURI,""));
        } else {
            listResources.add(new UriAndString("",""));
        }

        return listResources;
    }
}
