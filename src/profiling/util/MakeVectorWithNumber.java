package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeVectorWithNumber {

	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static String makeVector(Model model, String nameOfList) {
	
		// Premier paramètre en entrée	: le modèle
		// Deuxième paramètre en entrée	: le nom de la liste RDF de type <UriAndNumber>
		// Deuxième paramètre en sortie : une chaine litérale de la
		//  forme "c(x, y, z, ....)" (vecteur pour l'utilisation de R)
		//  les valeurs x, y et z sont les valeurs numériques liés au node
		//  par une relation (quelle qu'elle soit)
	
        new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;
		List<Double> values = new ArrayList<Double>();
		
		
		String strVector = "c(0.0)";
			
		Query query = QueryFactory.create(prefix + 
			"SELECT (?val AS ?usage) WHERE {"+
			"dsp:" + nameOfList +				
			" rdf:rest*/rdf:first ?element ." +
			" ?element dsp:asValue ?val ." +
			" } "
		);	
		//System.out.println(query);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				values.add(querySolution.getLiteral("usage").getDouble()) ;				
			}
		}
		
		if (!(values.size()==0)) {
			Collections.sort(values);
			// On formate le vecteur sous la forme (c(x,y,z,...))
			if (values.size()>1) {
				strVector = "c(" ;
				Boolean first = true;
				for (Double d : values) {
					if (first) {
						first = false;
					} else {
						strVector = strVector.concat(",");	
					}
						strVector = strVector.concat(d.toString());
				}
				strVector = strVector.concat(")");
				//System.out.println(strVector);
			}	
		}
	
		return strVector;
	}
}