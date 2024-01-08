package profiling.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

public class MakeTestInsert {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static void make(Model model) {
		
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;	
		
		String queryUpdateString = prefix + 
		" INSERT DATA {dsp:bibi <http://purl.org/dc/elements/1.1/rights> dsp:boboRight ."+
		" dsp:bibi <http://xmlns.com/foaf/0.1/thumbnail> dsp:boboThumbnail .}";			
		UpdateRequest update = UpdateFactory.create(queryUpdateString);
		UpdateAction.execute(update, model);
	}
}
