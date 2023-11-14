package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class MakeListPropertyUsagePerObject {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndNumberAndNumber> makeList(Model model, String nameOfListIn, String nameOfListOut) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndNumberAndNumber> ListResources = new ArrayList<UriAndNumberAndNumber>();
	
		Integer n = 0;
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Resource b = model.createResource();
		Resource u = model.createResource(dsp + "uri");
		Literal v = null;
		Literal v2 = null;
		Property pu = model.createProperty(dsp + "asURI");
		Property pv = model.createProperty(dsp + "asValue");
		Property pv2 = model.createProperty(dsp + "asValue2");

		// nameOfListIn = nameOfListPropertyUsageCount	
		Query query = QueryFactory.create(prefix + 
			"SELECT ?property (COUNT(?i2) AS ?usage) (COUNT(DISTINCT ?i2) AS ?usage2) WHERE " +
			" { dsp:" + nameOfListIn + " rdf:rest*/rdf:first ?element ." +
			" ?element dsp:asURI ?property ." +
			" ?i1 ?property ?i2 ." +
			" } GROUP BY ?property "
		);
		
 		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new UriAndNumberAndNumber(querySolution.getResource("property").toString(), querySolution.getLiteral("usage").getInt(), querySolution.getLiteral("usage2").getInt())) ;
			}
		}
		
		
		Collections.sort(ListResources, new UriAndNumberAndNumberComparator());

		for (UriAndNumberAndNumber resource : ListResources) {
			if (n == 0) {
				s = model.createResource(dsp + nameOfListOut);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u = model.createResource(resource.getUri().toString());
				v = ResourceFactory.createTypedLiteral(resource.getNumber());
				v2 = ResourceFactory.createTypedLiteral(resource.getNumber2());
				model.add(b, pu, u);
				model.add(b, pv, v);
				model.add(b, pv2, v2);

				model.add(s, p, b);
				n = n + 1;
			} else {
				s = model.createResource(dsp + nameOfListOut + n);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u = model.createResource(resource.getUri().toString());
				v = ResourceFactory.createTypedLiteral(resource.getNumber());
				v2 = ResourceFactory.createTypedLiteral(resource.getNumber2());
				model.add(b, pu, u);
				model.add(b, pv, v);
				model.add(b, pv2, v2);

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

	static class UriAndNumberAndNumberComparator implements java.util.Comparator<UriAndNumberAndNumber> {
		@Override
		public int compare(UriAndNumberAndNumber a, UriAndNumberAndNumber b) {
			if (!(b.getNumber() - a.getNumber() == 0)) {
				return b.getNumber() - a.getNumber();
			} else {
				return (b.getNumber2() - a.getNumber2());
			}
		}
	}
}