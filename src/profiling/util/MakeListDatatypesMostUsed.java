package profiling.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

public class MakeListDatatypesMostUsed {
	
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

		for (Uri resource : listDistinctDatatypes) {
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
		
		return listDistinctDatatypes;
	}
}