package profiling.util;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

public class GiveTypedSubjects {
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static Integer giveNumber(Model model) {
		Integer nNumber = 0;
	
		new ProfilingConf();
		String rdf = ProfilingConf.rdf;

		Resource s1 = null;
		Property p1 = null;
		Resource o1 = null;
	
		p1 = model.createProperty(rdf ,"type");
		
		Selector selector = new SimpleSelector(s1, p1, o1) ;
		StmtIterator stmtIte= model.listStatements(selector);
		
		nNumber = IteratorUtils.size(stmtIte);

		stmtIte.close();
		
		return nNumber;
	}	
}