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

public class MakeListCombinationPropertiesWithNewClass {
	static boolean alreadyProcessed = false; 
	// Création de Classes pour chaque combinaison de propriétés
	public static ArrayList<UriListAndUriList> makeClasses(Model model, ArrayList<UriListAndUriListAndNumberListAndNumber> listCombinationPropertiesPerSubjectCleanedAndReducedTemp) {
		
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
		ArrayList<UriListAndUriList> ListNewClassesListAndTheirProperties = new ArrayList<UriListAndUriList>();
		
		Instant start0 = Instant.now();	
		
		// Tri de la liste pour que les combinaisons les plus grandes en nombre passe en priorité
		// Afin de ne pas affecter une nouvelle classe en doublon
		Collections.sort(listCombinationPropertiesPerSubjectCleanedAndReducedTemp, new UriListAndUriListAndNumberListAndNumberComparator()); 	
		
		n = 1;
		for (UriListAndUriListAndNumberListAndNumber resource : listCombinationPropertiesPerSubjectCleanedAndReducedTemp) {      	
			ArrayList<Uri> ListCombinaisonProperties = resource.getUriList();
			ArrayList<UriListAndNumber> ListClassesListForCombinaison = resource.getUriListAndNumberList();
			for (UriListAndNumber listClass : ListClassesListForCombinaison) {
				// !!! Si dans une liste de classe il n'y à pas de classe ie "", il n'y a forcément qu'un seul élément dans la liste
				if ( listClass.getUriList().get(0).getUri() == "") {
					// On creer une nouvelle classe que l'on attribut aux instances concernées dans le modèle 
					String queryString = buildSparqlString(ListCombinaisonProperties);
					Query query = QueryFactory.create(prefix + queryString
					);		
					QueryExecution qe = QueryExecutionFactory.create(query, model);		
					ResultSet result = qe.execSelect();
					if (result.hasNext()) {
						o = model.createResource(dsp + "Class-" + n);
						n++;
						model.add(o, p, c);
						ArrayList<Uri> listClasses = new ArrayList<Uri>();
						Uri classe = new Uri(o.getURI());
						listClasses.add(classe);
						UriListAndUriList NewClasseListAndTheseProperties = new UriListAndUriList(listClasses, ListCombinaisonProperties);
						ListNewClassesListAndTheirProperties.add(NewClasseListAndTheseProperties);
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

				} else {
					UriListAndUriList NewClasseListAndTheseProperties = new UriListAndUriList(listClass.getUriList(), ListCombinaisonProperties);
					ListNewClassesListAndTheirProperties.add(NewClasseListAndTheseProperties);
				}
			}
		}
		Instant end0 = Instant.now();
		System.out.println("Running time for ListCombinationPropertiesWithNewClass: " + ProfilingUtil.getDurationAsString(Duration.between(start0, end0).toMillis()));
		return ListNewClassesListAndTheirProperties;
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
	static class UriListAndUriListAndNumberListAndNumberComparator implements java.util.Comparator<UriListAndUriListAndNumberListAndNumber> {
		@Override
		public int compare(UriListAndUriListAndNumberListAndNumber a, UriListAndUriListAndNumberListAndNumber b) {
			return b.getUriList().size() - a.getUriList().size();
		}
	}
}

