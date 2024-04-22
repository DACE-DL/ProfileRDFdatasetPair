package profiling.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommonPartFinder {
    public static void main(String[] args) {
        List<String> uris1 = new ArrayList<>();
        uris1.add("http://purl.obolibrary.org/obo/ncbitaxon#");
        uris1.add("http://purl.obolibrary.org/obo/");
        uris1.add("http://taxref.mnhn.fr/");
        uris1.add("http://taxref.mnhn.fr/lod/taxon/351926/");
        uris1.add("http://taxref.mnhn.fr/lod/taxon/351927/");
        uris1.add("http://taxref.mnhn.fr/lod/taxon/351928/");
        uris1.add("http://www.taxref.mnhn.fr/lod/taxon/351928/");
        uris1.add("http://www.tttttaxref.mnhn.fr/lod/taxon/351928/");
        uris1.add("www.orgtamere");
        uris1.add("www.orgtompere");
        

        List<String> commonParts = new ArrayList<>();
        // Find common parts
        commonParts = findLargestCommonPart(uris1);
        System.out.println("CommonParts: " + commonParts.toString());
        // Use an iterator to go through the list
        //  and remove elements that share a common part
        Iterator<String> iterator = uris1.iterator();
        while (iterator.hasNext()) {
            String uri = iterator.next();
            for (String commonPart : commonParts) {
                if (uri.startsWith(commonPart)) {
                    iterator.remove();
                    break;
                }
            }
        }
        uris1.addAll(commonParts);
        System.out.println("List: " + uris1.toString());
    }    

    public static List<String> findLargestCommonPart(List<String> uris) {
        
        List<String> urisTemp = new ArrayList<>(uris);
        Set<String> distinctCommonPrefix = new HashSet<>();
        
        if (uris == null || uris.isEmpty()) {
            return new ArrayList<>(distinctCommonPrefix);
        }
    
        String commonPrefix = "";
        for (String uri : uris) {
            for (String uriTemp : urisTemp) {
                if (!uriTemp.equals(uri)) {
                    commonPrefix = findCommonPrefix(uri, uriTemp);
                    if (! (commonPrefix.equals("http://") || commonPrefix.equals("")) ) {
                        distinctCommonPrefix.add(commonPrefix);
                        break;
                    }
                }
            }
        }
        return new ArrayList<>(distinctCommonPrefix);
    }
    
    private static String findCommonPrefix(String str1, String str2) {
        int minLength = Math.min(str1.length(), str2.length());
        StringBuilder commonPrefixBuilder = new StringBuilder();
        for (int i = 0; i < minLength; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
            commonPrefixBuilder.append(str1.charAt(i));
        }
        String commonPart = commonPrefixBuilder.toString();
        int lastHashIndex = commonPart.lastIndexOf('#');
        if (lastHashIndex == -1) {
            int lastSlashIndex = commonPart.lastIndexOf('/');
            if (lastSlashIndex < commonPrefixBuilder.length() ) {
                commonPart = commonPart.substring(0, lastSlashIndex + 1);
            }
        }
        return commonPart;
    }
}





    