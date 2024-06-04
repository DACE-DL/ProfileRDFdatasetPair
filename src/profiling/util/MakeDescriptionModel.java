package profiling.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public class MakeDescriptionModel {
	
	// Création du modèle de description
	public static OntModel  makeModel(ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships,
	 ArrayList<UriListAndUriList> listCombinationPropertiesClassRelationshipsPropertiesOfClasses,
	 ArrayList<UriListAndUriList> listCombinationPropertiesWithNewClass, ArrayList<UriAndUriListAndNumberListAndNumber> listMostUsedPropertyWithClassDomain,
	 ArrayList<UriAndUriListAndNumberListAndUriListAndNumberListAndNumber> listMostUsedPropertyWithDatatypeAndClassRange,
	 ArrayList<UriAndNumber> listMostUsedObjectProperty, ArrayList<UriAndNumber>listMostUsedDatatypeProperty,
	 ArrayList<UriAndNumber> listMostUsedAnnotationProperty, ArrayList<UriAndNumber> listMostUsedRDFproperty) {
		
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

		ArrayList<UriAndListUriListAndListUriList> listOfRelationships = new ArrayList<UriAndListUriListAndListUriList>();
		ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationshipsTemp = new ArrayList<UriListAndUriAndUriListAndNumber>();
		ArrayList<String> listOfProperty = new ArrayList<String>();
		ArrayList<UriAndListUriListAndListUriList> listOfPropertyDomainAndRange = new ArrayList<UriAndListUriListAndListUriList>();
		ArrayList<String> listOfPropertyString = new ArrayList<String>();
		Integer maxOfUnion = 20;

		// Instant start0 = Instant.now();	
		System.out.println("MakeDescriptionModel !");
	    
		listCombinationPropertiesClassRelationshipsTemp.addAll(listCombinationPropertiesClassRelationships);

		// Pour chaque propriétés on créé la liste de liste de domaine et la liste de liste de range
		// On part de la liste des relation entre classes.
		for (UriListAndUriAndUriListAndNumber resource : listCombinationPropertiesClassRelationships) {
			String uriProperty = resource.getUri().toString();
			// Si propriété pas encore traitée
			if (!listOfProperty.contains(uriProperty)) {
				listOfProperty.add(uriProperty);
				ArrayList<ArrayList<Uri>> listUriListRange = new ArrayList<ArrayList<Uri>>();
				ArrayList<ArrayList<Uri>> listUriListDomain = new ArrayList<ArrayList<Uri>>();
				for (UriListAndUriAndUriListAndNumber resourceTemp : listCombinationPropertiesClassRelationshipsTemp) {
					// System.out.println("resourceTemp.getUri() :" + resourceTemp.getUri().toString());
					if (resourceTemp.getUri().toString().equals(uriProperty)) {
						if (!listUriListDomain.contains(resourceTemp.getUriList1())) {
							listUriListDomain.add(resourceTemp.getUriList1());
						}
						if (!listUriListRange.contains(resourceTemp.getUriList2())) {
							listUriListRange.add(resourceTemp.getUriList2());
						}
					}
				}
				UriAndListUriListAndListUriList uriAndUriListAndUriList = new UriAndListUriListAndListUriList(uriProperty, listUriListDomain, listUriListRange);
				listOfRelationships.add(uriAndUriListAndUriList);
			}
		}
		StringBuilder relationshipsBuilder = new StringBuilder();
		relationshipsBuilder.append("@prefix rdf: <" + rdf + "> .");
		relationshipsBuilder.append("@prefix owl: <" + owl + "> .");
		relationshipsBuilder.append("@prefix rdfs: <" + rdfs + "> .");
		// On déclare toutes les classes
		for (UriAndListUriListAndListUriList resource : listOfRelationships) {
			relationshipsBuilder.append(profiling.util.ProfilingUtil.buildTurtleClassString(resource.getListUriList1(), resource.getListUriList2(), listCombinationPropertiesWithNewClass, listMostUsedAnnotationProperty ));
			//System.out.println("resource.getListUriList1() :" + resource.getListUriList1().toString());
			//System.out.println("resource.getListUriList2() :" + resource.getListUriList2().toString());
		}
		// System.out.println("relationshipsBuilder : " + relationshipsBuilder);
		// On s'occupe maintenant de toutes les propriétés liées aux classes concernées dans les relations 

		// On récupère les domaines et ranges des propriétés les plus utilisées pour créer la liste listOfPropertyDomainAndRange
		for (UriAndUriListAndNumberListAndUriListAndNumberListAndNumber resourcePropertyWithDatatypeAndClassRange : listMostUsedPropertyWithDatatypeAndClassRange) {
			String uriPropertyDatatypeAndClassRange = resourcePropertyWithDatatypeAndClassRange.getUri().toString();
			// Si propriété pas encore traitée
			if (!listOfPropertyString.contains(uriPropertyDatatypeAndClassRange)) {
				listOfPropertyString.add(uriPropertyDatatypeAndClassRange);
				// if (uriPropertyDatatypeAndClassRange.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#rest")) {
				// 	System.out.println("property Ok ");
				// }	
				ArrayList<UriList> listUriListDomainTemp = new ArrayList<UriList>();
				ArrayList<UriList> listUriListRangeTemp = new ArrayList<UriList>();
				ArrayList<ArrayList<Uri>> listUriListDomain = new ArrayList<ArrayList<Uri>>();
				ArrayList<ArrayList<Uri>> listUriListRange = new ArrayList<ArrayList<Uri>>();
				if (resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList1().size() > 0) {
					for (UriListAndNumber uriListAndNumber : resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList1()) {
						listUriListRangeTemp.add(new UriList(uriListAndNumber.getUriList()));
					}
				} else {
					if (resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList2().size() > 0) {
						for (UriListAndNumber uriListAndNumber : resourcePropertyWithDatatypeAndClassRange.getUriListAndNumberList2()) {
							listUriListRangeTemp.add(new UriList(uriListAndNumber.getUriList()));
						}
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
		



		// De nouvelles classes ayant été crées pour étudier les relations entre individus définis par une combinaison de caractéristiques
		// on met à jour la liste listOfPropertyDomainAndRange
		for (UriAndListUriListAndListUriList relationships : listOfRelationships) {
			String uriRelationship = relationships.getUri();
			for (UriAndListUriListAndListUriList propertydomainandrange : listOfPropertyDomainAndRange) {
				String uriProperty = propertydomainandrange.getUri();
				if (uriProperty.equals(uriRelationship)) {
					// if (uriProperty.equals("http://www.bbc.co.uk/ontologies/coreconcepts/primaryTopic")) {
					// 	System.out.println("OK !");
					// }
					ArrayList<ArrayList<Uri>> listUriListRange = new ArrayList<ArrayList<Uri>>();
					ArrayList<ArrayList<Uri>> listUriListDomain = new ArrayList<ArrayList<Uri>>();
					// on récupère les listes d'origine.
					listUriListDomain.addAll(propertydomainandrange.getListUriList1());
					listUriListRange.addAll(propertydomainandrange.getListUriList2());
					for (ArrayList<Uri> relationshipsDomains : relationships.getListUriList1()) {
						if (!listUriListDomain.contains(relationshipsDomains)) {
							listUriListDomain.add(relationshipsDomains);
						}
					}	
					for (ArrayList<Uri> relationshipsDomains : relationships.getListUriList2()) {
						if (!listUriListRange.contains(relationshipsDomains)) {
							listUriListRange.add(relationshipsDomains);
						}
					}	
					// On met à jour la liste
					propertydomainandrange.setListUriList1(listUriListDomain);
					propertydomainandrange.setListUriList2(listUriListRange);
					break;
				}
			}	
		
		}

		// Avec les nouvelles classes ayant été crées pour étudier les relations entre individus définis par une combinaison de caractéristiques,
		//  on a aussi créer de nouveaux domaines pour les propriétés des classes même celles qui ne sont pas impliquées dans les relations entre 
		//  classes !
		// On met à jour la liste listOfPropertyDomainAndRange
		for (UriListAndUriList uriListAndUriList :listCombinationPropertiesClassRelationshipsPropertiesOfClasses) {
			for (Uri property : uriListAndUriList.getUriList2()) {
				String uriPropertyOfClass = property.getUri().toString();
				for (UriAndListUriListAndListUriList propertydomainandrange : listOfPropertyDomainAndRange) {
					String uriProperty = propertydomainandrange.getUri();
					if (uriProperty.equals(uriPropertyOfClass)) {
						// if (uriProperty.equals("http://www.bbc.co.uk/ontologies/coreconcepts/primaryTopic")) {
						// 	System.out.println("OK !");
						// }
						ArrayList<ArrayList<Uri>> listUriListDomain = new ArrayList<ArrayList<Uri>>();
						// on récupère les listes d'origine.
						listUriListDomain.addAll(propertydomainandrange.getListUriList1());
						ArrayList<Uri> relationshipsDomains = uriListAndUriList.getUriList1(); 
						if (!listUriListDomain.contains(relationshipsDomains)) {
							listUriListDomain.add(relationshipsDomains);
						}
						// On met à jour la liste
						propertydomainandrange.setListUriList1(listUriListDomain);
						break;
					}
				}	
		
			}
		}

		// System.out.println("propertydomainandrange :");
		// for(UriAndListUriListAndListUriList propertydomainandrange : listOfPropertyDomainAndRange) {
		// 	if (propertydomainandrange.getUri().toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#rest")) {
		// 	System.out.println("property : ");
		// 	System.out.println(propertydomainandrange.getUri());
		// 	System.out.println("domain : ");
		// 	for(ArrayList<Uri> uriListdomaine : propertydomainandrange.getListUriList1()) {
		// 		System.out.println(uriListdomaine.toString());
		// 	}	
		// 	System.out.println("range : ");
		// 	for(ArrayList<Uri> uriListRange : propertydomainandrange.getListUriList2()) {
		// 		System.out.println(uriListRange.toString());
		// 	}	
		// 	}
		// }

		// On recupère les propriétés des classes concernées par des relations entres classes
		ArrayList<UriAndNumber> listObjectProperty = new ArrayList<UriAndNumber>();
		ArrayList<UriAndNumber> listDatatypeProperty = new ArrayList<UriAndNumber>();
		ArrayList<UriAndNumber> listAnnotationProperty = new ArrayList<UriAndNumber>();
		ArrayList<UriAndNumber> listRDFproperty = new ArrayList<UriAndNumber>();
		ArrayList<String> listOfPropertyString2 = new ArrayList<String>();
		for (UriListAndUriList ressource : listCombinationPropertiesClassRelationshipsPropertiesOfClasses) {
			for (Uri ressource2 : ressource.getUriList2()) {
				String uriProperty = ressource2.getUri();
				// Si propriété pas encore traitée
				if (!listOfPropertyString2.contains(uriProperty)) {
					listOfPropertyString2.add(uriProperty);
					for (UriAndNumber ressource3 : listMostUsedObjectProperty) {
						if (ressource3.getUri().toString().equals(uriProperty)) {
							listObjectProperty.add(ressource3);
							break;
						}
					}	
					for (UriAndNumber ressource4 : listMostUsedDatatypeProperty) {
						if (ressource4.getUri().toString().equals(uriProperty)) {
							listDatatypeProperty.add(ressource4);
							break;
						}
					}	
					for (UriAndNumber ressource5 : listMostUsedAnnotationProperty) {
						if (ressource5.getUri().toString().equals(uriProperty)) {
							listAnnotationProperty.add(ressource5);
							break;
						}
					}
					for (UriAndNumber ressource6 : listMostUsedAnnotationProperty) {
						if (ressource6.getUri().toString().equals(uriProperty)) {
							listRDFproperty.add(ressource6);
							break;
						}
					}		
				}	
			}
		}

		// On s'occupe maintenant des object properties
		relationshipsBuilder.append(profiling.util.ProfilingUtil.buildTurtleObjectPropertyString(listMostUsedObjectProperty, listOfPropertyDomainAndRange, maxOfUnion));

		// On s'occupe maintenant des datatype properties
		relationshipsBuilder.append(profiling.util.ProfilingUtil.buildTurtleDatatypePropertyString(listMostUsedDatatypeProperty, listOfPropertyDomainAndRange, maxOfUnion));  

		// On s'occupe maintenant des rdfs properties
		relationshipsBuilder.append(profiling.util.ProfilingUtil.buildTurtleRDFpropertyString(listMostUsedRDFproperty, listOfPropertyDomainAndRange, maxOfUnion));  

		// On s'occupe maintenant des annotation properties

		// Pour les annotations propriétés qui ne supportent pas les unions annonymes
		// On prépare une liste des différentes unions possibles pour éviter les doublons
		ArrayList<String> classUnionList = new ArrayList<String>();
		ArrayList<String> classUnionAndNumberList = new ArrayList<String>(); 
		Integer UnionNumber = 1;
		for (UriAndListUriListAndListUriList propertyDomainAndRange : listOfPropertyDomainAndRange) {
			StringBuilder classUnionBuilder = new StringBuilder();
			Integer numberUriList = 0;
			for (ArrayList<Uri> uriList : propertyDomainAndRange.getListUriList1()) {
				numberUriList++;
				if (numberUriList > 1) {
                    classUnionBuilder.append("|");
				}
				classUnionBuilder.append(uriList.toString());
			}
			if (numberUriList > 1) { // Pour eliminer les cas ou il n'y a pas d'union
				if (numberUriList < maxOfUnion) { // Si pas trop de listes pour l'union
					if (!classUnionList.contains(classUnionBuilder.toString())) {
						classUnionList.add(classUnionBuilder.toString());
						classUnionBuilder.append("*" + UnionNumber);
						classUnionAndNumberList.add(classUnionBuilder.toString());
						UnionNumber++;
					}
				}	
			}
			// On traite aussi les ranges
			StringBuilder classUnionBuilder2 = new StringBuilder();
			numberUriList = 0;
			for (ArrayList<Uri> uriList : propertyDomainAndRange.getListUriList2()) {
				numberUriList++;
				if (numberUriList > 1) {
                    classUnionBuilder2.append("|");
				}
				classUnionBuilder2.append(uriList.toString());
			}
			if (numberUriList > 1) { // Pour eliminer les cas ou il n'y a pas d'union
				if (numberUriList < maxOfUnion) { // Si pas trop de listes pour l'union
					if (!classUnionList.contains(classUnionBuilder2.toString())) {
						classUnionList.add(classUnionBuilder2.toString());
						classUnionBuilder2.append("*" + UnionNumber);
						classUnionAndNumberList.add(classUnionBuilder2.toString());
						UnionNumber++;
					}
				}	
			}
		}
		// System.out.println("classUnionAndNumberList :");
		// for(String classUnionAndNumber : classUnionAndNumberList) {
		// 	System.out.println(classUnionAndNumber);
		// }
		relationshipsBuilder.append(profiling.util.ProfilingUtil.buildTurtleAnnotationPropertyString(listMostUsedAnnotationProperty,
		 listOfPropertyDomainAndRange, maxOfUnion, classUnionAndNumberList));    
		
		//System.out.println(relationshipsBuilder.toString());
		
		// Lecture de la séquence Turtle depuis la chaîne de caractères
        descriptionModel.read(new StringReader(relationshipsBuilder.toString()), null, "TURTLE");

		// Instant end0 = Instant.now();
		// System.out.println("Running time for making description model: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return descriptionModel;
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

