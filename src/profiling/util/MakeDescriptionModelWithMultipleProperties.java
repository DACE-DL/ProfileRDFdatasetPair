package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public class MakeDescriptionModelWithMultipleProperties {
	
	// Création du modèle de description
	public static OntModel  makeModel(ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClasses) {
		
		OntModel descriptionModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String skos = ProfilingConf.skos;
		descriptionModel.setNsPrefix("dsp", dsp);
		descriptionModel.setNsPrefix("rdf", rdf);
		descriptionModel.setNsPrefix("skos", skos);

		ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClassesTemp = new ArrayList<UriAndUriAndUriAndNumber>();
		ArrayList<UriAndUriAndUriAndNumber> listOfRelationshipsBetweenNewClassesTemp2 = new ArrayList<UriAndUriAndUriAndNumber>();
		
		Integer n = 0;

		Instant start0 = Instant.now();	
	    
		listOfRelationshipsBetweenNewClassesTemp.addAll(listOfRelationshipsBetweenNewClasses);

		for (UriAndUriAndUriAndNumber resource : listOfRelationshipsBetweenNewClasses) {
			String uriProperty = resource.getUri2();
			n = 0;
			for (UriAndUriAndUriAndNumber resourceTemp : listOfRelationshipsBetweenNewClassesTemp) {
				if (resourceTemp.getUri2() == uriProperty) {
					n++;
					if (n == 1) {
						UriAndUriAndUriAndNumber uriAndUriAndUriAndNumber = new UriAndUriAndUriAndNumber();
						uriAndUriAndUriAndNumber.setUri2(uriProperty);
						uriAndUriAndUriAndNumber.setUri1(resourceTemp.getUri1());
						uriAndUriAndUriAndNumber.setUri3(resourceTemp.getUri3());
						uriAndUriAndUriAndNumber.setNumber(resourceTemp.getNumber());
						listOfRelationshipsBetweenNewClassesTemp2.add(uriAndUriAndUriAndNumber);
					} else {
						descriptionModel.createObjectProperty(uriProperty).addSubProperty(descriptionModel.createObjectProperty(uriProperty + "__" + n));
						UriAndUriAndUriAndNumber uriAndUriAndUriAndNumber = new UriAndUriAndUriAndNumber();
						uriAndUriAndUriAndNumber.setUri2(uriProperty + "__" + n);
						uriAndUriAndUriAndNumber.setUri1(resourceTemp.getUri1());
						uriAndUriAndUriAndNumber.setUri3(resourceTemp.getUri3());
						uriAndUriAndUriAndNumber.setNumber(resourceTemp.getNumber());
						listOfRelationshipsBetweenNewClassesTemp2.add(uriAndUriAndUriAndNumber);
					}
				}
			}
		}
		
		for (UriAndUriAndUriAndNumber resource : listOfRelationshipsBetweenNewClassesTemp2) {	
			descriptionModel.createObjectProperty(resource.getUri2())
					.setDomain(descriptionModel.createClass(resource.getUri1()));
			descriptionModel.createObjectProperty(resource.getUri2())
					.setRange(descriptionModel.createClass(resource.getUri3()));
		}

		Instant end0 = Instant.now();
		System.out.println("Running time for making description model with multiple properties : " + Duration.between(start0, end0).getSeconds() + " secondes");
		return descriptionModel;
	}
	
	static class UriAndUriAndUriAndNumberComparator implements java.util.Comparator<UriAndUriAndUriAndNumber> {
		@Override
		public int compare(UriAndUriAndUriAndNumber a, UriAndUriAndUriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}

