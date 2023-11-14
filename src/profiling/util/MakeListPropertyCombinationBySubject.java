package profiling.util;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListPropertyCombinationBySubject {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriListAndNumber> makeList(Model model, String nameOfList) {
		
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;
		String dsp = ProfilingConf.dsp;

		ArrayList<UriListAndNumber> ListResources = new ArrayList<UriListAndNumber>();
		
		// Integer n = 0;
		// Resource s = model.createResource(dsp + "sujet");
		// Property p = model.createProperty(dsp + "predicat");
		// Resource o = model.createResource(dsp + "objet");
		// Resource b = model.createResource();
		// Resource u = model.createResource(dsp + "uri");
		// Literal v = null;
		// Property pu = model.createProperty(dsp + "asURI");
		// Property pv = model.createProperty(dsp + "asValue");

		Query query = QueryFactory.create(prefix + 
		" SELECT ?propertyGroup (COUNT(*) AS ?usageCount) " +
		" WHERE {      " +
		"  { " +
		"    SELECT DISTINCT ?subject (GROUP_CONCAT(DISTINCT ?property; separator=\"|\") AS ?propertyList) " +	
		"	 WHERE { " +	
		" 	 	?subject ?property ?object ." +	
		"		FILTER ( !STRSTARTS(str(?property),\"" + dsp + "\") && " + 
        " 			?property NOT IN ( <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>, <http://www.w3.org/1999/02/22-rdf-syntax-ns#first>, <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> ) " +
      	"     	) " +
		" 	 } " +	
		"	 GROUP BY ?subject " +	
		"   } " +	
		"   BIND (CONCAT(?propertyList) AS ?propertyGroup) " +	
		" } " +		
		" GROUP BY ?propertyGroup " +	
		" ORDER BY DESC(COUNT(*)) " +
		" LIMIT 100 " 	  
		);
		
		
		
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				String[] elements = querySolution.getLiteral("propertyGroup").getString().split("\\|");
				ArrayList<Uri> ListProperty = new ArrayList<Uri>();
				for (String element : elements) {
					Uri uri = new Uri(element);
					ListProperty.add(uri);
				}
				Collections.sort(ListProperty, new UriComparator());
				UriListAndNumber uriListAndNumber = new UriListAndNumber();
				uriListAndNumber.setUriList(ListProperty);
				uriListAndNumber.setNumber(querySolution.getLiteral("usageCount").getInt());
				ListResources.add(uriListAndNumber) ;
			}
		}
		
		
		// for (UriListAndNumber resource : ListResources) {
		// 	if (n == 0) {
		// 		s = model.createResource(dsp + nameOfList);
		// 		p = model.createProperty(rdf + "first");
				
		// 		b = model.createResource();
		// 		u = model.createResource(resource.getUri().toString());
		// 		v = ResourceFactory.createTypedLiteral(resource.getNumber());
		// 		model.add(b, pu, u);
		// 		model.add(b, pv, v);

		// 		model.add(s, p, b);
		// 		n = n + 1;
		// 	} else {
		// 		s = model.createResource(dsp + nameOfList + n);
		// 		p = model.createProperty(rdf + "first");
				
		// 		b = model.createResource();
		// 		u = model.createResource(resource.getUri().toString());
		// 		v = ResourceFactory.createTypedLiteral(resource.getNumber());
		// 		model.add(b, pu, u);
		// 		model.add(b, pv, v);

		// 		model.add(s, p, b);
		// 		if (n == 1) {
		// 			s = model.createResource(dsp + nameOfList);
		// 			p = model.createProperty(rdf + "rest");
		// 			o = model.createResource(dsp + nameOfList + n);
		// 			model.add(s, p, o);
		// 			n = n + 1;
		// 		} else {
		// 			s = model.createResource(dsp + nameOfList + (n - 1));
		// 			p = model.createProperty(rdf + "rest");
		// 			o = model.createResource(dsp + nameOfList + n);
		// 			model.add(s, p, o);
		// 			n = n + 1;
		// 		}
		// 	}
		// }

		// if (n > 0) {

		// 	if (n == 1) {
		// 		s = model.createResource(dsp + nameOfList);
		// 		p = model.createProperty(rdf + "rest");
		// 		o = model.createResource(rdf + "nil");
		// 		model.add(s, p, o);
		// 	} else {
		// 		s = model.createResource(dsp + nameOfList + (n - 1));
		// 		p = model.createProperty(rdf + "rest");
		// 		o = model.createResource(rdf + "nil");
		// 		model.add(s, p, o);
		// 	}
		// 	s = model.createResource(dsp + nameOfList);
		// 	p = model.createProperty(rdf + "type");
		// 	o = model.createResource(rdf + "List");
		// 	model.add(s, p, o);
		// }
		return ListResources;
	}
	
	static class UriComparator implements java.util.Comparator<Uri> {
		@Override
		public int compare(Uri a, Uri b) {
			return GiveLocalname.giveName(a.getUri()).compareTo(GiveLocalname.giveName(b.getUri()));
		}
	}
	
}