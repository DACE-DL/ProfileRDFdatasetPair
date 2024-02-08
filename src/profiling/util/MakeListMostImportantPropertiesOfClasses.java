package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class MakeListMostImportantPropertiesOfClasses {
	
	// Création du modèle de description
	public static ArrayList<UriAndUriAndUriListList>  makeList(ArrayList<String> listMostImportantClasses,ArrayList<UriAndUriList> listOfNewClassWithPropertiesCombinaison, ArrayList<UriAndUriAndNumberListAndUriAndNumberListAndNumber> listPropertyMostUsedWithDatatypeAndClassRange) {
		Instant start0 = Instant.now();	
		ArrayList<UriAndUriAndUriListList> listMostImportantPropertiesOfClasses = new ArrayList<UriAndUriAndUriListList>();
		
		for (String treatedClass : listMostImportantClasses) {
			ArrayList<UriAndUriList> uriAndUriListList = new ArrayList<UriAndUriList>();
			for (UriAndUriList propertiesOfClass : listOfNewClassWithPropertiesCombinaison) {
				if(propertiesOfClass.getUri().equals(treatedClass) ) {
					for (Uri property : propertiesOfClass.getUriList()) {
						for (UriAndUriAndNumberListAndUriAndNumberListAndNumber propertiesDatatypes : listPropertyMostUsedWithDatatypeAndClassRange) {
							if(propertiesDatatypes.getUri().equals(property)) {
								UriAndUriList uriAndUriList = new UriAndUriList();
								uriAndUriList.setUri(property.getUri());
								ArrayList<Uri> uriList = new ArrayList<Uri>();
								for (UriAndNumber datatypeAndNumber : propertiesDatatypes.getUriAndNumberList1()) {
								Uri datatype = new Uri(datatypeAndNumber.getUri());	
								uriList.add(datatype);
								}
								if (uriList.size() > 0) {
									uriAndUriList.setUriList(uriList);
									uriAndUriListList.add(uriAndUriList);
								} else {
									// Si la liste est vide on ne selectionne la propriété que si aucune classe
									//  n'est declarée dans le domaine.
									if (propertiesDatatypes.getUriAndNumberList2().size() == 0) {
										uriAndUriList.setUriList(uriList);
										uriAndUriListList.add(uriAndUriList);
										// System.out.println("Propriété " + property.getUri() + " sans domaine");
									} else {
										// System.out.println("Propriété " + property.getUri() + " de type ObjectProperty");
									}
								}
								
							}	
						}
					}	
				}
			}
			UriAndUriAndUriListList uriAndUriAndUriListList = new UriAndUriAndUriListList();
			uriAndUriAndUriListList.setUri(treatedClass);
			uriAndUriAndUriListList.setUriAndUriList(uriAndUriListList);
			listMostImportantPropertiesOfClasses.add(uriAndUriAndUriListList);
		}
		Instant end0 = Instant.now();
		System.out.println("Running time for listMostImportantPropertiesOfClasses: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return listMostImportantPropertiesOfClasses;
	}
}

