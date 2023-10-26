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

public class MakeListClassDefined {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<Uri> makeList(Model model, String nameOfList) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String prefix = ProfilingConf.queryPrefix;

		Integer n = 0;
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Resource b = model.createResource();
		Resource u = model.createResource(dsp + "uri");
		Property pu = model.createProperty(dsp + "asURI");
		
		ArrayList<Uri> ListResources = new ArrayList<Uri>();
		
		Query query = QueryFactory.create(prefix + 
				"SELECT DISTINCT (?s AS ?class) " +
				" WHERE { " +
				" ?s rdf:type ?o ." +
				" FILTER isIRI(?o) ." +
				" FILTER (?o=rdfs:Class||?o=owl:Class) ." +
				" FILTER (?s != <http://www.w3.org/2002/07/owl#Class>) " +
				" FILTER (?s != <http://www.w3.org/2000/01/rdf-schema#Class>) " +
				" FILTER (?s != <http://www.w3.org/2004/02/skos/core#Concept>) " +
				" FILTER (?s != <http://www.w3.org/2002/07/owl#Thing>) " +
				" FILTER (?s != <http://www.w3.org/2002/07/owl#Nothing>) " +
				" FILTER (?s != <http://www.w3.org/1999/02/22-rdf-syntax-ns#List>) " +  
				" } " );			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new Uri(querySolution.getResource("class").toString())) ;
			}
		}

		for (Uri resource : ListResources) {
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
		
		return ListResources;
	}
}