package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

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
	public static ArrayList<UriListAndUriList> makeClasses(Model model, ArrayList<UriListAndUriListAndNumberListAndNumber> listCombinationPropertiesPerSubjectCleanedAndReduced) {
		
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
		ArrayList<UriListAndNumberAndUriList> ListNewClassesListAndTheirPropertiesTemp = new ArrayList<UriListAndNumberAndUriList>();
		ArrayList<UriListAndUriList> ListNewClassesListAndTheirProperties = new ArrayList<UriListAndUriList>();
		
		Instant start0 = Instant.now();	
		
		// On créer une version temporaire de listCombinationPropertiesPerSubjectCleanedAndReduced car on modifie son ordre dans le programme
		ArrayList<UriListAndUriListAndNumberListAndNumber> listCombinationPropertiesPerSubjectCleanedAndReducedTemp = new ArrayList<UriListAndUriListAndNumberListAndNumber>();
		for (UriListAndUriListAndNumberListAndNumber obj : listCombinationPropertiesPerSubjectCleanedAndReduced) {
            UriListAndUriListAndNumberListAndNumber newObj = new UriListAndUriListAndNumberListAndNumber(obj); // Créez un nouvel objet en copiant les valeurs de obj
            listCombinationPropertiesPerSubjectCleanedAndReducedTemp.add(newObj);
        }

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
					// System.out.println("ListCombinaisonProperties : " + ListCombinaisonProperties.toString());
					// System.out.println("queryString : " + queryString);
					Query query = QueryFactory.create(prefix + queryString
					);		
					if (!ListCombinaisonProperties.toString().equals("[]")) { 
						QueryExecution qe = QueryExecutionFactory.create(query, model);		
						ResultSet result = qe.execSelect();
						if (result.hasNext()) {
							o = model.createResource(dsp + "Class-" + n);
							n++;
							model.add(o, p, c);
							ArrayList<Uri> listClasses = new ArrayList<Uri>();
							Uri classe = new Uri(o.getURI());
							listClasses.add(classe);
							UriListAndNumberAndUriList NewClasseListAndTheseProperties = new UriListAndNumberAndUriList(listClasses, listClass.getNumber(), ListCombinaisonProperties);
							ListNewClassesListAndTheirPropertiesTemp.add(NewClasseListAndTheseProperties);
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

				} else {
					UriListAndNumberAndUriList NewClasseListAndTheseProperties = new UriListAndNumberAndUriList(listClass.getUriList(), listClass.getNumber(),ListCombinaisonProperties);
					ListNewClassesListAndTheirPropertiesTemp.add(NewClasseListAndTheseProperties);
				}
			}
		}
		
		// Tri de la liste pour que les combinaisons les plus grandes en nombre passe en priorité
		Collections.sort(ListNewClassesListAndTheirPropertiesTemp, new UriListAndNumberAndUriListComparator());

		// La liste se présente maintenant par une liste de classes unique et sa combinaison de propriétés, 
		//  il peut donc y avoir des doublons : une même liste de classes avec des combinaisons de propriétés
		//  proches mais différentes on cherche donc les propriétés communes une fois de plus ! 
		
		ArrayList<UriListAndNumberAndUriList> ListNewClassesListAndTheirPropertiesTemp2 = new ArrayList<UriListAndNumberAndUriList>();
		ListNewClassesListAndTheirPropertiesTemp2.addAll(ListNewClassesListAndTheirPropertiesTemp);
		
		// Traitement des listes de classes (uniques)
		ArrayList<String> listClassTreated = new ArrayList<String>();
        for (UriListAndNumberAndUriList resource : ListNewClassesListAndTheirPropertiesTemp) {		
			String classesTreated = resource.getUriList1().toString();
			if (!listClassTreated.contains(classesTreated)) {
				ArrayList<Uri> listResultProperties =  new ArrayList<Uri>();
				ArrayList<Uri> listProperties =  new ArrayList<Uri>();
				listProperties.addAll(resource.getUriList2());
				listResultProperties.addAll(resource.getUriList2());
				//System.out.println("Test glop");
				Integer number = resource.getNumber();
				for (UriListAndNumberAndUriList resourceTemp : ListNewClassesListAndTheirPropertiesTemp2) {
					String classesTreatedTemp = resourceTemp.getUriList1().toString();
					if (classesTreatedTemp.equals(classesTreated)) {
						// On applique la règle du 1% 
						// Si le nombre d'instances pour cette liste de classe identique (mais qui n'a pas
						//  la même combinaison de propriété) n'est pas 1% des cas précedents on l'abandonne.
						if (number <= 0) { // Premier passage (forcément obligatoire car ListNewClassesListAndTheirPropertiesTemp2 est une copie de ListNewClassesListAndTheirPropertiesTemp)
							number = number + resourceTemp.getNumber();
						} else { // Passages suivants
							if (((resourceTemp.getNumber() * 100) / number) > 1) {
								//System.out.println(number);
								//System.out.println(resourceTemp.getNumber());
								number = number + resourceTemp.getNumber();
								ArrayList<Uri> listPropertiesTemp =  new ArrayList<Uri>();
								listPropertiesTemp.addAll(resourceTemp.getUriList2());
								ArrayList<Uri> commonElements = getCommonElements(listProperties, listPropertiesTemp);
								Integer listPropertiesSize = listProperties.size();
								Integer listPropertiesTempSize = listPropertiesTemp.size();
								Integer listPropertiesSizeMax = 0;
								if (listPropertiesSize >= listPropertiesTempSize) {
									listPropertiesSizeMax = listPropertiesSize;
								} else {
									listPropertiesSizeMax = listPropertiesTempSize;
								}
								if (commonElements.size()>0) {
									// On ne fait la selection que si le nombre d'éléments communs est au moins supérieur
									//  au 2/3 de la plus grande liste  
									if (commonElements.size() >= (listPropertiesSizeMax*2/3)) {
										//System.out.println("Glop");
										listResultProperties.clear();
										listResultProperties.addAll(commonElements);
										listProperties.clear();
										listProperties.addAll(commonElements);
									}	else {
										//System.out.println("Pas glop");
									}

								}
							}	
						}	 		
					}
				}
				
				UriListAndUriList listNewClassesListAndTheirProperties = new UriListAndUriList(resource.getUriList1(), listResultProperties);
				ListNewClassesListAndTheirProperties.add(listNewClassesListAndTheirProperties);
				listClassTreated.add(classesTreated);
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

	static class UriListAndNumberAndUriListComparator implements java.util.Comparator<UriListAndNumberAndUriList> {
		@Override
		public int compare(UriListAndNumberAndUriList a, UriListAndNumberAndUriList b) {
			return b.getNumber() - a.getNumber();
		}
	}

	// Méthode pour extraire les éléments communs entre deux listes
    public static ArrayList<Uri> getCommonElements(ArrayList<Uri> list1, ArrayList<Uri> list2) {
        // Utilisation d'un HashSet pour une recherche plus rapide des éléments communs
        HashSet<Uri> set1 = new HashSet<>(list1);
        HashSet<Uri> set2 = new HashSet<>(list2);

        // Retenir seulement les éléments communs
        set1.retainAll(set2);

        // Conversion du HashSet en ArrayList pour la cohérence
        return new ArrayList<>(set1);
    }
}

