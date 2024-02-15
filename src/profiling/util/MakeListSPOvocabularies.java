package profiling.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;

public class MakeListSPOvocabularies {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<List<String>> makeList(Model model, ArrayList<UriAndNumber> listProperty, String nameOfListS, String nameOfListP, String nameOfListO) {
		
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
				
		ArrayList<List<String>> ListResourcesSPO = new ArrayList<List<String>>(3);
		
		List<String> listDistinctSubjectVocabularies = new ArrayList<>();
		List<String> listDistinctPredicatVocabularies = new ArrayList<>();
		List<String> listDistinctObjectVocabularies = new ArrayList<>();

		Integer n = 0;
		
		Resource s = model.createResource(dsp + "sujet");
		Property p = model.createProperty(dsp + "predicat");
		Resource o = model.createResource(dsp + "objet");
		Literal v = null;
		
		// On boucle sur les propriétés
		listProperty.forEach((property) -> {
			Resource p2 = model.createProperty(property.getUri());
			String nameSpacePredicat = ProfilingUtil.controlNameSpace(p2.getNameSpace());
			if	(!nameSpacePredicat.equals("")) {
				// Duplicate checking
				if (!listDistinctPredicatVocabularies.contains(nameSpacePredicat)) { 
					listDistinctPredicatVocabularies.add(nameSpacePredicat);
				}
			}
			//Pour chaque propriété on extrait les triplets contenant celle çi
			Resource s2 = null;
			Resource o2 = null;
			Selector selector1 = new SimpleSelector(s2, model.createProperty(p2.getURI()), o2) ;
			StmtIterator stmtIte1 = model.listStatements(selector1);

			stmtIte1.forEach((stm) -> {
				// On traite les Sujets pour une propriété donnée
				if (stm.getSubject().isResource()) {
					if (!stm.getSubject().isAnon()) {
						String nameSpaceSubject = ProfilingUtil.controlNameSpace(stm.getSubject().asResource().getNameSpace());
						if	(!nameSpaceSubject.equals("")) {
							// Duplicate checking
							if (!listDistinctSubjectVocabularies.contains(nameSpaceSubject)) { 
								listDistinctSubjectVocabularies.add(nameSpaceSubject);
							}
						}	
					}			
				}
				// On traite les Objets pour une propriété donnée
				if (stm.getObject().isResource()) {
					if (!stm.getObject().isAnon()) {
						String nameSpaceObject = ProfilingUtil.controlNameSpace(stm.getObject().asResource().getNameSpace());
						if	(!nameSpaceObject.equals("")) {
							// Duplicate checking
							if (!listDistinctObjectVocabularies.contains(nameSpaceObject)) { 
								listDistinctObjectVocabularies.add(nameSpaceObject);
							}
						}
					}
				}		
			});	
		});

		ListResourcesSPO.add(listDistinctSubjectVocabularies);
		ListResourcesSPO.add(listDistinctPredicatVocabularies);
		ListResourcesSPO.add(listDistinctObjectVocabularies);

		// for (String vocabulary : listDistinctSubjectVocabularies) {
		// 	if (n == 0) {
		// 		s = model.createResource(dsp + nameOfListS);
		// 		p = model.createProperty(rdf + "first");
		// 		v = model.createLiteral(vocabulary.toString());
		// 		model.add(s, p, v);
		// 		n = n + 1;
		// 	} else {
		// 		s = model.createResource(dsp + nameOfListS + n);
		// 		p = model.createProperty(rdf + "first");
		// 		v = model.createLiteral(vocabulary.toString());
		// 		model.add(s, p, v);

		// 		if (n == 1) {
		// 			s = model.createResource(dsp + nameOfListS);
		// 			p = model.createProperty(rdf + "rest");
		// 			o = model.createResource(dsp + nameOfListS + n);
		// 			model.add(s, p, o);
		// 			n = n + 1;
		// 		} else {
		// 			s = model.createResource(dsp + nameOfListS + (n - 1));
		// 			p = model.createProperty(rdf + "rest");
		// 			o = model.createResource(dsp + nameOfListS + n);
		// 			model.add(s, p, o);
		// 			n = n + 1;
		// 		}
		// 	}
		// }

		// if (n > 0) {

		// 	if (n == 1) {
		// 		s = model.createResource(dsp + nameOfListS);
		// 		p = model.createProperty(rdf + "rest");
		// 		o = model.createResource(rdf + "nil");
		// 		model.add(s, p, o);
		// 	} else {
		// 		s = model.createResource(dsp + nameOfListS + (n - 1));
		// 		p = model.createProperty(rdf + "rest");
		// 		o = model.createResource(rdf + "nil");
		// 		model.add(s, p, o);
		// 	}
		// 	s = model.createResource(dsp + nameOfListS);
		// 	p = model.createProperty(rdf + "type");
		// 	o = model.createResource(rdf + "List");
		// 	model.add(s, p, o);
		// }

		// n = 0;

		// for (String vocabulary : listDistinctPredicatVocabularies) {
		// 	if (n == 0) {
		// 		s = model.createResource(dsp + nameOfListP);
		// 		p = model.createProperty(rdf + "first");
		// 		v = model.createLiteral(vocabulary.toString());
		// 		model.add(s, p, v);
		// 		n = n + 1;
		// 	} else {
		// 		s = model.createResource(dsp + nameOfListP + n);
		// 		p = model.createProperty(rdf + "first");
		// 		v = model.createLiteral(vocabulary.toString());
		// 		model.add(s, p, v);

		// 		if (n == 1) {
		// 			s = model.createResource(dsp + nameOfListP);
		// 			p = model.createProperty(rdf + "rest");
		// 			o = model.createResource(dsp + nameOfListP + n);
		// 			model.add(s, p, o);
		// 			n = n + 1;
		// 		} else {
		// 			s = model.createResource(dsp + nameOfListP + (n - 1));
		// 			p = model.createProperty(rdf + "rest");
		// 			o = model.createResource(dsp + nameOfListP + n);
		// 			model.add(s, p, o);
		// 			n = n + 1;
		// 		}
		// 	}
		// }

		// if (n > 0) {

		// 	if (n == 1) {
		// 		s = model.createResource(dsp + nameOfListP);
		// 		p = model.createProperty(rdf + "rest");
		// 		o = model.createResource(rdf + "nil");
		// 		model.add(s, p, o);
		// 	} else {
		// 		s = model.createResource(dsp + nameOfListP + (n - 1));
		// 		p = model.createProperty(rdf + "rest");
		// 		o = model.createResource(rdf + "nil");
		// 		model.add(s, p, o);
		// 	}
		// 	s = model.createResource(dsp + nameOfListP);
		// 	p = model.createProperty(rdf + "type");
		// 	o = model.createResource(rdf + "List");
		// 	model.add(s, p, o);
		// }

		// n = 0;

		// for (String vocabulary : listDistinctObjectVocabularies) {
		// 	if (n == 0) {
		// 		s = model.createResource(dsp + nameOfListO);
		// 		p = model.createProperty(rdf + "first");
		// 		v = model.createLiteral(vocabulary.toString());
		// 		model.add(s, p, v);
		// 		n = n + 1;
		// 	} else {
		// 		s = model.createResource(dsp + nameOfListO + n);
		// 		p = model.createProperty(rdf + "first");
		// 		v = model.createLiteral(vocabulary.toString());
		// 		model.add(s, p, v);

		// 		if (n == 1) {
		// 			s = model.createResource(dsp + nameOfListO);
		// 			p = model.createProperty(rdf + "rest");
		// 			o = model.createResource(dsp + nameOfListO + n);
		// 			model.add(s, p, o);
		// 			n = n + 1;
		// 		} else {
		// 			s = model.createResource(dsp + nameOfListO + (n - 1));
		// 			p = model.createProperty(rdf + "rest");
		// 			o = model.createResource(dsp + nameOfListO + n);
		// 			model.add(s, p, o);
		// 			n = n + 1;
		// 		}
		// 	}
		// }

		// if (n > 0) {

		// 	if (n == 1) {
		// 		s = model.createResource(dsp + nameOfListO);
		// 		p = model.createProperty(rdf + "rest");
		// 		o = model.createResource(rdf + "nil");
		// 		model.add(s, p, o);
		// 	} else {
		// 		s = model.createResource(dsp + nameOfListO + (n - 1));
		// 		p = model.createProperty(rdf + "rest");
		// 		o = model.createResource(rdf + "nil");
		// 		model.add(s, p, o);
		// 	}
		// 	s = model.createResource(dsp + nameOfListO);
		// 	p = model.createProperty(rdf + "type");
		// 	o = model.createResource(rdf + "List");
		// 	model.add(s, p, o);
		// }
		
		return ListResourcesSPO;
	}

}