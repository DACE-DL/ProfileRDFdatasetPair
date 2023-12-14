package Asupprimer;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

import profiling.util.ProfilingConf;
import profiling.util.UriAndUri;

public class MakeListLinksSVG {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUri> makeList(Model model, String nameOfList) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;

		ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
		List<String> listResourcesString = new ArrayList<>();
		
		Resource s1 = null;
		Property p1 = null;
		Resource o1 = null;
		Integer n = 0;
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Resource b = model.createResource();
		Resource u1 = model.createResource(dsp + "property");
		Resource u2 = model.createResource(dsp + "subproperty");
		Property pu1 = model.createProperty(dsp + "asNamespaceSubject");
		Property pu2 = model.createProperty(dsp + "asNamespaceObject");
		List<Resource> listProperty = new ArrayList<>();
	
		p1 = model.createProperty(rdf ,"type");
		o1 = model.createResource(rdf + "Property");

		SimpleSelector selector = new SimpleSelector(s1, p1, o1) ;
		StmtIterator stmtIte= model.listStatements(selector);
		
		stmtIte.forEach((statement) -> {
            listProperty.add(statement.getSubject());
        });

		listProperty.forEach((property) -> {
			Resource s2 = null;
			Resource o2 = null;
			SimpleSelector selector1 = new SimpleSelector(s2, model.createProperty(property.getURI()), o2);
			StmtIterator stmtIte1 = model.listStatements(selector1);
			stmtIte1.forEach((stm) -> {
				if (stm.getSubject().isResource() && stm.getObject().isResource()) {
					if (!(stm.getSubject().isAnon() || stm.getObject().isAnon())) {		
						if (stm.getSubject().getNameSpace() != stm.getObject().asResource().getNameSpace()) {
							String SubNsObNs = stm.getSubject().getNameSpace() + stm.getObject().asResource().getNameSpace();
							if (!listResourcesString.contains(SubNsObNs)) { 
								listResourcesString.add(SubNsObNs);
								ListResources.add(new UriAndUri(stm.getSubject().getNameSpace(), stm.getObject().asResource().getNameSpace())) ;
							}	
						}
					}	
				}			
			});
        });

		for (UriAndUri resource : ListResources) {
			if (n == 0) {
				s = model.createResource(dsp + nameOfList);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri1().toString());
				u2 = model.createResource(resource.getUri2().toString());
				model.add(b, pu1, u1);
				model.add(b, pu2, u2);

				model.add(s, p, b);
				n = n + 1;
			} else {
				s = model.createResource(dsp + nameOfList + n);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri1().toString());
				u2 = model.createResource(resource.getUri2().toString());
				model.add(b, pu1, u1);
				model.add(b, pu2, u2);

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
		return ListResources;
	}
}