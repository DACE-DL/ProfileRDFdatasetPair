package profiling.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.sparql.pfunction.library.bag;

import com.github.andrewoma.dexx.collection.internal.base.Break;

public class MakeListCombinationPropertiesClassRelationshipsPropertiesOfClasses {
	
	// Création 
	public static ArrayList<UriListAndUriList>  makeList(ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships,ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClass, ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listMostUsedPropertyWithDatatypeAndClassRange) {
		//Instant start0 = Instant.now();	
		ArrayList<UriListAndUriList> listCombinationPropertiesClassRelationshipsPropertiesOfClasses = new ArrayList<UriListAndUriList>();
		// On recupére toutes les combinaisons de classes qui sont en relation
		Set<UriList> setListClassesInvolvedInRelationships = new HashSet<>();
		for (UriListAndUriAndUriListAndNumber uriListAndUriAndUriListAndNumber : listCombinationPropertiesClassRelationships) {
			setListClassesInvolvedInRelationships.add(new UriList(uriListAndUriAndUriListAndNumber.getUriList1()));
			setListClassesInvolvedInRelationships.add(new UriList(uriListAndUriAndUriListAndNumber.getUriList2()));
		}
		ArrayList<UriList> listListClassesInvolvedInRelationships = new ArrayList<>(setListClassesInvolvedInRelationships);	
		
		// System.out.println("listListClassesInvolvedInRelationships");
		// for (UriList treatedListClasses : listListClassesInvolvedInRelationships) {
		//  	System.out.println(treatedListClasses.toString());
		// }	

		
		for (UriList treatedListClasses : listListClassesInvolvedInRelationships) {
			ArrayList<Uri> urilist1 = new ArrayList<Uri>();
			ArrayList<Uri> urilist2 = new ArrayList<Uri>();
			// Pour rappel la liste listCombinationPropertiesWithNewClass à été nettoyée pour obtenir les propriétés communes !  
			// En effet quand plusieurs combinaisons de propriétés corespondait à une même combinaison de classes seules les propriétés
			//  communes ont été retenues.
			for (UriListAndUriList propertiesOfClass : listCombinationPropertiesWithNewClass) {
				if(propertiesOfClass.getUriList1().toString().equals(treatedListClasses.toString()) ) {
					// if (treatedListClasses.toString().equals("[http://www.inrae.fr/DatasetProfiling/Class-4]")) {
					// 	System.out.println("OK ***********");
					// }	
					for (Uri property : propertiesOfClass.getUriList2()) {
						// if (treatedListClasses.toString().equals("[http://www.inrae.fr/DatasetProfiling/Class-4]")) {
						// 	System.out.println(property.toString());
						// }
						urilist2.add(property);
						
					}

					UriListAndUriList uriListAndUriList = new UriListAndUriList();
					uriListAndUriList.setUriList1(urilist1);
					for (int i = 0; i < treatedListClasses.size(); i++) {
						urilist1.add(treatedListClasses.get(i));
					}	
					uriListAndUriList.setUriList2(urilist2);
					listCombinationPropertiesClassRelationshipsPropertiesOfClasses.add(uriListAndUriList);
					break;
				}
			}
		}
		//Instant end0 = Instant.now();
		//System.out.println("Running time for listCombinationPropertiesClassRelationshipsPropertiesOfClasses: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return listCombinationPropertiesClassRelationshipsPropertiesOfClasses;
	}
}

