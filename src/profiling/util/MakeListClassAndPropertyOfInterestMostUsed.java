package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class MakeListClassAndPropertyOfInterestMostUsed {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndUriAndNumber> makeList(Model model, ArrayList<UriAndUriAndUriAndNumber> listClassAndPropertyOfInterestCount, String nameOfListOut) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;

		ArrayList<UriAndUriAndUriAndNumber> ListResources = new ArrayList<UriAndUriAndUriAndNumber>();
	
		Integer i = 0;
		Integer n = 0;
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Resource b = model.createResource();
		Resource u1 = model.createResource(dsp + "uri1");
		Resource u2 = model.createResource(dsp + "uri2");
		Resource u3 = model.createResource(dsp + "uri3");
		Literal v = null;
		Property pu1 = model.createProperty(dsp + "asURI1");
		Property pu2 = model.createProperty(dsp + "asURI2");
		Property pu3 = model.createProperty(dsp + "asURI3");
		Property pv = model.createProperty(dsp + "asValue");

		Collections.sort(listClassAndPropertyOfInterestCount, new UriAndUriAndUriAndNumberComparator());

		for (UriAndUriAndUriAndNumber classAndPropertyOfInterestCount : listClassAndPropertyOfInterestCount) {
			ListResources.add(new UriAndUriAndUriAndNumber(classAndPropertyOfInterestCount.getUri1(), classAndPropertyOfInterestCount.getUri2(), classAndPropertyOfInterestCount.getUri3(), classAndPropertyOfInterestCount.getNumber()));		
			i++;
			if ( i > 99 ) break;
		};	

		

		for (UriAndUriAndUriAndNumber resource : ListResources) {
			if (n == 0) {
				s = model.createResource(dsp + nameOfListOut);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri1().toString());
				u2 = model.createResource(resource.getUri2().toString());
				u3 = model.createResource(resource.getUri3().toString());
				v = ResourceFactory.createTypedLiteral(resource.getNumber());
				model.add(b, pu1, u1);
				model.add(b, pu2, u2);
				model.add(b, pu3, u3);
				model.add(b, pv, v);

				model.add(s, p, b);
				n = n + 1;
			} else {
				s = model.createResource(dsp + nameOfListOut + n);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri1().toString());
				u2 = model.createResource(resource.getUri2().toString());
				u3 = model.createResource(resource.getUri3().toString());
				v = ResourceFactory.createTypedLiteral(resource.getNumber());
				model.add(b, pu1, u1);
				model.add(b, pu2, u2);
				model.add(b, pu3, u3);
				model.add(b, pv, v);

				model.add(s, p, b);
				if (n == 1) {
					s = model.createResource(dsp + nameOfListOut);
					p = model.createProperty(rdf + "rest");
					o = model.createResource(dsp + nameOfListOut + n);
					model.add(s, p, o);
					n = n + 1;
				} else {
					s = model.createResource(dsp + nameOfListOut + (n - 1));
					p = model.createProperty(rdf + "rest");
					o = model.createResource(dsp + nameOfListOut + n);
					model.add(s, p, o);
					n = n + 1;
				}
			}
		}

		if (n > 0) {

			if (n == 1) {
				s = model.createResource(dsp + nameOfListOut);
				p = model.createProperty(rdf + "rest");
				o = model.createResource(rdf + "nil");
				model.add(s, p, o);
			} else {
				s = model.createResource(dsp + nameOfListOut + (n - 1));
				p = model.createProperty(rdf + "rest");
				o = model.createResource(rdf + "nil");
				model.add(s, p, o);
			}
			s = model.createResource(dsp + nameOfListOut);
			p = model.createProperty(rdf + "type");
			o = model.createResource(rdf + "List");
			model.add(s, p, o);
		}
		return ListResources;
	}
	
	static class UriAndUriAndUriAndNumberComparator implements java.util.Comparator<UriAndUriAndUriAndNumber> {
		@Override
		public int compare(UriAndUriAndUriAndNumber a, UriAndUriAndUriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}