package profiling.util;

import java.util.ArrayList;
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

public class MakeListPerProperty {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndStringAndBigNumber> makeList(Model model, String nameOfList) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndStringAndBigNumber> ListResources = new ArrayList<UriAndStringAndBigNumber>();
	
		Integer n = 0;
		
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Resource b = model.createResource();
		Resource u = model.createResource(dsp + "uri");
		Literal v = null;
		Literal t = null;
		Property pu = model.createProperty(dsp + "asURI");
		Property pd = model.createProperty(dsp + "asDomain");
		Property pv = model.createProperty(dsp + "asValue");

			Query query = QueryFactory.create(prefix +
					"SELECT ?property ?datatype (AVG(?o) AS ?usage) " +
					" WHERE { " +
					"dsp:listPropertyUsageCount rdf:rest*/rdf:first ?element ." +
					"?element dsp:asURI ?property ." +
					" ?s ?property ?o ." +
					" FILTER ( datatype(?o) = xsd:integer || datatype(?o) = xsd:float || datatype(?o) = xsd:double || datatype(?o) = xsd:decimal || datatype(?o) = xsd:dateTime ) " +
					" } GROUP BY ?property (datatype(?o) AS ?datatype) ORDER BY DESC (?usage) ");
			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet result = qe.execSelect();
			if (result.hasNext()) {
				while (result.hasNext()) {
					QuerySolution querySolution = result.next();
					//System.out.println("datatype : " + querySolution.get("datatype"));
					//System.out.println("usage : " + querySolution.getLiteral("usage"));
					if (querySolution.getLiteral("usage") != null) {
						ListResources.add(new UriAndStringAndBigNumber(querySolution.get("property").toString(), querySolution.get("datatype").toString(), querySolution.getLiteral("usage").getLong()));
					}
				}
			}

		for (UriAndStringAndBigNumber resource : ListResources) {
			if (n == 0) {
				s = model.createResource(dsp + nameOfList);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u = model.createResource(resource.getUri().toString());
				t = model.createLiteral(resource.getStr().toString());
				v = ResourceFactory.createTypedLiteral(resource.getNumber());
				model.add(b, pu, u);
				model.add(b, pd, t);
				model.add(b, pv, v);

				model.add(s, p, b);
				n = n + 1;
			} else {
				s = model.createResource(dsp + nameOfList + n);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u = model.createResource(resource.getUri().toString());
				t = model.createLiteral(resource.getStr().toString());
				v = ResourceFactory.createTypedLiteral(resource.getNumber());
				model.add(b, pu, u);
				model.add(b, pd, t);
				model.add(b, pv, v);

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