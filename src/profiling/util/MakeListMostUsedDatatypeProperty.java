package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.jena.rdf.model.Model;

public class MakeListMostUsedDatatypeProperty {
	
	// Création d'une liste des propriétés limité à 100
	public static ArrayList<UriAndNumber> makeList(Model model, ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listMostUsedPropertyWithDatatypeAndClassRange, ArrayList<UriAndUri> listMostUsedPropertyType) {

		ArrayList<UriAndNumber> ListResources = new ArrayList<UriAndNumber>();

		for (UriAndUriListAndNumberListAndUriListAndNumberListAndNumber resource : listMostUsedPropertyWithDatatypeAndClassRange) {
			String owl = ProfilingConf.owl;
			Boolean typeFound = false;
			String type = "";
			String typeDatatypeProperty = owl + "DatatypeProperty";
			for (UriAndUri resource2 : listMostUsedPropertyType) {
				if (resource2.getUri1().toString().equals(resource.getUri().toString())) {
						typeFound = true;
						type = resource2.getUri2().toString();
					break;
				}
			}
			if (typeFound) { // Le type a été trouvé
				if (type.equals(typeDatatypeProperty)) { // et le type est "owl:DatatypeProperty"
					ListResources.add(new UriAndNumber(resource.getUri().toString(), resource.getNumber())) ;
				}
			} else { // Le type n'a pas été trouvé
				if (resource.getUriListAndNumberList1().size() > 0 && resource.getUriListAndNumberList2().size() == 0) {
					// On estime que puisque la propriété est liées à un litéral (présence d'un datatype), c'est  
					//  une Dataproperty puisqu'on a vérifier qu'il ne s'agit pas d'une annotationProperty (typeFound = "false").
					ListResources.add(new UriAndNumber(resource.getUri().toString(), resource.getNumber())) ; 
				}	
			}
		}	
		
		Collections.sort(ListResources, new UriAndNumberComparator());

		return ListResources;
	}

	static class UriAndNumberComparator implements java.util.Comparator<UriAndNumber> {
		@Override
		public int compare(UriAndNumber a, UriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}