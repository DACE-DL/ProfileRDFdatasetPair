package profiling.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

public class MakeListClassLanguages {
	
	// Création d'une liste des vocabulaires des classes
	public static ArrayList<Uri> makeList(Model model, ArrayList<UriAndNumber> listClassUsageCount) {

		ArrayList<Uri> listDistinctLanguages = new ArrayList<>();
		List<String> listLanguagesString = new ArrayList<>();

		listClassUsageCount.forEach((theClass) -> {
			Property p1 = null;
			Resource o1 = null;
			Selector selector1 = new SimpleSelector(model.createResource(theClass.getUri()), p1, o1) ;
			StmtIterator stmtIte1 = model.listStatements(selector1);
			stmtIte1.forEach((stm) -> {
				if (stm.getObject().isLiteral()) {	
					if (stm.getObject().asLiteral().getLanguage().toString() != "") {
						if (!listLanguagesString.contains(stm.getObject().asLiteral().getLanguage().toString())) { 
							//System.out.println("Property class: " + stm.getPredicate().toString());
							//System.out.println("Objet class: " + stm.getObject().toString());
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