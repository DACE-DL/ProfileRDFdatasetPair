package profiling.util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class MakeListObjectClassOfInterest {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndNumber> makeList(Model model, String nameOfListIn, String nameOfListOut) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndNumber> ListResources = new ArrayList<UriAndNumber>();
	
		Integer n = 0;
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Resource b = model.createResource();
		Resource u1 = model.createResource(dsp + "uri");
		
		Property pu1 = model.createProperty(dsp + "asURI");
		
		Query query = QueryFactory.create(prefix + 
		" SELECT  ?class2 (COUNT(*) AS ?usage)" +
		" WHERE { " +
		" dsp:" + nameOfListIn + " rdf:rest*/rdf:first ?element ." +
		" ?element dsp:asURI3 ?class2 ." +
		" } GROUP BY ?class2 ORDER BY DESC(?usage) "
		);
		// System.out.println("Query : " + query );
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new UriAndNumber(querySolution.getResource("class2").toString(), querySolution.getLiteral("usage").getInt())) ;
			}
		}

		for (UriAndNumber resource : ListResources) {
			if (n == 0) {
				s = model.createResource(dsp + nameOfListOut);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri().toString());
				
				model.add(b, pu1, u1);
				
				model.add(s, p, b);
				n = n + 1;
			} else {
				s = model.createResource(dsp + nameOfListOut + n);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri().toString());
				
				model.add(b, pu1, u1);
		
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

	static class UriAndNumberComparator implements java.util.Comparator<UriAndNumber> {
		@Override
		public int compare(UriAndNumber a, UriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}