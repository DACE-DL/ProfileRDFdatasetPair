package profiling.util;

import java.io.StringReader;
import java.util.ArrayList;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public class MakeDescriptionModel {
	
	// Création du modèle de description
	public static OntModel  makeModel(ArrayList<UriListAndUriAndUriListAndNumber> listCombinationPropertiesClassRelationships, ArrayList<UriListAndUriList2> listCombinationPropertiesWithNewClass) {
		
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

		// Instant start0 = Instant.now();	
	    
		listCombinationPropertiesClassRelationshipsTemp.addAll(listCombinationPropertiesClassRelationships);

		for (UriListAndUriAndUriListAndNumber resource : listCombinationPropertiesClassRelationships) {
			String uriProperty = resource.getUri().toString();
			// Si propriété pas encore traitée
			if (!listOfProperty.contains(uriProperty)) {
				listOfProperty.add(uriProperty);
				ArrayList<ArrayList<Uri>> listUriListRange = new ArrayList<ArrayList<Uri>>();
				ArrayList<ArrayList<Uri>> listUriListDomain = new ArrayList<ArrayList<Uri>>();
				for (UriListAndUriAndUriListAndNumber resourceTemp : listCombinationPropertiesClassRelationshipsTemp) {
					if (resourceTemp.getUri().toString() == uriProperty) {
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
		// On déclare toute les classes
		for (UriAndListUriListAndListUriList resource : listOfRelationships) {
			relationshipsBuilder.append(buildTurtleClassString(resource.getListUriList1(), resource.getListUriList2()));
		}
		// on s'occupe maintenant des objects properties  
		for (UriAndListUriListAndListUriList resource : listOfRelationships) {
			relationshipsBuilder.append(buildTurtleString(resource.getUri(), resource.getListUriList1(), resource.getListUriList2()));
		}
		// Lecture de la séquence Turtle depuis la chaîne de caractères
        descriptionModel.read(new StringReader(relationshipsBuilder.toString()), null, "TURTLE");

		
		// Instant end0 = Instant.now();
		// System.out.println("Running time for making description model: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return descriptionModel;
	}

	private static String buildTurtleString(String uriProperty, ArrayList<ArrayList<Uri>> listUriListDomain, ArrayList<ArrayList<Uri>> listUriListRange) {
        StringBuilder turtleBuilder = new StringBuilder();
        turtleBuilder.append("<" + uriProperty + ">");
		turtleBuilder.append(" a owl:ObjectProperty ;");
		turtleBuilder.append(" rdfs:domain ");
		if (listUriListDomain.size() == 1) { // il n'y a qu'une liste de classes pour le domain
			for (ArrayList<Uri> uriListDomain : listUriListDomain) {
				if (uriListDomain.size() == 1) { // dans cette unique liste de classes il n'y a qu'une classe.
					turtleBuilder.append("<" + uriListDomain.get(0).getUri() + "> ");
					turtleBuilder.append(" ; ");	
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
					turtleBuilder.append(" ; ");	
				}	 
			}	
		} else {
			turtleBuilder.append(" [ rdf:type owl:Class ;");
			turtleBuilder.append(" owl:unionOf ( ");
			for (ArrayList<Uri> uriListDomain : listUriListDomain) {
				if (uriListDomain.size() == 1) {
					//turtleBuilder.append(" [ rdf:type owl:Class ; owl:oneOf ( ");
					turtleBuilder.append("<" + uriListDomain.get(0).getUri() + "> ");
					//turtleBuilder.append(" ) ");
					//turtleBuilder.append(" ] ");		
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
			turtleBuilder.append(" ] ; ");	
		}
		
		
		
		
		turtleBuilder.append("rdfs:range ");
		if (listUriListRange.size() == 1) {
			for (ArrayList<Uri> uriListRange : listUriListRange) {
				if (uriListRange.size() == 1) {
					turtleBuilder.append("<" + uriListRange.get(0).getUri() + "> ");
					turtleBuilder.append(" . ");	
				} else {
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
			}
		} else {
			turtleBuilder.append(" [ rdf:type owl:Class ;");
			turtleBuilder.append(" owl:unionOf ( ");
			for (ArrayList<Uri> uriListRange : listUriListRange) {
				if (uriListRange.size() == 1) {
					//turtleBuilder.append(" [ rdf:type owl:Class ; owl:oneOf ( ");
					turtleBuilder.append("<" + uriListRange.get(0).getUri() + "> ");
					//turtleBuilder.append(" ) ");
					//turtleBuilder.append(" ] ");
				} else {
					turtleBuilder.append(" [ rdf:type owl:Class ; owl:intersectionOf ( ");
					for (Uri uriRange : uriListRange) {
						turtleBuilder.append("<"+ uriRange.getUri() + "> ");
					}
					turtleBuilder.append(" ) ");
					turtleBuilder.append(" ] ");
				}	 
			}
			turtleBuilder.append(" ) ");
			turtleBuilder.append(" ] . ");	
		}
		
		// System.out.println(turtleBuilder.toString());	
        // Retourne le resultat en tant que chaîne de caractères
        return turtleBuilder.toString();
    }

	private static String buildTurtleClassString( ArrayList<ArrayList<Uri>> listUriListDomain, ArrayList<ArrayList<Uri>> listUriListRange) {
        StringBuilder turtleBuilder = new StringBuilder();
		
		for (ArrayList<Uri> uriListDomain : listUriListDomain) {
			for (Uri uriDomain : uriListDomain) {
				turtleBuilder.append("<"+ uriDomain.getUri() + "> a owl:Class . ");
			}	
		}
		for (ArrayList<Uri> uriListRange : listUriListRange) {
			for (Uri uriRange : uriListRange) {
				turtleBuilder.append("<"+ uriRange.getUri() + "> a owl:Class . ");
			}	
		}	
		// System.out.println(turtleBuilder.toString());	
        // Retourne le resultat en tant que chaîne de caractères
        return turtleBuilder.toString();
    }

	static class UriAndUriAndUriAndNumberComparator implements java.util.Comparator<UriAndUriAndUriAndNumber> {
		@Override
		public int compare(UriAndUriAndUriAndNumber a, UriAndUriAndUriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}

	// public static boolean isListEqualsWithoutOrder(List<String> l1, List<String> l2) {
    //     return l1.size()==l2.size() && l1.containsAll(l2) && l2.containsAll(l1);
	// }

	// private static ArrayList<UriListAndUriList> removeDuplicates(ArrayList<UriListAndUriList> listOfUriListAndUriList) {
    //     // l'égalité se fait sur la deuxième liste ! (voir overide dans la définition de la classe UriListAndUriList)
	// 	Set<UriListAndUriList> uniqueSet = new HashSet<>(listOfUriListAndUriList);
    //     return new ArrayList<>(uniqueSet);
    // }
}

