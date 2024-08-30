package profiling.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class VerifyLinks {
	
	// Création d'une liste des liens dans un triplet
	public static ArrayList<UriAndUriAndNumber> makeList(Model model, ArrayList<UriAndNumber> listPropertyUsageCount, ArrayList<Uri> listModelNameSpace) {
    
		String prefix = ProfilingConf.queryPrefix;
	
		ArrayList<UriAndUriAndNumber> ListResources = new ArrayList<UriAndUriAndNumber>();
		Map<String, UriAndUriAndNumber> resourceMap = new HashMap<>();
	
		listPropertyUsageCount.forEach((property) -> {
			Query query2 = QueryFactory.create(prefix +
				" SELECT ?s (<" + property.getUri().toString() + "> AS ?property) ?o WHERE { " +
				" ?s <" + property.getUri().toString() + "> ?o " +
				" FILTER isIRI(?s) " +
				" } "
			);
	
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model);
			ResultSet result2 = qe2.execSelect();
			if (result2.hasNext()) {
				while (result2.hasNext()) {
					QuerySolution querySolution = result2.next();
					Resource subject = querySolution.getResource("s");
					RDFNode objectNode = querySolution.get("o");
	
					if (objectNode.isResource()) {
						Resource object = querySolution.getResource("o");
						if (!(subject.isAnon() || object.isAnon())) {
							String nameDomainSubject = normalizeNamespace(subject.getNameSpace(), subject.getURI(), listModelNameSpace);
							String nameDomainObject = normalizeNamespace(object.getNameSpace(), object.getURI(), listModelNameSpace);
	
							String key = nameDomainSubject + nameDomainObject;
							resourceMap.computeIfAbsent(key, k -> new UriAndUriAndNumber(nameDomainSubject, nameDomainObject, 0)).incrementNumber();
						}
					} else {
						if (!subject.isAnon()) {
							String nameDomainSubject = normalizeNamespace(subject.getNameSpace(), subject.getURI(), listModelNameSpace);
							String key = nameDomainSubject + "";
							resourceMap.computeIfAbsent(key, k -> new UriAndUriAndNumber(nameDomainSubject, "", 0)).incrementNumber();
						}
					}
				}
			}
		});
	
		ListResources.addAll(resourceMap.values());
		ListResources.sort((o1, o2) -> Integer.compare(o2.getNumber(), o1.getNumber()));
		return ListResources;
	}
	
	private static String normalizeNamespace(String namespace, String uri, ArrayList<Uri> listModelNameSpace) {
		// Vérifiez si l'URI contient l'un des noms de domaine
        for (Uri domain : listModelNameSpace) {
            if (uri.contains(domain.getUri())) {
                return domain.getUri();
            }
        }
		
		Pattern pat = Pattern.compile("^(((ftp|http|https):\\/\\/)?(www.)?[a-zA-Z0-9\\.-]*(\\/)?).*");
		Matcher mat = pat.matcher(namespace);
		if (mat.find()) {
			String normalized = mat.group(1);
			if (normalized.equals("http://http/") || normalized.equals("http://") || normalized.equals("https://") || normalized.equals("ftp://") || normalized.equals("www.")) {
				return uri;
			}
			return normalized;
		}
		return namespace;
	}
	
}