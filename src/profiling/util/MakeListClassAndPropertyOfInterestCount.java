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
		"SELECT ?property (SUM(?usage) AS ?propertyUsage)  (GROUP_CONCAT(DISTINCT ?classSubjectclassObjectAndCount ; separator=\"|\") AS ?classSubjectclassObjectAndCountList) " +
		" WHERE { " +
			"SELECT ?property (COUNT(?s) AS ?usage) (CONCAT(STR( ?classSubject ), ';', STR(COUNT(?classSubject)), '*' ,STR( ?classObject ), ';', STR(COUNT(?classObject))) AS ?classSubjectclassObjectAndCount)" +
			" WHERE { " +
				" ?s ?property ?o ." +
				" ?s rdf:type ?classSubject ." +	
				" ?o rdf:type ?classObject " +		
				" FILTER EXISTS { " +
				" dsp:listPropertyMostUsed rdf:rest*/rdf:first ?element ." +
				" ?element dsp:asURI ?property ." +
				" } " +	
				" " +
			" } GROUP BY ?property ?classSubject ?classObject ORDER BY DESC (?usage) " +
		" } GROUP BY ?property ORDER BY DESC (?propertyUsage) LIMIT 100"
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				String property = querySolution.getResource("property").toString();
				if (!(querySolution.getLiteral("classSubjectclassObjectAndCountList")==null)) {
					String[] classSubjectclassObjectAndCounts = querySolution.getLiteral("classSubjectclassObjectAndCountList").getString().split("\\|");
					for (String classSubjectclassObjectAndCount : classSubjectclassObjectAndCounts) {
						String[] classSubjectclassObjectCount = classSubjectclassObjectAndCount.split("\\*");
						String[] classSubjectCount = classSubjectclassObjectCount[0].split("\\;");
						String[] classObjectCount = classSubjectclassObjectCount[1].split("\\;");
						ListResources.add(new UriAndUriAndUriAndNumber(
							classSubjectCount[0],
							property,
							classObjectCount[0],
							Integer.valueOf(classSubjectCount[1])
						));
					}
				}
			}
		}
							
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