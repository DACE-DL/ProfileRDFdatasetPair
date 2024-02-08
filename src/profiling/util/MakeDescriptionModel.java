package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.UnionClass;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;

public class MakeDescriptionModel {
	
	// Création du modèle de description
	public static UriForDescriptionModel  makeModel(ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClasses) {
		
		OntModel descriptionModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String skos = ProfilingConf.skos;
		descriptionModel.setNsPrefix("dsp", dsp);
		descriptionModel.setNsPrefix("rdf", rdf);
		descriptionModel.setNsPrefix("skos", skos);

		UriForDescriptionModel results = new UriForDescriptionModel();
		ArrayList<UriAndUriListAndUriList> listOfRelationships = new ArrayList<UriAndUriListAndUriList>();
		ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClassesTemp = new ArrayList<UriAndUriAndUriAndNumber>();
		ArrayList<String> listOfProperty = new ArrayList<String>();
		RDFList listAnonymous = descriptionModel.createList(new RDFNode[] {});
		UnionClass unionClass = descriptionModel.createUnionClass(dsp + "anonymous", listAnonymous);
		// Pour supprimer la description de la classe d'union crée par l'initialisation de "unionClass"
		descriptionModel.removeAll(descriptionModel.getUnionClass(dsp + "anonymous"), null, (RDFNode) null);
		Integer n = 1;
		
		// Instant start0 = Instant.now();	
	    
		listOfRelationshipsBetweenNewClassesTemp.addAll(listOfRelationshipsBetweenNewClasses);

		for (UriAndUriAndUriAndNumber resource : listOfRelationshipsBetweenNewClasses) {
			String uriProperty = resource.getUri2();
			// Si propriété pas encore traitée
			if (!listOfProperty.contains(uriProperty)) {
				listOfProperty.add(uriProperty);
				ArrayList<String> listUriRange = new ArrayList<String>();
				ArrayList<String> listUriDomain = new ArrayList<String>();
				for (UriAndUriAndUriAndNumber resourceTemp : listOfRelationshipsBetweenNewClassesTemp) {
					if (resourceTemp.getUri2() == uriProperty) {
						if (!listUriRange.contains(resourceTemp.getUri1())) {
							listUriRange.add(resourceTemp.getUri1());
						}
						if (!listUriDomain.contains(resourceTemp.getUri3())) {
							listUriDomain.add(resourceTemp.getUri3());
						}
					}
				}
				UriAndUriListAndUriList uriAndUriListAndUriList = new UriAndUriListAndUriList(uriProperty, listUriRange, listUriDomain);
				listOfRelationships.add(uriAndUriListAndUriList);
			}
		}
		
		ArrayList<UriAndUriListAndUriList> listOfRelationshipsTemp = new ArrayList<UriAndUriListAndUriList>();
		listOfRelationshipsTemp.addAll(listOfRelationships);
		ArrayList<UriListAndUriList> listOfRelationshipsDomain = new ArrayList<UriListAndUriList>();
		ArrayList<UriListAndUriList> listOfRelationshipsRange = new ArrayList<UriListAndUriList>();
		
		for (UriAndUriListAndUriList resource : listOfRelationships) {
			ArrayList<String> listForDomain = new ArrayList<String>();
			ArrayList<String> listOfPropertyForDomain = new ArrayList<String>();
			ArrayList<String> listForRange = new ArrayList<String>();
			ArrayList<String> listOfPropertyForRange = new ArrayList<String>();
			listOfPropertyForDomain.add(resource.getUri());
			listForDomain.addAll(resource.getUriList1());
			listOfPropertyForRange.add(resource.getUri());
			listForRange.addAll(resource.getUriList2());
			for (UriAndUriListAndUriList resourceTemp : listOfRelationshipsTemp) {
				if (isListEqualsWithoutOrder(listForDomain, resourceTemp.getUriList1())) {
					if (!listOfPropertyForDomain.contains(resourceTemp.getUri())) {
						listOfPropertyForDomain.add(resourceTemp.getUri());
					}
				}
				if (isListEqualsWithoutOrder(listForRange, resourceTemp.getUriList2())) {
					if (!listOfPropertyForRange.contains(resourceTemp.getUri())) {
						listOfPropertyForRange.add(resourceTemp.getUri());
					}	
				}
			}
			UriListAndUriList uriListAndUriListDomain = new UriListAndUriList(listOfPropertyForDomain, listForDomain);
			listOfRelationshipsDomain.add(uriListAndUriListDomain);
			UriListAndUriList uriListAndUriListRange = new UriListAndUriList(listOfPropertyForRange, listForRange);
			listOfRelationshipsRange.add(uriListAndUriListRange);
		}

		// Suppression des doublons pour les domaines
		ArrayList<UriListAndUriList> listOfRelationshipsDomainWithoutDuplicates = removeDuplicates(listOfRelationshipsDomain);
		
		for (UriListAndUriList resource : listOfRelationshipsDomainWithoutDuplicates) {	
			if (resource.getUriList2().size() == 1) {
			} else { 
				// Si plusieurs classes dans la liste, créer une union de classes
				RDFList list = descriptionModel.createList(resource.getUriList2().stream().map(descriptionModel::createClass).toArray(RDFNode[]::new));
				// Création d'une propriété avec le domaine correspondant à l'union de classes
				unionClass = descriptionModel.createUnionClass(dsp + "DomainUnionClass_" + n, list);
				//unionClass = descriptionModel.createUnionClass(null, list);
				n++;
			}
			for (String property : resource.getUriList1()) {
				if (resource.getUriList2().size() == 1) {
					descriptionModel.createObjectProperty (property).setDomain(descriptionModel.createClass(resource.getUriList2().get(0)));
				} else { 
					// Création d'une propriété avec le domaine correspondant à l'union de classes
					descriptionModel.createObjectProperty(property)
						.setDomain(unionClass);
				}
			}
		}

		// Suppression des doublons pour les ranges
		ArrayList<UriListAndUriList> listOfRelationshipsRangeWithoutDuplicates = removeDuplicates(listOfRelationshipsRange);
		
		n = 1;
		for (UriListAndUriList resource : listOfRelationshipsRangeWithoutDuplicates) {	
			if (resource.getUriList2().size() == 1) {
			} else { 
				// Si plusieurs classes dans la liste, créer une union de classes
				RDFList list = descriptionModel.createList(resource.getUriList2().stream().map(descriptionModel::createClass).toArray(RDFNode[]::new));
				// Création d'une propriété avec le domaine correspondant à l'union de classes
				unionClass = descriptionModel.createUnionClass(dsp + "RangeUnionClass_" + n, list);
				n++;
			}
			for (String property : resource.getUriList1()) {
				if (resource.getUriList2().size() == 1) {
					descriptionModel.createObjectProperty (property).setRange(descriptionModel.createClass(resource.getUriList2().get(0)));
				} else { 
					// Création d'une propriété avec le domaine correspondant à l'union de classes
					descriptionModel.createObjectProperty(property)
						.setRange(unionClass);
				}
			}	
		}
		
		results.setDescriptionModel(descriptionModel);
		results.setListOfRelationshipsDomain(listOfRelationshipsDomainWithoutDuplicates);
		results.setListOfRelationshipsRange(listOfRelationshipsRangeWithoutDuplicates);
		
		// Instant end0 = Instant.now();
		// System.out.println("Running time for making description model: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return results;
	}
	
	static class UriAndUriAndUriAndNumberComparator implements java.util.Comparator<UriAndUriAndUriAndNumber> {
		@Override
		public int compare(UriAndUriAndUriAndNumber a, UriAndUriAndUriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}

	public static boolean isListEqualsWithoutOrder(List<String> l1, List<String> l2) {
        return l1.size()==l2.size() && l1.containsAll(l2) && l2.containsAll(l1);
	}

	private static ArrayList<UriListAndUriList> removeDuplicates(ArrayList<UriListAndUriList> listOfUriListAndUriList) {
        // l'égalité se fait sur la deuxième liste ! (voir overide dans la définition de la classe UriListAndUriList)
		Set<UriListAndUriList> uniqueSet = new HashSet<>(listOfUriListAndUriList);
        return new ArrayList<>(uniqueSet);
    }
}

