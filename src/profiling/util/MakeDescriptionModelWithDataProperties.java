package profiling.util;

import java.util.ArrayList;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public class MakeDescriptionModelWithDataProperties {
	
	// Création du modèle de description
	public static OntModel  makeModel(OntModel descriptionModelTemp, ArrayList<UriAndUriAndUriListList> listMostImportantPropertiesOfClasses) {
		// String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String rdfs = ProfilingConf.rdfs;
		OntModel descriptionModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		descriptionModel.add(descriptionModelTemp);
		// System.out.println("descriptionModel :" + descriptionModel.size());
		
		// RDFList listAnonymous = descriptionModel.createList(new RDFNode[] {});
		// UnionClass unionClass = descriptionModel.createUnionClass(dsp + "anonymous", listAnonymous);
		// Pour supprimer la description de la classe d'union crée par l'initialisation de "unionClass"
		// descriptionModel.removeAll(descriptionModel.getUnionClass(dsp + "anonymous"), null, (RDFNode) null);
		// Integer n = 1;
		
		for (UriAndUriAndUriListList uriAndUriAndUriListList : listMostImportantPropertiesOfClasses) {
			for (UriAndUriList uriAndUriList : uriAndUriAndUriListList.getUriAndUriList()) {
				// On verifie que la propriété ne soit pas déja utilisé en objectProperty
				if (descriptionModel.getObjectProperty(uriAndUriList.getUri()) == null) {
					descriptionModel.createDatatypeProperty(uriAndUriList.getUri())
					.addDomain(descriptionModel.getResource(uriAndUriAndUriListList.getUri()));
					if (uriAndUriList.getUriList().size() == 1 ) {
						descriptionModel.getDatatypeProperty(uriAndUriList.getUri())
						.addRange(descriptionModel.createResource(uriAndUriList.getUriList().get(0).getUri()));
					}
					if (uriAndUriList.getUriList().size() > 1 ) {
						for (Uri datatype : uriAndUriList.getUriList()) {
							descriptionModel.add(descriptionModel.createResource(datatype.getUri()),
						    descriptionModel.createProperty(rdf + "type"),descriptionModel.getResource(rdfs + "Datatype"));
							descriptionModel.getDatatypeProperty(uriAndUriList.getUri())
						    .addRange(descriptionModel.getResource(datatype.getUri()));
						}
						// // Si plusieurs datatype dans la liste, créer une union de datatypes
						// ArrayList<String> listOfdatatypes = new ArrayList<String>();
						// RDFList RDFlistDatatypes = descriptionModel.createList(new RDFNode[] {});
						// for (Uri datatype : uriAndUriList.getUriList()) {
						// 	listOfdatatypes.add(datatype.getUri());
						// 	descriptionModel.add(descriptionModel.createResource(datatype.getUri()),
						// 	descriptionModel.createProperty(rdf + "type"),descriptionModel.getResource(rdfs + "Datatype"));
						// 	RDFlistDatatypes = RDFlistDatatypes.with(descriptionModel.getResource(datatype.getUri()));
						// }
						// // Création d'une propriété avec le range correspondant à l'union des datatypes
						// descriptionModel.getDatatypeProperty(uriAndUriList.getUri())
						// .addRange(descriptionModel.createDataRange(RDFlistDatatypes));
						// n++;		
					}
				} else {
					// System.out.println(descriptionModel.getObjectProperty(uriAndUriList.getUri()).getURI());
				}
			}
		}
		return descriptionModel;
	}
}

