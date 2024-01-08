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
	public static ArrayList<Uri> makeList(Model model, ArrayList<UriAndNumber> listProperty, String nameOfList) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;

		Integer n = 0;
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Resource b = model.createResource();
		Resource u = model.createResource(dsp + "uri");
		Property pu = model.createProperty(dsp + "asURI");

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

		
		for (Uri resource : listDistinctLanguages) {
			if (n == 0) {
				s = model.createResource(dsp + nameOfList);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u = model.createResource(resource.getUri().toString());
				model.add(b, pu, u);
				model.add(s, p, b);
				n = n + 1;
			} else {
				s = model.createResource(dsp + nameOfList + n);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u = model.createResource(resource.getUri().toString());
				model.add(b, pu, u);
				model.add(s, p, b);
				if (n == 1) {
					s = model.createResource(dsp + nameOfList);
					p = model.createProperty(rdf + "rest");
					o = model.createResource(dsp + nameOfList + n);
					model.add(s, p, o);
					n = n + 1;
				} else {
					s = model.createResource(dsp + nameOfList + (n - 1));
					p = model.createProperty(rdf + "rest");
					o = model.createResource(dsp + nameOfList + n);
					model.add(s, p, o);
					n = n + 1;
				}
			}
		}

		if (n > 0) {

			if (n == 1) {
				s = model.createResource(dsp + nameOfList);
				p = model.createProperty(rdf + "rest");
				o = model.createResource(rdf + "nil");
				model.add(s, p, o);
			} else {
				s = model.createResource(dsp + nameOfList + (n - 1));
				p = model.createProperty(rdf + "rest");
				o = model.createResource(rdf + "nil");
				model.add(s, p, o);
			}
			s = model.createResource(dsp + nameOfList);
			p = model.createProperty(rdf + "type");
			o = model.createResource(rdf + "List");
			model.add(s, p, o);
		}
		
		return listDistinctLanguages;
	}
}