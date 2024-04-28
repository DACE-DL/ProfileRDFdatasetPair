package profiling.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public class MakeDescriptionModelWithoutClassRelationships {
	
	// Création du modèle de description
	public static OntModel  makeModel(ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClass, ArrayList<UriAndNumber> listClassMostUsed, ArrayList<UriAndUriListAndNumberListAndNumber> listMostUsedPropertyWithClassDomain,
	 ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listMostUsedPropertyWithDatatypeAndClassRange,
	 ArrayList<UriAndNumber> listMostUsedObjectProperty, ArrayList<UriAndNumber>listMostUsedDatatypeProperty, ArrayList<UriAndNumber> listMostUsedAnnotationProperty) {

		// Instant start0 = Instant.now();	
		OntModel descriptionModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String skos = ProfilingConf.skos;
		String owl = ProfilingConf.owl;
		String rdfs = ProfilingConf.rdfs;
		descriptionModel.setNsPrefix("dsp", dsp);
		descriptionModel.setNsPrefix("rdf", rdf);
		descriptionModel.setNsPrefix("skos", skos);
		descriptionModel.setNsPrefix("owl", owl);
		descriptionModel.setNsPrefix("rdfs", rdfs);

		StringBuilder relationshipsBuilder = new StringBuilder();
		relationshipsBuilder.append("@prefix rdf: <" + rdf + "> .");
		relationshipsBuilder.append("@prefix owl: <" + owl + "> .");
		relationshipsBuilder.append("@prefix rdfs: <" + rdfs + "> .");

		ArrayList<UriAndListUriListAndListUriList> listOfPropertyDomainAndRange = new ArrayList<UriAndListUriListAndListUriList>();
		ArrayList<String> listOfPropertyString = new ArrayList<String>();
		ArrayList<Uri> listOfClasses = new ArrayList<Uri>();
		ArrayList<String> listOfClassesString = new ArrayList<String>();
		Integer maxOfUnion = 11;

		// Instant start0 = Instant.now();	

		// On récupère la liste des classes (y compris les classes créées pour les combinaisons de propriétées pour des ressources sans classes)
		for (UriListAndUriList combinationPropertiesWithNewClass : listCombinationPropertiesWithNewClass) {
			for (Uri uriClass : combinationPropertiesWithNewClass.getUriList1()) {
				// Si Classe pas encore traitée
				if (!listOfClassesString.contains(uriClass.toString())) {
					listOfClassesString.add(uriClass.toString());
					listOfClasses.add(uriClass);
				}
			}
		}	
		// On déclare toute les classes
		relationshipsBuilder.append(buildTurtleClassString(listOfClasses, listCombinationPropertiesWithNewClass, listMostUsedAnnotationProperty));



		// for (UriAndUriListAndNumberListAndUriListAndNumberListAndNumber resourcePropertyWithDatatypeAndClassRange : listMostUsedPropertyWithDatatypeAndClassRange) {
		// 	String uriPropertyDatatypeAndClassRange = resourcePropertyWithDatatypeAndClassRange.getUri().toString();
		// 	// Si propriété pas encore traitée
		// 	if (!listOfPropertyString.contains(uriPropertyDatatypeAndClassRange)) {
		// 		listOfPropertyString.add(uriPropertyDatatypeAndClassRange);
		// 		ArrayList<ArrayList<Uri>> listUriListRange = new ArrayList<ArrayList<Uri>>();
		// 		ArrayList<ArrayList<Uri>> listUriListDomain = new ArrayList<ArrayList<Uri>>();
		// 		if (resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList1().size() > 0) {
		// 			for (UriListAndNumber uriListAndNumber : resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList1()) {
		// 			listUriListRange.add(uriListAndNumber.getUriList());
		// 			}
		// 		} else {
		// 			for (UriListAndNumber uriListAndNumber : resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList2()) {
		// 				listUriListRange.add(uriListAndNumber.getUriList());
		// 			}
		// 		}

		// 		for (UriAndUriListAndNumberListAndNumber resourcePropertyWithClassDomain : listMostUsedPropertyWithClassDomain) {
		// 			String uriPropertyClassDomain = resourcePropertyWithClassDomain.getUri().toString();
		// 			if (uriPropertyClassDomain.equals(uriPropertyDatatypeAndClassRange)) {
		// 				if (resourcePropertyWithClassDomain.getUriListAndNumberList().size() > 0) {
		// 					for (UriListAndNumber uriListAndNumber : resourcePropertyWithClassDomain.getUriListAndNumberList()) {
		// 					listUriListDomain.add(uriListAndNumber.getUriList());
		// 					}
		// 				}
		// 				break;
		// 			}	
		// 		}
		// 		UriAndListUriListAndListUriList uriAndUriListAndUriList = new UriAndListUriListAndListUriList(uriPropertyDatatypeAndClassRange, listUriListDomain, listUriListRange);
		// 		listOfPropertyDomainAndRange.add(uriAndUriListAndUriList);
		// 		// System.out.println(uriPropertyDatatypeAndClassRange);	
		// 		// System.out.println(listUriListDomain.toString());	
		// 		// System.out.println(listUriListRange.toString());	
		// 	}
		
		// }

		for (UriAndUriListAndNumberListAndUriListAndNumberListAndNumber resourcePropertyWithDatatypeAndClassRange : listMostUsedPropertyWithDatatypeAndClassRange) {
			String uriPropertyDatatypeAndClassRange = resourcePropertyWithDatatypeAndClassRange.getUri().toString();
			// Si propriété pas encore traitée
			if (!listOfPropertyString.contains(uriPropertyDatatypeAndClassRange)) {
				listOfPropertyString.add(uriPropertyDatatypeAndClassRange);
				ArrayList<UriList> listUriListDomainTemp = new ArrayList<UriList>();
				ArrayList<UriList> listUriListRangeTemp = new ArrayList<UriList>();
				ArrayList<ArrayList<Uri>> listUriListDomain = new ArrayList<ArrayList<Uri>>();
				ArrayList<ArrayList<Uri>> listUriListRange = new ArrayList<ArrayList<Uri>>();
				if (resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList1().size() > 0) {
					for (UriListAndNumber uriListAndNumber : resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList1()) {
						listUriListRangeTemp.add(new UriList(uriListAndNumber.getUriList()));
					}
				} else {
					for (UriListAndNumber uriListAndNumber : resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList2()) {
						listUriListRangeTemp.add(new UriList(uriListAndNumber.getUriList()));
					}
				}

				for (UriAndUriListAndNumberListAndNumber resourcePropertyWithClassDomain : listMostUsedPropertyWithClassDomain) {
					String uriPropertyClassDomain = resourcePropertyWithClassDomain.getUri().toString();
					if (uriPropertyClassDomain.equals(uriPropertyDatatypeAndClassRange)) {
						if (resourcePropertyWithClassDomain.getUriListAndNumberList().size() > 0) {
							for (UriListAndNumber uriListAndNumber : resourcePropertyWithClassDomain.getUriListAndNumberList()) {
								listUriListDomainTemp.add(new UriList(uriListAndNumber.getUriList()));
							}
						}
						break;
					}	
				}
				
				Collections.sort(listUriListDomainTemp, new UriListComparator());

				for(UriList urilist : listUriListDomainTemp) {
					listUriListDomain.add(urilist.getUriList());
				}
				Collections.sort(listUriListRangeTemp, new UriListComparator());
				for(UriList urilist : listUriListRangeTemp) {
					listUriListRange.add(urilist.getUriList());
				}
				UriAndListUriListAndListUriList uriAndListUriListAndListUriList = new UriAndListUriListAndListUriList(uriPropertyDatatypeAndClassRange, listUriListDomain, listUriListRange);
				listOfPropertyDomainAndRange.add(uriAndListUriListAndListUriList);
			}
		}

		// On déclare toute les classes
		relationshipsBuilder.append(buildTurtleClassString(listClassMostUsed));
	
		// On s'occupe maintenant des object properties
		relationshipsBuilder.append(profiling.util.ProfilingUtil.buildTurtleObjectPropertyString(listMostUsedObjectProperty, listOfPropertyDomainAndRange));

		// On s'occupe maintenant des datatype properties
		relationshipsBuilder.append(profiling.util.ProfilingUtil.buildTurtleDatatypePropertyString(listMostUsedDatatypeProperty, listOfPropertyDomainAndRange));  

		// On s'occupe maintenant des annotation properties

		// Pour les annotations propriétés qui ne supportent pas les unions annonymes
		// On prépare une liste des différentes unions possibles pour éviter les doublons
		ArrayList<String> classUnionList = new ArrayList<String>();
		ArrayList<String> classUnionAndNumberList = new ArrayList<String>(); 
		Integer UnionNumber = 1;
		for (UriAndListUriListAndListUriList resource : listOfPropertyDomainAndRange) {
			StringBuilder classUnionBuilder = new StringBuilder();
			Integer numberUriList = 0;
			for (ArrayList<Uri> uriList : resource.getListUriList1()) {
				numberUriList++;
				if (numberUriList > 1) {
                    classUnionBuilder.append("|");
				}
				classUnionBuilder.append(uriList.toString());
			}
			if (numberUriList > 1) { // Pour eliminer les cas ou il n'y a pas d'union
				if (numberUriList < maxOfUnion) { // Si trop de listes pour l'union
					if (!classUnionList.contains(classUnionBuilder.toString())) {
						classUnionList.add(classUnionBuilder.toString());
						classUnionBuilder.append("*" + UnionNumber);
						classUnionAndNumberList.add(classUnionBuilder.toString());
						UnionNumber++;
					}
				}	
			}
		}
		relationshipsBuilder.append(profiling.util.ProfilingUtil.buildTurtleAnnotationPropertyString(listMostUsedAnnotationProperty, listOfPropertyDomainAndRange, maxOfUnion, classUnionAndNumberList));    
		
		// Lecture de la séquence Turtle depuis la chaîne de caractères
        descriptionModel.read(new StringReader(relationshipsBuilder.toString()), null, "TURTLE");

		// Instant end0 = Instant.now();
		// System.out.println("Running time for making description model: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return descriptionModel;
	}

	private static String buildTurtleClassString( ArrayList<UriAndNumber> listClassMostUsed ) {
        StringBuilder turtleBuilder = new StringBuilder();
		
		for (UriAndNumber uriAndNumber : listClassMostUsed) {
				turtleBuilder.append("<"+ uriAndNumber.getUri() + "> a owl:Class . ");
		}
		// System.out.println(turtleBuilder.toString());	
        // Retourne le resultat en tant que chaîne de caractères
        return turtleBuilder.toString();
    }
	private static String buildTurtleClassString( ArrayList<Uri>  listOfClasses, ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClass, ArrayList<UriAndNumber> listMostUsedAnnotationProperty) {
        StringBuilder turtleBuilder = new StringBuilder();
		String dsp = ProfilingConf.dsp;
		for (Uri uriClass : listOfClasses) {
			turtleBuilder.append("<"+ uriClass.getUri() + "> a owl:Class . ");
			if (uriClass.getUri().toString().contains(dsp + "Class-")) {
				turtleBuilder.append(buildTurtleIntentionClassString(uriClass.getUri().toString(), listCombinationPropertiesWithNewClass, listMostUsedAnnotationProperty));
			}	
		}
		// System.out.println(turtleBuilder.toString());	
        // Retourne le resultat en tant que chaîne de caractères
        return turtleBuilder.toString();
    }
	

	private static String buildTurtleIntentionClassString(String uriClass, ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClass, ArrayList<UriAndNumber> listMostUsedAnnotationProperty) {
        StringBuilder turtleBuilder = new StringBuilder();
		String dc = ProfilingConf.dc;
		for (UriListAndUriList combinationPropertiesWithNewClass : listCombinationPropertiesWithNewClass) {
			if (combinationPropertiesWithNewClass.getUriList1().get(0).toString().equals(uriClass)) {
				ArrayList<Uri> listCombinaisonPropertiesWithoutAnnotationProperties = new ArrayList<Uri>();
				ArrayList<Uri> listAllCombinaisonProperties = new ArrayList<Uri>();
				for (Uri uriProperty : combinationPropertiesWithNewClass.getUriList2()) {
					listAllCombinaisonProperties.add(uriProperty);
					Boolean annotationProperty = false;
					for (UriAndNumber uriAndNumber : listMostUsedAnnotationProperty) {
						if (uriAndNumber.getUri().toString().equals(uriProperty.toString())) {
							annotationProperty = true;
							break;
						}
					}	
					if (!annotationProperty) {
						listCombinaisonPropertiesWithoutAnnotationProperties.add(uriProperty);
					}		
				}	
				
				if (listCombinaisonPropertiesWithoutAnnotationProperties.size() > 0) {
					turtleBuilder.append("<"+ uriClass + "> rdfs:subClassOf ");
					Boolean first = true;
					for (Uri uriProperty : listCombinaisonPropertiesWithoutAnnotationProperties) {	
						if (!first) {
							turtleBuilder.append(" , ");
						} else {
							first = false;
						}
						turtleBuilder.append(" [ rdf:type owl:Restriction ; ");
						turtleBuilder.append(" ; owl:onProperty <"+ uriProperty.getUri() + "> ; ");
						//turtleBuilder.append(" owl:someValuesFrom owl:Thing ");
						turtleBuilder.append(" owl:minCardinality 1 ");
						turtleBuilder.append(" ]  ");    
						}
					turtleBuilder.append("  . ");
				}
				if (listAllCombinaisonProperties.size() > 0) {
					turtleBuilder.append("<"+ uriClass + "> <" + dc +"description> ");
					turtleBuilder.append("\"");
					turtleBuilder.append( uriClass + " is the class of individuals described with the following characteristics: ");
					Boolean first = true;
					for (Uri uriProperty : listAllCombinaisonProperties) {	
						if (!first) {
							turtleBuilder.append(", ");
						} else {
							first = false;
						}
						turtleBuilder.append(uriProperty);
					}	
					turtleBuilder.append("\"");
					turtleBuilder.append("  . ");
				}
			}		
		}
		// System.out.println(turtleBuilder.toString());	
        // Retourne le resultat en tant que chaîne de caractères
        return turtleBuilder.toString();
    }
	
	static class UriListComparator implements java.util.Comparator<UriList> {
		@Override
		public int compare(UriList a, UriList b) {
			 // Compare les tailles des listes
			 int sizeComparison = Integer.compare(a.size(), b.size());
			 if (sizeComparison != 0) {
				 return sizeComparison;
			 }
			 // Compare les éléments de chaque liste
			 for (int i = 0; i < a.size(); i++) {
				 Object objA = a.get(i);
				 Object objB = b.get(i);
				 if (objA == null && objB == null) {
					 continue;
				 } else if (objA == null) {
					 return -1; // a est considéré plus petit car il contient un élément null
				 } else if (objB == null) {
					 return 1; // b est considéré plus petit car il contient un élément null
				 }
				 int elementComparison = objA.toString().compareTo(objB.toString());
				 if (elementComparison != 0) {
					 return elementComparison;
				 }
			 }
			 return 0; // Les listes sont identiques
		}
	}
}
