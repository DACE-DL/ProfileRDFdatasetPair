package inferencesAndQueries;

import java.io.IOException;
import java.util.Iterator;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.reasoner.ValidityReport;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class VerifyInferedModel {
	
	// On passe par les fichiers du serveur en fournissant une liste de noms de fichiers contenant des requettes
	@SuppressWarnings("rawtypes")
	public static void verifyInferedModel(InfModel infModel) throws JsonParseException, JsonMappingException, IOException {

		System.out.println("Verify model:");

		ValidityReport validity = infModel.validate();
		if (validity.isValid()) {
    	System.out.println("OK");
		} else {
    	System.out.println("Conflicts");
    	for (Iterator i = validity.getReports(); i.hasNext(); ) {
        	System.out.println(" - " + i.next());
    	}
		}
	}
}