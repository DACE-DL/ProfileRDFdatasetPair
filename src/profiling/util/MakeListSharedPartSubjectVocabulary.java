package profiling.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class MakeListSharedPartSubjectVocabulary {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static List<String> makeList(Model model, List<String> ListVocabulary, String nameOfListOut) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
				
		List<String> ListVocabularyTemp = new ArrayList<String>();
		List<String> ListResources = new ArrayList<String>();

		Integer n = 0;
		
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Literal v = null;
		
		
		ListVocabularyTemp = ListVocabulary;
		for (String vocabulary : ListVocabulary) {
			for (String vocabularyTemp : ListVocabularyTemp) {
				if (vocabulary.equals(vocabularyTemp) ) {
					ListResources.add(vocabulary) ;
				}
			};	

		};

		for (String sharedPart : ListResources) {
			if (n == 0) {
				s = model.createResource(dsp + nameOfListOut);
				p = model.createProperty(rdf + "first");
				v = model.createLiteral(sharedPart.toString());
				model.add(s, p, v);
				n = n + 1;
			} else {
				s = model.createResource(dsp + nameOfListOut + n);
				p = model.createProperty(rdf + "first");
				v = model.createLiteral(sharedPart.toString());
				model.add(s, p, v);

				if (n == 1) {
					s = model.createResource(dsp + nameOfListOut);
					p = model.createProperty(rdf + "rest");
					o = model.createResource(dsp + nameOfListOut + n);
					model.add(s, p, o);
					n = n + 1;
				} else {
					s = model.createResource(dsp + nameOfListOut + (n - 1));
					p = model.createProperty(rdf + "rest");
					o = model.createResource(dsp + nameOfListOut + n);
					model.add(s, p, o);
					n = n + 1;
				}
			}
		}

		if (n > 0) {

			if (n == 1) {
				s = model.createResource(dsp + nameOfListOut);
				p = model.createProperty(rdf + "rest");
				o = model.createResource(rdf + "nil");
				model.add(s, p, o);
			} else {
				s = model.createResource(dsp + nameOfListOut + (n - 1));
				p = model.createProperty(rdf + "rest");
				o = model.createResource(rdf + "nil");
				model.add(s, p, o);
			}
			s = model.createResource(dsp + nameOfListOut);
			p = model.createProperty(rdf + "type");
			o = model.createResource(rdf + "List");
			model.add(s, p, o);
		}

		return ListResources;
	}

}