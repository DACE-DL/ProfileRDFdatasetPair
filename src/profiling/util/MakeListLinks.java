package profiling.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class MakeListLinks {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndUri> makeList(Model model, String nameOfPropertyList, String nameOfList) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
		List<String> listResourcesString = new ArrayList<>();
		ArrayList<Uri> listProperties = new ArrayList<Uri>();

		Integer n = 0;
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Resource b = model.createResource();
		Resource u1 = model.createResource(dsp + "property");
		Resource u2 = model.createResource(dsp + "subproperty");
		Property pu1 = model.createProperty(dsp + "asNamespaceSubject");
		Property pu2 = model.createProperty(dsp + "asNamespaceObject");
	
		//System.out.println("Begin time : " + Instant.now().toString()); 

		Query query = QueryFactory.create(prefix + 
			"SELECT (?uri AS ?property) WHERE { " +
			" dsp:" + nameOfPropertyList + " rdf:rest*/rdf:first ?element ." +
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
		//System.out.println("List size : " + listProperties.size());
		//Instant start4 = Instant.now();
		listProperties.forEach((property) -> {
			//System.out.println("property : " + property.getUri().toString());
			Query query2 = QueryFactory.create(prefix +
				" SELECT ?s (<" + property.getUri().toString() + "> AS ?property) ?o WHERE { " +
					" ?s <" + property.getUri().toString() + "> ?o " +
					" FILTER isIRI(?s) " +
					" FILTER isIRI(?o) " +
				" } "
			);
			
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model);
			ResultSet result2 = qe2.execSelect();
			if (result2.hasNext()) {
				//System.out.println("query OK !!!!!!!!!!!!!!!!!!");
				while (result2.hasNext()) {
					QuerySolution querySolution = result2.next();
					Resource subject = querySolution.getResource("s");
					Resource object = querySolution.getResource("o") ;
					//System.out.println("uri : " + object.getURI().toString());
					//System.out.println("split : " + object.getURI().substring(1, SplitIRI.splitpoint(object.getURI())) );
					if (!(subject.isAnon() || object.isAnon())) {		
						Pattern pat = Pattern.compile("^(((ftp|http|https):\\/\\/)?(www.)?[a-zA-Z0-9\\.-]*(\\/)?).*");
						String nameDomainSubject = subject.getNameSpace();
						String nameDomainObject = object.getNameSpace();
						
						Matcher matSubject = pat.matcher(subject.getNameSpace());
						if (matSubject.find() ){
							nameDomainSubject = matSubject.group(1);
						} 
						if (nameDomainSubject.equals("http://http/")) {
							nameDomainSubject = subject.getNameSpace();	
						}
						if (nameDomainSubject.equals("http://")) {
							nameDomainSubject = subject.getURI();	
						}
						
						Matcher matObject = pat.matcher(object.getNameSpace());
						if (matObject.find() ){
							nameDomainObject = matObject.group(1);
						}
						if (nameDomainObject.equals("http://http/")) {
								nameDomainObject = object.getNameSpace();	
						}
						if (nameDomainObject.equals("http://")) {
							nameDomainObject = object.getURI();	
						} 
						
						if (!nameDomainSubject.equals(nameDomainObject)) {
							String SubNsObNs = nameDomainSubject + nameDomainObject;
							if (!listResourcesString.contains(SubNsObNs)) { 
								listResourcesString.add(SubNsObNs);
								ListResources.add(new UriAndUri(nameDomainSubject, nameDomainObject)) ;
							}	
						}
					}	
				}
			}
		});
		//Instant end4 = Instant.now();
		//System.out.println("All Links running time : " + Duration.between(start4, end4).getSeconds() + " secondes");

		for (UriAndUri resource : ListResources) {
			if (n == 0) {
				s = model.createResource(dsp + nameOfList);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri1().toString());
				u2 = model.createResource(resource.getUri2().toString());
				model.add(b, pu1, u1);
				model.add(b, pu2, u2);

				model.add(s, p, b);
				n = n + 1;
			} else {
				s = model.createResource(dsp + nameOfList + n);
				p = model.createProperty(rdf + "first");
				
				b = model.createResource();
				u1 = model.createResource(resource.getUri1().toString());
				u2 = model.createResource(resource.getUri2().toString());
				model.add(b, pu1, u1);
				model.add(b, pu2, u2);

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