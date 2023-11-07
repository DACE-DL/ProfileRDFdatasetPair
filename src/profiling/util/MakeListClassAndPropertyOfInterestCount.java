package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;


public class MakeListClassAndPropertyOfInterestCount {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndUriAndNumber> makeList(Model model, String nameOfList) {
		
		new ProfilingConf();
		//String dsp = ProfilingConf.dsp;
		//String rdf = ProfilingConf.rdf;
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriAndUriAndNumber> ListResources = new ArrayList<UriAndUriAndUriAndNumber>();
		ArrayList<Uri> listProperties = new ArrayList<Uri>();
		ArrayList<UriAndUri> listClassSubjectAndProperties = new ArrayList<UriAndUri>();
		ArrayList<UriAndUri> listClassObjectAndProperties = new ArrayList<UriAndUri>();
		
		// Integer n = 0;
		// Resource s = model.createResource(dsp + "sujet");
		// Property p = model.createProperty(dsp + "predicat");
		// Resource o = model.createResource(dsp + "objet");
		// Resource b = model.createResource();
		// Resource u1 = model.createResource(dsp + "uri1");
		// Resource u2 = model.createResource(dsp + "uri2");
		// Resource u3 = model.createResource(dsp + "uri3");
		// Literal v = null;
		// Property pu1 = model.createProperty(dsp + "asURI1");
		// Property pu2 = model.createProperty(dsp + "asURI2");
		// Property pu3 = model.createProperty(dsp + "asURI3");
		// Property pv = model.createProperty(dsp + "asValue");
		
		Instant start4 = Instant.now();

		Query query = QueryFactory.create(prefix + 
			"SELECT (?uri AS ?property) WHERE { " +
			" dsp:listPropertyMostUsed rdf:rest*/rdf:first ?element ." +
			" ?element dsp:asURI ?uri ." +
			" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				listProperties.add(new Uri(querySolution.getResource("property").toString())) ;
			}
		}

		//Instant start3 = Instant.now();
		listProperties.forEach((property) -> {
			Query query2 = QueryFactory.create(prefix +
				" SELECT ?class1 WHERE { " +
					" dsp:listClassMostUsed rdf:rest*/rdf:first ?element1 ." +
					" ?element1 dsp:asURI ?class1 ." +
					" BIND (IRI(?class1) AS ?iriClass1 )" +
					" FILTER EXISTS { " +
					" 	?ic1 rdf:type ?iriClass1 ." +
					"   ?ic1 <" + property.getUri() + "> ?ic2 ." +
					"   FILTER isIRI(?ic2) " +
					" }" +
				" } "
			);
	
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model);
			ResultSet result2 = qe2.execSelect();
			if (result2.hasNext()) {
				while (result2.hasNext()) {
					QuerySolution querySolution = result2.next();
					listClassSubjectAndProperties.add(new UriAndUri(querySolution.getResource("class1").toString(),
							property.getUri()));
				}
			}
		});
		//Instant end3 = Instant.now();
		//System.out.println("Class Subjet and properties running time : " + Duration.between(start3, end3).getSeconds() + " secondes");
		//System.out.println("Class Subjet size : " + listClassSubjectAndProperties.size());

		listProperties.forEach((property) -> {
			Query query2 = QueryFactory.create(prefix +
				" SELECT ?class1 WHERE { " +
					" dsp:listClassMostUsed rdf:rest*/rdf:first ?element1 ." +
					" ?element1 dsp:asURI ?class1 ." +
					" BIND (IRI(?class1) AS ?iriClass1 )" +
					" FILTER EXISTS { " +
					" 	?ic2 rdf:type ?iriClass1 ." +
					"   ?ic1 <" + property.getUri() + "> ?ic2 ." +
					"   FILTER isIRI(?ic2) " +
					" }" +
				" } "
			);
			
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model);
			ResultSet result2 = qe2.execSelect();
			if (result2.hasNext()) {
				while (result2.hasNext()) {
					QuerySolution querySolution = result2.next();
					listClassObjectAndProperties.add(new UriAndUri(querySolution.getResource("class1").toString(),
							property.getUri()));
				}
			}
		});
		
		listProperties.forEach((property) -> {
			listClassSubjectAndProperties.forEach((classSubjectAndProperty) -> {
				if (classSubjectAndProperty.getUri2().equals(property.getUri())) {
			
					listClassObjectAndProperties.forEach((classObjectAndProperty) -> {
						if (classObjectAndProperty.getUri2().equals(property.getUri())) {	

							Query query2 = QueryFactory.create(prefix +
							" SELECT  ?class1 ?property ?class2 (COUNT(*) AS ?usage)" +
							" WHERE { " +
							"   BIND (IRI(STR(<" + classSubjectAndProperty.getUri1().toString() + ">)) AS ?class1) ." +
							"   BIND (IRI(STR(<" + classObjectAndProperty.getUri1().toString() + ">)) AS ?class2) ." +
							"   BIND (IRI(STR(<" + classObjectAndProperty.getUri2().toString() + ">)) AS ?property) ." +
							" 	?ic1 rdf:type ?class1 ." +
							" 	?ic2 rdf:type ?class2 ." +
							"   ?ic1 ?property ?ic2 ." +
							" } GROUP BY ?class1 ?property ?class2 ORDER BY DESC(?usage) "
							);
							
							QueryExecution qe2 = QueryExecutionFactory.create(query2, model);
							ResultSet result2 = qe2.execSelect();
							if (result2.hasNext()) {
								while (result2.hasNext()) {
									QuerySolution querySolution = result2.next();
									ListResources.add(new UriAndUriAndUriAndNumber(
										querySolution.getResource("class1").toString(),
										querySolution.getResource("property").toString(),
										querySolution.getResource("class2").toString(),
										querySolution.getLiteral("usage").getInt()));
								}
							}
						}	
					});
				}	
			});

		});
		
		Instant end4 = Instant.now();
		System.out.println("All Class Subjet and Object and properties running time : " + Duration.between(start4, end4).toMillis() + " millisecondes");

		// Collections.sort(ListResources, new UriAndUriAndUriAndNumberComparator());
		
		// for (UriAndUriAndUriAndNumber resource : ListResources) {
		// 	if (n == 0) {
		// 		s = model.createResource(dsp + nameOfList);
		// 		p = model.createProperty(rdf + "first");
				
		// 		b = model.createResource();
		// 		u1 = model.createResource(resource.getUri1().toString());
		// 		u2 = model.createResource(resource.getUri2().toString());
		// 		u3 = model.createResource(resource.getUri3().toString());
		// 		v = ResourceFactory.createTypedLiteral(resource.getNumber());
				
		// 		model.add(b, pu1, u1);
		// 		model.add(b, pu2, u2);
		// 		model.add(b, pu3, u3);
		// 		model.add(b, pv, v);
				
		// 		model.add(s, p, b);
		// 		n = n + 1;
		// 	} else {
		// 		s = model.createResource(dsp + nameOfList + n);
		// 		p = model.createProperty(rdf + "first");
				
		// 		b = model.createResource();
		// 		u1 = model.createResource(resource.getUri1().toString());
		// 		u2 = model.createResource(resource.getUri2().toString());
		// 		u3 = model.createResource(resource.getUri3().toString());
		// 		v = ResourceFactory.createTypedLiteral(resource.getNumber());
				
		// 		model.add(b, pu1, u1);
		// 		model.add(b, pu2, u2);
		// 		model.add(b, pu3, u3);
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

	static class UriAndUriAndUriAndNumberComparator implements java.util.Comparator<UriAndUriAndUriAndNumber> {
		@Override
		public int compare(UriAndUriAndUriAndNumber a, UriAndUriAndUriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}