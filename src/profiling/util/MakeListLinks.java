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
import org.apache.jena.rdf.model.Resource;

public class MakeListLinks {
	
	// Création d'une liste des liens dans un triplet
	public static ArrayList<UriAndUri> makeList(Model model, ArrayList<UriAndNumber> listPropertyUsageCount) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
		List<String> listResourcesString = new ArrayList<>();
		
		listPropertyUsageCount.forEach((property) -> {
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
		
		return ListResources;
	}
}