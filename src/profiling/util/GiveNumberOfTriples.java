package profiling.util;

import org.apache.jena.rdf.model.Model;

public class GiveNumberOfTriples {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static Integer giveNumber(Model model) {
		Integer n = (int) model.size(); 
		return n;
	}	
}