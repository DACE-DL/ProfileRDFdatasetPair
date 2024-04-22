package profiling.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

public class MakeListLanguagesPredicat {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<Uri> makeList(Model model, ArrayList<UriAndNumber> listProperty) {
		
		ArrayList<Uri> listDistinctLanguages = new ArrayList<>();
		List<String> listLanguagesString = new ArrayList<>();

		listProperty.forEach((property) -> {
			Resource o2 = null;
			Property p2 = null;
			Selector selector1 = new SimpleSelector(model.createResource(property.getUri()), p2, o2) ;
			StmtIterator stmtIte1 = model.listStatements(selector1);
			stmtIte1.forEach((stm) -> {
				if (stm.getObject().isLiteral()) {
					if (stm.getObject().asLiteral().getLanguage().toString() != "") {
						if (!listLanguagesString.contains(stm.getObject().asLiteral().getLanguage().toString())) { 
							//System.out.println("Property predicat: " + stm.getPredicate().toString());
							//System.out.println("Objet predicat: " + stm.getObject().toString());
							listLanguagesString.add(stm.getObject().asLiteral().getLanguage().toString());
							listDistinctLanguages.add(new Uri(stm.getObject().asLiteral().getLanguage().toString()));
						}	
					}			
				}	
			});
        });
		return listDistinctLanguages;
	}
}