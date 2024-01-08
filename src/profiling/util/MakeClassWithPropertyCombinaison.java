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

public class MakeClassWithPropertyCombinaison {
	
	// Création de Classes pour chaque combinaison de propriétés
	public static void makeClasses(Model model, ArrayList<UriListAndUriAndNumberListAndNumber> ListResources) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String rdfs = ProfilingConf.rdfs;
		String prefix = ProfilingConf.queryPrefix;

		Integer n = 0;
		Property p = model.createProperty(rdf + "type");
		Resource o = model.createResource(dsp + "objet");
		Resource c = model.createResource(rdfs + "Class");
		ArrayList<Resource> ListInstancesOfCombinaisonProperties =  new ArrayList<Resource>();;
		
		for (UriListAndUriAndNumberListAndNumber resource : ListResources) {
        	n++;
			ArrayList<Uri> ListCombinaisonProperties = resource.getUriList();
			String queryString = buildSparqlString(ListCombinaisonProperties);
			Query query = QueryFactory.create(prefix + queryString
			);			
 			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			if (result.hasNext()) {
				o = model.createResource(dsp + "Class" + n);
				// System.out.println("o : " + o.toString());
				// System.out.println("p : " + p.toString());
				// System.out.println("c : " + c.toString());
				model.add(o, p, c);
				while( result.hasNext() ) {
					QuerySolution querySolution = result.next() ;
					ListInstancesOfCombinaisonProperties.add(model.createResource(querySolution.getResource("subject").toString()));
				}
				for (Resource s : ListInstancesOfCombinaisonProperties) {
					model.add(s, p, o);
				}
			}

			if (n>9) {
				break;
		   	}		
		}
	}

    private static String buildSparqlString(ArrayList<Uri> properties) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ?subject WHERE { ");
        // Ajout des triplets pour chaque propriété dans la liste
        Integer x = 0;
		for (Uri property : properties) {
			x++;
            queryBuilder.append("?subject <").append(property.getUri().toString()).append("> ?object").append(x).append(" . ");
        }
        queryBuilder.append("}");
        // Retourne la requête en tant que chaîne de caractères
		//System.out.println(queryBuilder.toString());
        return queryBuilder.toString();
    }
}

