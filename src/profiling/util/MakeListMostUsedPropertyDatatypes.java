package profiling.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

public class MakeListMostUsedPropertyDatatypes {
	
	// Création d'une liste of datatypes des propriétés les plus utilisées.
	public static ArrayList<Uri> makeList(Model model, ArrayList<UriAndNumber> listProperty) {

		List<RDFNode> listObject = new ArrayList<>();
	
		listProperty.forEach((property) -> {
			Resource s2 = null;
			Resource o2 = null;
			Selector selector1 = new SimpleSelector(s2, model.createProperty(property.getUri()), o2) ;
			StmtIterator stmtIte1 = model.listStatements(selector1);
			stmtIte1.forEach((stmObj) -> {
				listObject.add(stmObj.getObject());
			});
        });

		// Duplicate checking
		ArrayList<Uri> listDistinctDatatypes = new ArrayList<>();
		List<String> listDatatypesString = new ArrayList<>();
		listObject.forEach((object) -> {
			if (object.isLiteral()) {
				if (object.asLiteral().getDatatypeURI().toString() != null) {		
					if (!listDatatypesString.contains(object.asLiteral().getDatatypeURI().toString())) { 
						listDatatypesString.add(object.asLiteral().getDatatypeURI().toString());
						listDistinctDatatypes.add(new Uri(object.asLiteral().getDatatypeURI().toString()));
					}
				}			
			} 
		});
		
		return listDistinctDatatypes;
	}
}