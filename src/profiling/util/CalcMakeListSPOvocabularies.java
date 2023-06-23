package profiling.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.java.util.jar.Pack200;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcMakeListSPOvocabularies extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcMakeListSPOvocabularies";
	}

	@Override
	public int getArgLength() {
		return 0;
	}

	@Override
	public void headAction(Node[] args, int length, RuleContext context) {
		doUserRequiredAction(args, length, context);
	}  

	@Override
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		return doUserRequiredAction(args, length, context);
	}

	private boolean doUserRequiredAction(Node[] args, int length, RuleContext context) {
		new ProfilingConf();
		String dsp = ProfilingConf.dsp;
		String rdf = ProfilingConf.rdf;
		String nameOfSubjectList = dsp + "listOfSubjectVocabularies";
		String nameOfPredicatList = dsp + "listOfPredicatVocabularies";
		String nameOfObjectList = dsp + "listOfObjectVocabularies";
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		Integer n = 0;
		Node s = NodeFactory.createURI(dsp + "sujet");
		Node p = NodeFactory.createURI(dsp + "predicat");
		Node o = NodeFactory.createURI(dsp + "objet");

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Resource s1 = null;
		Property p1 = null;
		Resource o1 = null;
		List<Resource> listProperty = new ArrayList<>();
		List<String> listDistinctSubjectVocabularies = new ArrayList<>();
		List<String> listDistinctPredicatVocabularies = new ArrayList<>();
		List<String> listDistinctObjectVocabularies = new ArrayList<>();
	
		p1 = model.createProperty(rdf ,"type");
		o1 = model.createResource(rdf + "Property");

		Selector selector = new SimpleSelector(s1, p1, o1) ;
		StmtIterator stmtIte= model.listStatements(selector);

		stmtIte.forEach((statement) -> {
            listProperty.add(statement.getSubject());
        });

		// On boucle sur les propriétés
		listProperty.forEach((property) -> {
			Resource p2 = model.createProperty(property.getURI());
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
		
		for (String vocabulary : listDistinctSubjectVocabularies) {
			if (n == 0) {
				s = NodeFactory.createURI(nameOfSubjectList);
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createLiteral(vocabulary);
				context.add(Triple.create(s, p, o));
				n = n + 1;
			} else {
				s = NodeFactory.createURI(nameOfSubjectList + n);
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createLiteral(vocabulary);
				context.add(Triple.create(s, p, o));
				if (n == 1) {
					s = NodeFactory.createURI(nameOfSubjectList);
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(nameOfSubjectList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				} else {
					s = NodeFactory.createURI(nameOfSubjectList + (n - 1));
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(nameOfSubjectList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				}
			}
		}

		if (n > 0) {

			if (n == 1) {
				s = NodeFactory.createURI(nameOfSubjectList);
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			} else {
				s = NodeFactory.createURI(nameOfSubjectList + (n - 1));
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			}
			s = NodeFactory.createURI(nameOfSubjectList);
			p = NodeFactory.createURI(rdf + "type");
			o = NodeFactory.createURI(rdf + "List");
			context.add(Triple.create(s, p, o));
			success = true;
		}
		n = 0;
		for (String vocabulary : listDistinctPredicatVocabularies) {
			if (n == 0) {
				s = NodeFactory.createURI(nameOfPredicatList);
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createLiteral(vocabulary);
				context.add(Triple.create(s, p, o));
				n = n + 1;
			} else {
				s = NodeFactory.createURI(nameOfPredicatList + n);
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createLiteral(vocabulary);
				context.add(Triple.create(s, p, o));
				if (n == 1) {
					s = NodeFactory.createURI(nameOfPredicatList);
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(nameOfPredicatList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				} else {
					s = NodeFactory.createURI(nameOfPredicatList + (n - 1));
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(nameOfPredicatList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				}
			}
		}

		if (n > 0) {

			if (n == 1) {
				s = NodeFactory.createURI(nameOfPredicatList);
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			} else {
				s = NodeFactory.createURI(nameOfPredicatList + (n - 1));
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			}
			s = NodeFactory.createURI(nameOfPredicatList);
			p = NodeFactory.createURI(rdf + "type");
			o = NodeFactory.createURI(rdf + "List");
			context.add(Triple.create(s, p, o));
			success = true;
		}
		n = 0;
		for (String vocabulary : listDistinctObjectVocabularies) {
			if (n == 0) {
				s = NodeFactory.createURI(nameOfObjectList);
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createLiteral(vocabulary);
				context.add(Triple.create(s, p, o));
				n = n + 1;
			} else {
				s = NodeFactory.createURI(nameOfObjectList + n);
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createLiteral(vocabulary);
				context.add(Triple.create(s, p, o));
				if (n == 1) {
					s = NodeFactory.createURI(nameOfObjectList);
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(nameOfObjectList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				} else {
					s = NodeFactory.createURI(nameOfObjectList + (n - 1));
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(nameOfObjectList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				}
			}
		}

		if (n > 0) {

			if (n == 1) {
				s = NodeFactory.createURI(nameOfObjectList);
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			} else {
				s = NodeFactory.createURI(nameOfObjectList + (n - 1));
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			}
			s = NodeFactory.createURI(nameOfObjectList);
			p = NodeFactory.createURI(rdf + "type");
			o = NodeFactory.createURI(rdf + "List");
			context.add(Triple.create(s, p, o));
			success = true;
		}
		return success;
	}
}