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
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class MakeListClassOfInterest {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUriAndUri> makeList(Model model, String nameOfList) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUriAndUri> ListResources = new ArrayList<UriAndUriAndUri>();
		ArrayList<Uri> listProperties = new ArrayList<Uri>();
		ArrayList<UriAndUri> listClassSubjectAndProperties = new ArrayList<UriAndUri>();
		ArrayList<UriAndUri> listClassObjectAndProperties = new ArrayList<UriAndUri>();
	
		Integer n = 0;
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Resource b = model.createResource();
		Resource u1 = model.createResource(dsp + "uri1");
		Resource u2 = model.createResource(dsp + "uri2");
		Resource u3 = model.createResource(dsp + "uri3");
		Property pu1 = model.createProperty(dsp + "asURI1");
		Property pu2 = model.createProperty(dsp + "asURI2");
		Property pu3 = model.createProperty(dsp + "asURI3");
		
		Query query = QueryFactory.create(prefix + 
			"SELECT (?uri AS ?property) WHERE { " +
			" dsp:listPropertyMostUsed rdf:rest*/rdf:first ?element ." +
			" ?element dsp:asURI ?uri ." +
			" ?element dsp:asValue ?val ." +
			" } ORDER BY DESC (?val)"
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				listProperties.add(new Uri(querySolution.getResource("property").toString())) ;
			}
		}

		//System.out.println("Begin time : " + Instant.now().toString());
		//Instant start0 = Instant.now();
		listProperties.forEach((property) -> {
			//System.out.println("Property : " + property.getUri());
			//Instant start1 = Instant.now();

			Query query2 = QueryFactory.create(prefix +
				" SELECT ?class1 WHERE { " +
					" dsp:listClassMostUsed rdf:rest*/rdf:first ?element1 ." +
					" ?element1 dsp:asURI ?class1 ." +
					" BIND (IRI(?class1) AS ?iriClass1 )" +
					" FILTER EXISTS { " +
					" 	?ic1 rdf:type ?iriClass1 ." +
					"   ?ic1 <" + property.getUri() + "> ?ic2 ." +
					" }" +
				" } "
			);
			// System.out.println(query2);
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model);
			ResultSet result2 = qe2.execSelect();
			if (result2.hasNext()) {
				// System.out.println("query OK !!!!!!!!!!!!!!!!!!");
				while (result2.hasNext()) {
					QuerySolution querySolution = result2.next();
					listClassSubjectAndProperties.add(new UriAndUri(querySolution.getResource("class1").toString(),
							property.getUri()));
				}
				//Instant end1 = Instant.now();
				//System.out.println("Property running time : " + Duration.between(start1, end1).getSeconds() + " secondes");
			}
		});
		//Instant end0 = Instant.now();
		//System.out.println("All Class Subjet and properties running time : " + Duration.between(start0, end0).getSeconds() + " secondes");

		//Instant start2 = Instant.now();
		listProperties.forEach((property) -> {
			//System.out.println("Property : " + property.getUri());
			//Instant start3 = Instant.now();

			Query query2 = QueryFactory.create(prefix +
				" SELECT ?class1 WHERE { " +
					" dsp:listClassMostUsed rdf:rest*/rdf:first ?element1 ." +
					" ?element1 dsp:asURI ?class1 ." +
					" BIND (IRI(?class1) AS ?iriClass1 )" +
					" FILTER EXISTS { " +
					" 	?ic2 rdf:type ?iriClass1 ." +
					"   ?ic1 <" + property.getUri() + "> ?ic2 ." +
					" }" +
				" } "
			);
			// System.out.println(query2);
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model);
			ResultSet result2 = qe2.execSelect();
			if (result2.hasNext()) {
				// System.out.println("query OK !!!!!!!!!!!!!!!!!!");
				while (result2.hasNext()) {
					QuerySolution querySolution = result2.next();
					listClassObjectAndProperties.add(new UriAndUri(querySolution.getResource("class1").toString(),
							property.getUri()));
				}
				//Instant end3 = Instant.now();
				//System.out.println("Property running time : " + Duration.between(start3, end3).getSeconds() + " secondes");
			}
		});
		//Instant end2 = Instant.now();
		//System.out.println("All Class Objet and properties running time : " + Duration.between(start2, end2).getSeconds() + " secondes");
		
		Instant start4 = Instant.now();
		listProperties.forEach((property) -> {
			// Instant start5 = Instant.now();
			listClassSubjectAndProperties.forEach((classSubjectAndProperty) -> {
				
				if (classSubjectAndProperty.getUri2().equals(property.getUri())) {
				
					//System.out.println("Class subject : " + classSubjectAndProperty.getUri1());
					
					listClassObjectAndProperties.forEach((classObjectAndProperty) -> {
					
						if (classObjectAndProperty.getUri2().equals(property.getUri())) {	

							//System.out.println("Class object : " + classObjectAndProperty.getUri1());

							//Instant start5 = Instant.now();
							Query query2 = QueryFactory.create(prefix +
								" SELECT (<" + classSubjectAndProperty.getUri1().toString() + "> AS ?class1) (<" + classObjectAndProperty.getUri1().toString() + "> AS ?class2) WHERE { " +
									" FILTER EXISTS { " +
									" 	?ic1 rdf:type <" +  classSubjectAndProperty.getUri1()  + "> ." +
									" 	?ic2 rdf:type <" +  classObjectAndProperty.getUri1()  + "> ." +
									"   ?ic1 <" + property.getUri() + "> ?ic2 ." +
									" }" +
								" } "
							);
							// System.out.println(query2);
							QueryExecution qe2 = QueryExecutionFactory.create(query2, model);
							ResultSet result2 = qe2.execSelect();
							if (result2.hasNext()) {
								// System.out.println("query OK !!!!!!!!!!!!!!!!!!");
								while (result2.hasNext()) {
									QuerySolution querySolution = result2.next();
									ListResources.add(new UriAndUriAndUri(querySolution.getResource("class1").toString(),
											property.getUri(), querySolution.getResource("class2").toString()));
								}
								
							}
						}	
					});
				}	
			});
			// Instant end5 = Instant.now();
			// System.out.println("Property : " + property.getUri());
			// System.out.println("Property running time : " + Duration.between(start5, end5).getSeconds() + " secondes");
		});	
		Instant end4 = Instant.now();
		System.out.println("All Class Subjet and Object and properties running time : " + Duration.between(start4, end4).getSeconds() + " secondes");


		
		for (UriAndUriAndUri resource : ListResources) {
			if (n == 0) {
				s = model.createResource(dsp + nameOfList);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri1().toString());
				u2 = model.createResource(resource.getUri2().toString());
				u3 = model.createResource(resource.getUri3().toString());
				
				model.add(b, pu1, u1);
				model.add(b, pu2, u2);
				model.add(b, pu3, u3);
				
				model.add(s, p, b);
				n = n + 1;
			} else {
				s = model.createResource(dsp + nameOfList + n);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri1().toString());
				u2 = model.createResource(resource.getUri2().toString());
				u3 = model.createResource(resource.getUri3().toString());
				
				model.add(b, pu1, u1);
				model.add(b, pu2, u2);
				model.add(b, pu3, u3);

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

	static class UriAndNumberComparator implements java.util.Comparator<UriAndNumber> {
		@Override
		public int compare(UriAndNumber a, UriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}