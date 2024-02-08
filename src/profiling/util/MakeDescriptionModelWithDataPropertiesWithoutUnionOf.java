package profiling.util;

import java.util.ArrayList;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public class MakeDescriptionModelWithDataPropertiesWithoutUnionOf {
	
	// Création du modèle de description
	public static OntModel  makeModel(OntModel descriptionModelTemp, ArrayList<UriAndUriAndUriListList> listMostImportantPropertiesOfClasses) {
		
		OntModel descriptionModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		descriptionModel.add(descriptionModelTemp);
		for (UriAndUriAndUriListList uriAndUriAndUriListList : listMostImportantPropertiesOfClasses) {
			for (UriAndUriList uriAndUriList : uriAndUriAndUriListList.getUriAndUriList()) {
				// On verifie que la propriété ne soit pas déja utilisé en objectProperty
				if (descriptionModel.getObjectProperty(uriAndUriList.getUri()) == null) {
					descriptionModel.createDatatypeProperty(uriAndUriList.getUri())
					.addDomain(descriptionModel.getResource(uriAndUriAndUriListList.getUri()));
					if (uriAndUriList.getUriList().size() >= 1 ) {
						for (Uri dataProperty : uriAndUriList.getUriList()) {
							descriptionModel.getDatatypeProperty(uriAndUriList.getUri())
							.addRange(descriptionModel.createResource(dataProperty.getUri()));
						}	
					}
				} else {
					// System.out.println(descriptionModel.getObjectProperty(uriAndUriList.getUri()).getURI());
				}
			}
		}
		return descriptionModel;
	}
}

