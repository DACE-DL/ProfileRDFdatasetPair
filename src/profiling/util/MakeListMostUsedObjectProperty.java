package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.jena.rdf.model.Model;

public class MakeListMostUsedObjectProperty {
	
	// Création d'une liste des propriétés limité à 100
	public static ArrayList<UriAndNumber> makeList(Model model, ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listMostUsedPropertyWithDatatypeAndClassRange, ArrayList<UriAndUri> listMostUsedPropertyType) {

		ArrayList<UriAndNumber> ListResources = new ArrayList<UriAndNumber>();
		// OWL Full n'impose aucune contrainte sur les annotations dans une ontologie. OWL DL autorise les annotations sur les classes, les propriétés, les individus et les en-têtes d'ontologie, mais uniquement dans les conditions suivantes :
		// Les ensembles de propriétés d'objet, de propriétés de type de données, de propriétés d'annotation et de propriétés d'ontologie doivent être mutuellement disjoints. Ainsi, dans OWL DL, dc:creator ne peut pas être à la fois une propriété de type données et une propriété d'annotation.
		// Les propriétés d'annotation doivent avoir un triple typage explicite de la forme :     AnnotationPropertyID rdf:type owl:AnnotationProperty .   
		// Les propriétés d'annotation ne doivent pas être utilisées dans les axiomes de propriété. Ainsi, dans OWL DL, il n'est pas possible de définir des sous-propriétés ou des contraintes de domaine/plage pour les propriétés d'annotation.
		// L'objet d'une propriété d'annotation doit être soit un littéral de données, soit une référence URI, soit un individu.
		// Référence : http://www.w3.org/TR/2004/REC-owl-ref-20040210/
		for (UriAndUriListAndNumberListAndUriListAndNumberListAndNumber resource : listMostUsedPropertyWithDatatypeAndClassRange) {
			if (resource.getUriListAndNumberList1().size() == 0 && resource.getUriListAndNumberList2().size() > 0) {
				Boolean annotationProperty = false;
				for (UriAndUri resource2 : listMostUsedPropertyType) {
					if (resource2.getUri1().toString().equals(resource.getUri().toString()) && 
						resource2.getUri2().toString().equals("http://www.w3.org/2002/07/owl#AnnotationProperty")) {
						annotationProperty = true;
						break;
					}
				}
				if (!annotationProperty) {
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