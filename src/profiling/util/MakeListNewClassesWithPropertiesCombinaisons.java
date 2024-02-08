package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

public class MakeListNewClassesWithPropertiesCombinaisons {
	static boolean alreadyProcessed = false; 
	// Création de Classes pour chaque combinaison de propriétés
	public static ArrayList<UriAndUriList> makeClasses(Model model, ArrayList<UriListAndUriAndNumberListAndNumber> cleanedAndReducedListClassSubjectByPropertyCombinationTemp) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String rdfs = ProfilingConf.rdfs;
		String prefix = ProfilingConf.queryPrefix;

		Integer n = 0;
		Integer ni = 0;
		Property p = model.createProperty(rdf + "type");
		Resource o = model.createResource(dsp + "objet");
		Resource c = model.createResource(rdfs + "Class");
		
		ArrayList<Resource> ListInstancesOfCombinaisonProperties =  new ArrayList<Resource>();
		ArrayList<UriAndUriList> ListNewClassesAndTheirProperties = new ArrayList<UriAndUriList>();
		
		Instant start0 = Instant.now();	
		
		// Tri de la liste pour que les combinaisons les plus grandes en nombre passe en priorité
		Collections.sort(cleanedAndReducedListClassSubjectByPropertyCombinationTemp, new UriListAndUriAndNumberListAndNumberComparator()); 	
		
		n = 0;
		for (UriListAndUriAndNumberListAndNumber resource : cleanedAndReducedListClassSubjectByPropertyCombinationTemp) {
        	n++;
			ArrayList<Uri> ListCombinaisonProperties = resource.getUriList();
			ArrayList<UriAndNumber> ListClassesForCombinaison = resource.getUriAndNumberList();
			// On observe en premier si la combinaison de propriétés ne s'applique qu'à une seule classe d'individus   
			if (ListClassesForCombinaison.size()==1 && ListClassesForCombinaison.get(0).getUri()!="") {
				// On enregistre la classe dans la liste
				UriAndUriList NewClasseAndTheseProperties = new UriAndUriList(ListClassesForCombinaison.get(0).getUri(), ListCombinaisonProperties);
				ListNewClassesAndTheirProperties.add(NewClasseAndTheseProperties);
			} else { // On creer une nouvelle classe que l'on attribut aux instances concernées dans le model 
				String queryString = buildSparqlString(ListCombinaisonProperties);
				Query query = QueryFactory.create(prefix + queryString
				);		
				QueryExecution qe = QueryExecutionFactory.create(query, model);		
				ResultSet result = qe.execSelect();
				if (result.hasNext()) {
					o = model.createResource(dsp + "Class-" + n);
					model.add(o, p, c);
					UriAndUriList NewClasseAndTheseProperties = new UriAndUriList(o.getURI(), ListCombinaisonProperties);
					ListNewClassesAndTheirProperties.add(NewClasseAndTheseProperties);
					ni = 0;
					while( result.hasNext() ) {
						ni++;
						QuerySolution querySolution = result.next() ;
						ListInstancesOfCombinaisonProperties.add(model.createResource(querySolution.getResource("subject").toString()));
						if (ni>999999) {
							break;
						}		
					}
					for (Resource s : ListInstancesOfCombinaisonProperties) {
						Resource ci = null;
						Selector selector1 = new SimpleSelector(s, p, ci) ;
						StmtIterator stmtIte1 = model.listStatements(selector1);
						// On vérifit que les instances concernées n'appartiennent pas déjà
						// à une classe
						if (!stmtIte1.hasNext()) {
							model.add(s, p, o);
						}
					}
					ListInstancesOfCombinaisonProperties.clear();
				}
			}
		}
		Instant end0 = Instant.now();
		System.out.println("Running time for classes with properties combinaisons: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return ListNewClassesAndTheirProperties;
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
        return queryBuilder.toString();
    }
	static class UriListAndUriAndNumberListAndNumberComparator implements java.util.Comparator<UriListAndUriAndNumberListAndNumber> {
		@Override
		public int compare(UriListAndUriAndNumberListAndNumber a, UriListAndUriAndNumberListAndNumber b) {
			return b.getUriList().size() - a.getUriList().size();
		}
	}
}

