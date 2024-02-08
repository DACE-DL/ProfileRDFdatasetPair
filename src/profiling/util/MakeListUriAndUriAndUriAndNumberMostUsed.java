package profiling.util;

import java.util.ArrayList;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class MakeListUriAndUriAndUriAndNumberMostUsed {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndUriAndNumber> makeList(Model model, ArrayList<UriAndUriAndUriAndNumber> listIn, String nameOfListOut, double dQuantile) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		// String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriAndUriAndNumber> ListResources = new ArrayList<UriAndUriAndUriAndNumber>();
	
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

		//System.out.println("Quantile : " + dQuantile);
		
		//Instant start3 = Instant.now();
		
		// Query query = QueryFactory.create(prefix + 
		// 	"SELECT (?uri1 AS ?class1) (?uri2 AS ?property) (?uri3 AS ?class2) (?val AS ?usage) " +
		// 	" WHERE { dsp:" + nameOfListIn + " rdf:rest*/rdf:first ?element ." +
		// 	" ?element dsp:asURI1 ?uri1 ." +
		// 	" ?element dsp:asURI2 ?uri2 ." +
		// 	" ?element dsp:asURI3 ?uri3 ." +
		// 	" ?element dsp:asValue ?val ." +
		// 	"FILTER (?val > " + dQuantile + ") " +
		// 	" } ORDER BY DESC (?usage)"
		// );			
 		// QueryExecution qe = QueryExecutionFactory.create(query, model);		
		// ResultSet result = qe.execSelect();
		// if (result.hasNext()) {
		// 	while( result.hasNext() ) {
		// 		QuerySolution querySolution = result.next() ;
		// 		ListResources.add(new UriAndUriAndUriAndNumber(querySolution.getResource("class1").toString(),
		// 		querySolution.getResource("property").toString(),
		// 		querySolution.getResource("class2").toString(),
		// 		querySolution.getLiteral("usage").getInt())) ;
		// 	}
		// }
		
		// Instant end3 = Instant.now();
		// System.out.println("Running time: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));

		// Collections.sort(ListResources, new UriAndUriAndUriAndNumberComparator());

		for (UriAndUriAndUriAndNumber resource : listIn) {
			if (resource.getNumber() > dQuantile) {
				ListResources.add(new UriAndUriAndUriAndNumber(resource.getUri1(), resource.getUri2(), resource.getUri3(), resource.getNumber()));		
			}	
		}


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