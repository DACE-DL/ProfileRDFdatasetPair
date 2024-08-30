package profiling.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.jena.rdf.model.Model;

public class VerifyModelNameSpace {
    
    // Création d'une liste des espaces de noms dans un modèle
    public static ArrayList<Uri> makeList(Model model) {

        Set<Uri> uniqueResources = new HashSet<>();

        // Récupère la carte des préfixes et des espaces de noms
        Map<String, String> prefixMap = model.getNsPrefixMap();

        // Crée une liste des espaces de noms
        for (String namespace : prefixMap.values()) {
            uniqueResources.add(new Uri(namespace));
        }

        // Convertit le Set en ArrayList pour trier les éléments
        ArrayList<Uri> listResources = new ArrayList<>(uniqueResources);

        // Trie la liste en ordre décroissant
        listResources.sort((uri1, uri2) -> uri2.getUri().compareTo(uri1.getUri()));

        return listResources;
    }
}
