package profiling.util;

import java.util.ArrayList;
import org.apache.jena.rdf.model.Model;

public class MakeVectorWithUriAndUriAndUriAndNumber {

	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static String makeVector(Model model, ArrayList<UriAndUriAndUriAndNumber> list) {
	
		// Premier paramètre en entrée	: le modèle
		// Deuxième paramètre en entrée	: le nom de la liste RDF de type <UriAndNumber>
		// Deuxième paramètre en sortie : une chaine litérale de la
		//  forme "c(x, y, z, ....)" (vecteur pour l'utilisation de R)
		//  les valeurs x, y et z sont les valeurs numériques liés au node
		//  par une relation (quelle qu'elle soit)
	
		// List<Double> values = new ArrayList<Double>();
		
		
		String strVector = "c(0.0)";
		
		if (!(list.size()==0)) {
			// On formate le vecteur sous la forme (c(x,y,z,...))
			if (list.size()>1) {
				strVector = "c(" ;
				Boolean first = true;
				for (UriAndUriAndUriAndNumber resource : list) {
					if (first) {
						first = false;
					} else {
						strVector = strVector.concat(",");	
					}
						Double d = (double)resource.getNumber();
						strVector = strVector.concat(d.toString());
				}
				strVector = strVector.concat(")");
				//System.out.println(strVector);
			}	
		}
	
		return strVector;
	}
}