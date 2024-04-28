package Asupprimer;

import java.io.StringReader;
import java.util.ArrayList;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

import profiling.util.ProfilingConf;
import profiling.util.Uri;
import profiling.util.UriAndListUriListAndListUriList;
import profiling.util.UriAndUri;
import profiling.util.UriAndUriAndUriAndNumber;
import profiling.util.UriAndUriList;
import profiling.util.UriListAndUriAndUriListList;

public class MakeDescriptionModelWithDataProperties {
	
	// Création du modèle de description
	public static OntModel  makeModel(OntModel descriptionModelTemp, ArrayList<UriListAndUriAndUriListList> listCombinationPropertiesClassRelationshipsPropertiesOfClasses, ArrayList<UriAndUri> listMostUsedPropertyType) {

		OntModel descriptionModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		descriptionModel.add(descriptionModelTemp);

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

		ArrayList<UriAndListUriListAndListUriList> listOfDatatypeProperties = new ArrayList<UriAndListUriListAndListUriList>();
		ArrayList<UriListAndUriAndUriListList> listCombinationPropertiesClassRelationshipsPropertiesOfClassesTemp = new ArrayList<UriListAndUriAndUriListList>();
		ArrayList<String> listOfProperty = new ArrayList<String>();

		// Instant start0 = Instant.now();	
	    
		listCombinationPropertiesClassRelationshipsPropertiesOfClassesTemp.addAll(listCombinationPropertiesClassRelationshipsPropertiesOfClasses);

		for (UriListAndUriAndUriListList resource : listCombinationPropertiesClassRelationshipsPropertiesOfClasses) {
			for (UriAndUriList resource2 : resource.getUriAndUriListList()) {
				String uriProperty = resource2.getUri().toString();
				// Si propriété pas encore traitée
				if (!listOfProperty.contains(uriProperty)) {
					listOfProperty.add(uriProperty);
					ArrayList<ArrayList<Uri>> listUriListDomain = new ArrayList<ArrayList<Uri>>();
					ArrayList<ArrayList<Uri>> listUriListRange = new ArrayList<ArrayList<Uri>>();
					for (UriListAndUriAndUriListList resourceTemp : listCombinationPropertiesClassRelationshipsPropertiesOfClassesTemp) {
						ArrayList<Uri> uriListDomain = new ArrayList<Uri>();
						for (int i = 0; i < resourceTemp.getUriList().size(); i++) {
							uriListDomain.add(new Uri(resourceTemp.getUriList().get(i).getUri()));
						}
						for (UriAndUriList resourceTemp2 : resourceTemp.getUriAndUriListList()) {
							if (resourceTemp2.getUri().toString() == uriProperty) {
								if (!listUriListDomain.contains(uriListDomain)) {
									listUriListDomain.add(uriListDomain);
								}
								if (!listUriListRange.contains(resourceTemp2.getUriList())) {
									listUriListRange.add(resourceTemp2.getUriList());
								}
							}
						}
					}
					UriAndListUriListAndListUriList uriAndUriListAndUriList = new UriAndListUriListAndListUriList(uriProperty, listUriListDomain, listUriListRange);
					listOfDatatypeProperties.add(uriAndUriListAndUriList);
				}
			}
		}
		StringBuilder datatypePropertiesBuilder = new StringBuilder();
		datatypePropertiesBuilder.append("@prefix dsp: <" + dsp + "> .");
		datatypePropertiesBuilder.append("@prefix rdf: <" + rdf + "> .");
		datatypePropertiesBuilder.append("@prefix owl: <" + owl + "> .");
		datatypePropertiesBuilder.append("@prefix rdfs: <" + rdfs + "> .");

		// Pour les annotations propriétés qui ne supportent pas les unions annonymes
		// On prépare une liste des différentes unions possibles pour éviter les doublons
		ArrayList<String> classUnionList = new ArrayList<String>();
		ArrayList<String> classUnionAndNumberList = new ArrayList<String>(); 
		Integer UnionNumber = 1;
		for (UriAndListUriListAndListUriList resource : listOfDatatypeProperties) {
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
				if (!classUnionList.contains(classUnionBuilder.toString())) {
					classUnionList.add(classUnionBuilder.toString());
					classUnionBuilder.append("*" + UnionNumber);
					classUnionAndNumberList.add(classUnionBuilder.toString());
					UnionNumber++;
				}
			}
		}
		

		for (UriAndListUriListAndListUriList resource : listOfDatatypeProperties) {
			datatypePropertiesBuilder.append(buildTurtleString(resource.getUri(), resource.getListUriList1(), resource.getListUriList2(), listMostUsedPropertyType, classUnionAndNumberList));
		}
		// Lecture de la séquence Turtle depuis la chaîne de caractères
        descriptionModel.read(new StringReader(datatypePropertiesBuilder.toString()), null, "TURTLE");

		
		// Instant end0 = Instant.now();
		// System.out.println("Running time for making description model: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return descriptionModel;
	}

	private static String buildTurtleString(String uriProperty, ArrayList<ArrayList<Uri>> listUriListDomain, ArrayList<ArrayList<Uri>> listUriListRange, ArrayList<UriAndUri> listMostUsedPropertyType, ArrayList<String> classUnionAndNumberList) {
        StringBuilder turtleBuilder = new StringBuilder();
		String owl = ProfilingConf.owl;
		String rdf = ProfilingConf.rdf;
		String dsp = ProfilingConf.dsp;

        turtleBuilder.append("<" + uriProperty + ">");
		String knownPropertyType = "";
		for (UriAndUri propertyAndType : listMostUsedPropertyType) {
			if (propertyAndType.getUri1().toString().equals(uriProperty)) {
				knownPropertyType = propertyAndType.getUri2().toString();
				break;
			}
		}
		if ( knownPropertyType != "" ) {
			turtleBuilder.append(" a <" + knownPropertyType + "> ");
		} else {
			turtleBuilder.append(" a <" + owl + "DatatypeProperty>");
			knownPropertyType = owl + "DatatypeProperty";
		}

		String knownPropertyTypeWithoutURL = knownPropertyType.replace(owl, "");
		knownPropertyTypeWithoutURL = knownPropertyTypeWithoutURL.replace(rdf, "");
	    
		switch(knownPropertyTypeWithoutURL){
			
			case "DatatypeProperty": 
				turtleBuilder.append(" ; ");
				// Pour le domaine de la datatype propriété 
				turtleBuilder.append(" rdfs:domain ");
				if (listUriListDomain.size() == 1) { // il n'y a qu'une liste de classes pour le domain
					for (ArrayList<Uri> uriListDomain : listUriListDomain) {
						if (uriListDomain.size() == 1) { // dans cette unique liste de classes il n'y a qu'une classe.
							turtleBuilder.append("<" + uriListDomain.get(0).getUri() + "> ");
						} else {
							Boolean first = true;
							for (Uri uriDomain : uriListDomain) {
								if (!first) {
									turtleBuilder.append(" , ");
								} else {
									first = false;
								}
								turtleBuilder.append("<" + uriDomain.getUri() + "> ");
							}
						}	 
					}	
				} else {
					turtleBuilder.append(" [ rdf:type owl:Class ;");
					turtleBuilder.append(" owl:unionOf ( ");
					for (ArrayList<Uri> uriListDomain : listUriListDomain) {
						if (uriListDomain.size() == 1) {
							turtleBuilder.append("<" + uriListDomain.get(0).getUri() + "> ");
						} else {
							turtleBuilder.append(" [ rdf:type owl:Class ; owl:intersectionOf ( ");
							for (Uri uriDomain : uriListDomain) {
								turtleBuilder.append("<"+ uriDomain.getUri() + "> ");
							}
							turtleBuilder.append(" ) ");
							turtleBuilder.append(" ] ");				
						}	 
					}
					turtleBuilder.append(" ) ");	
					turtleBuilder.append(" ]  ");
				}
					
				// Pour le range de la datatype propriété 
				
				if (listUriListRange.size() == 1) {
					for (ArrayList<Uri> uriListRange : listUriListRange) {
						if (uriListRange.size() != 0) {
							if (uriListRange.size() == 1) {
								turtleBuilder.append("; rdfs:range ");
								turtleBuilder.append("<" + uriListRange.get(0).getUri() + ">");
								turtleBuilder.append(" . ");	
							} else {
								turtleBuilder.append("; rdfs:range ");
								Boolean first = true;
								for (Uri uriRange : uriListRange) {
									if (!first) {
										turtleBuilder.append(" , ");
									} else {
										first = false;
									}
									turtleBuilder.append("<" + uriRange.getUri() + "> ");
								}	
								turtleBuilder.append(" . ");
							}	 
						} else {
							turtleBuilder.append(" . ");	
						}	
					}
				} else {
					turtleBuilder.append("; rdfs:range ");
					turtleBuilder.append(" [ rdf:type owl:Class ;");
					turtleBuilder.append(" owl:unionOf ( ");
					for (ArrayList<Uri> uriListRange : listUriListRange) {
						if (uriListRange.size() == 1) {
							turtleBuilder.append("<" + uriListRange.get(0).getUri() + "> ");
						} else {
							turtleBuilder.append(" [ rdf:type owl:Class ; owl:intersectionOf ( ");
							for (Uri uriRange : uriListRange) {
								turtleBuilder.append("<"+ uriRange.getUri() + ">");
							}
							turtleBuilder.append(" ) ");
							turtleBuilder.append(" ] ");
						}	 
					}
					turtleBuilder.append(" ] . ");	
				}
			break;
		
			case "AnnotationProperty":
				StringBuilder classUnionTempBuilder = new StringBuilder();
				String UnionNumberString = "";
				turtleBuilder.append(" ; ");
				// Pour le domaine de la datatype propriété 
				turtleBuilder.append(" rdfs:domain ");
				if (listUriListDomain.size() == 1) { // il n'y a qu'une liste de classes pour le domain
					for (ArrayList<Uri> uriListDomain : listUriListDomain) {
						if (uriListDomain.size() == 1) { // dans cette unique liste de classes il n'y a qu'une classe.
							turtleBuilder.append("<" + uriListDomain.get(0).getUri() + ">");
							turtleBuilder.append(" ");	
						} else {
							Boolean first = true;
							for (Uri uriDomain : uriListDomain) {
								if (!first) {
									turtleBuilder.append(" , ");
								} else {
									first = false;
								}
								turtleBuilder.append("<" + uriDomain.getUri() + "> ");
							}
						}	 
					}	
				} else {
					// On est obliger de créer une classe équivalente avec l'union des listes
					StringBuilder classUnionBuilder = new StringBuilder();
					Integer numberUriList = 0;
					for (ArrayList<Uri> uriList : listUriListDomain) {
						numberUriList++;
						if (numberUriList > 1) {
							classUnionBuilder.append("|");
						}
						classUnionBuilder.append(uriList.toString());
					}
					String classUnionBuilderString = classUnionBuilder.toString();
					if (numberUriList > 1) { // Pour eliminer les cas ou il n'y a pas d'union
						// On recherche dans classUnionAndNumberList si cette union existe et si oui on récupére son numéro
						for (String classUnionAndNumber : classUnionAndNumberList) {
							String[] classUnionAndNumberSplit = classUnionAndNumber.split("\\*");
							if (classUnionBuilderString.equals(classUnionAndNumberSplit[0])) {
								UnionNumberString = classUnionAndNumberSplit[1];
								break;
							}
						}
					}

					if (!UnionNumberString.equals("")) {
						
						// construction de l'union class
						classUnionTempBuilder.append("<" + dsp + "UnionClass" + UnionNumberString + "> rdf:type owl:Class ;");
						classUnionTempBuilder.append(" owl:equivalentClass ");
						classUnionTempBuilder.append(" [ rdf:type owl:Class ;");
						classUnionTempBuilder.append(" owl:unionOf ( ");
						for (ArrayList<Uri> uriListDomain : listUriListDomain) {
							if (uriListDomain.size() == 1) {
								classUnionTempBuilder.append("<" + uriListDomain.get(0).getUri() + "> ");
							} else {
								classUnionTempBuilder.append(" [ rdf:type owl:Class ; owl:intersectionOf ( ");
								for (Uri uriDomain : uriListDomain) {
									classUnionTempBuilder.append("<"+ uriDomain.getUri() + "> ");
								}
								classUnionTempBuilder.append(" ) ");
								classUnionTempBuilder.append(" ] ");				
							}	 
						}
						classUnionTempBuilder.append(" ) ");
						classUnionTempBuilder.append(" ] . ");		

						turtleBuilder.append(" <" + dsp + "UnionClass" + UnionNumberString + "> ");		
							
					} else {
						System.out.println("WTF !");
					}	
				}


				// Pour le range de la datatype propriété 
				
				if (listUriListRange.size() == 1) {
					for (ArrayList<Uri> uriListRange : listUriListRange) {
						if (uriListRange.size() != 0) {
							if (uriListRange.size() == 1) {
								turtleBuilder.append("; rdfs:range ");
								turtleBuilder.append("<" + uriListRange.get(0).getUri() + ">");
								turtleBuilder.append(" . ");	
							} else {
								turtleBuilder.append("; rdfs:range ");
								Boolean first = true;
								for (Uri uriRange : uriListRange) {
									if (!first) {
										turtleBuilder.append(" , ");
									} else {
										first = false;
									}
									turtleBuilder.append("<" + uriRange.getUri() + "> ");
								}	
								turtleBuilder.append(" . ");		
							}	 
						} else {
							turtleBuilder.append(" . ");	
						}	
					}
				} else {
					turtleBuilder.append("; rdfs:range ");
					turtleBuilder.append(" [ rdf:type owl:Class ;");
					turtleBuilder.append(" owl:unionOf ( ");
					for (ArrayList<Uri> uriListRange : listUriListRange) {
						if (uriListRange.size() == 1) {
							turtleBuilder.append("<" + uriListRange.get(0).getUri() + "> ");
						} else {
							turtleBuilder.append(" [ rdf:type owl:Class ; owl:intersectionOf ( ");
							for (Uri uriRange : uriListRange) {
								turtleBuilder.append("<"+ uriRange.getUri() + ">");
							}
							turtleBuilder.append(" ) ");
							turtleBuilder.append(" ] ");
						}	 
					}
					turtleBuilder.append(" ] . ");
				}
				// On ajoute l'union non anonyme
				turtleBuilder.append(classUnionTempBuilder.toString());
			break;
		
			default:
			    turtleBuilder.append(" . ");	
			break;
		}
		
        // Retourne le resultat en tant que chaîne de caractères
		// System.out.println(turtleBuilder.toString());
        return turtleBuilder.toString();
    }

	static class UriAndUriAndUriAndNumberComparator implements java.util.Comparator<UriAndUriAndUriAndNumber> {
		@Override
		public int compare(UriAndUriAndUriAndNumber a, UriAndUriAndUriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}
