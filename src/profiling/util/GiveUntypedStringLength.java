package profiling.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

public class GiveUntypedStringLength {
	static Integer n = 0;
	static double l = 0;
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static double giveLength(Model model) {
		double nNumber = 0;
	
		new ProfilingConf();
		String rdf = ProfilingConf.rdf;

		Resource s1 = null;
		Property p1 = null;
		Resource o1 = null;
		List<Resource> listProperty = new ArrayList<>();
		List<RDFNode> listObject = new ArrayList<>();
	
		p1 = model.createProperty(rdf ,"type");
		o1 = model.createResource(rdf + "Property");

		Selector selector = new SimpleSelector(s1, p1, o1) ;
		StmtIterator stmtIte= model.listStatements(selector);
		
		stmtIte.forEach((statement) -> {
            listProperty.add(statement.getSubject());
        });

		listProperty.forEach((property) -> {
			Resource s2 = null;
			Resource o2 = null;
			Selector selector1 = new SimpleSelector(s2, model.createProperty(property.getURI()), o2) ;
			StmtIterator stmtIte1 = model.listStatements(selector1);
			stmtIte1.forEach((stmObj) -> {
				listObject.add(stmObj.getObject());
			});
        });

		// Calcul of the length average
		listObject.forEach((object) -> {
			if (object.isLiteral()) {
				if (object.asLiteral().getDatatype() == null) {
					//System.out.println("n : " + n);
					//System.out.println("literal : " + object.asLiteral().toString());
					//System.out.println("length : " + object.asLiteral().toString().length());
					n ++;
					l = l + object.asLiteral().toString().length();
				}			
			} 
		});

		if (n!=0) {
			nNumber = l/n;
		}
		
		return nNumber;
	}	
}