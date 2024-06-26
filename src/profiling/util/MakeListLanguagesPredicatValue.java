package profiling.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

public class MakeListLanguagesPredicatValue {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<Uri> makeList(Model model, ArrayList<UriAndNumber> listProperty) {
		
		ArrayList<Uri> listDistinctLanguages = new ArrayList<>();
		List<String> listLanguagesString = new ArrayList<>();

		listProperty.forEach((property) -> {
			Resource s1 = null;
			Resource o1 = null;
			Selector selector1 = new SimpleSelector(s1, model.createProperty(property.getUri()), o1) ;
			StmtIterator stmtIte1 = model.listStatements(selector1);
			stmtIte1.forEach((stmObj) -> {
				if (stmObj.getObject().isLiteral()) {
						if (stmObj.getObject().asLiteral().getLanguage().toString() != "") {
							if (!listLanguagesString.contains(stmObj.getObject().asLiteral().getLanguage().toString())) { 
								listLanguagesString.add(stmObj.getObject().asLiteral().getLanguage().toString());
								listDistinctLanguages.add(new Uri(stmObj.getObject().asLiteral().getLanguage().toString()));
							}	
						}	
				}		
			});
			stmtIte1.close();
        });
		return listDistinctLanguages;
	}
}