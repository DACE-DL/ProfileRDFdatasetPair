package profiling.util;

import java.util.ArrayList;
import java.util.List;

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

public class CalcMakeListObjectVocabularies extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcMakeListObjectVocabularies";
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
		String nameOfList = dsp + "listOfObjectVocabularies";
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
		List<String> listDistinctObjectVocabularies = new ArrayList<>();
	
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
			stmtIte1.forEach((stm) -> {
				if (stm.getObject().isResource()) {
					if (!stm.getObject().isAnon()) {
						// System.out.println("NS : " + stm.getObject().getNameSpace());
						// Duplicate checking
						if (!listDistinctObjectVocabularies.contains(stm.getObject().asResource().getNameSpace())) { 
							listDistinctObjectVocabularies.add(stm.getObject().asResource().getNameSpace());
						}
					}			
				} 
			});
			//System.out.println("property : " + property.getURI() );	
			//System.out.println("liste : " + listDistinctObjectVocabularies.size() );	

        });

		// System.out.println("OK liste");

		for (String vocabulary : listDistinctObjectVocabularies) {
			if (n == 0) {
				s = NodeFactory.createURI(nameOfList);
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createLiteral(vocabulary);
				context.add(Triple.create(s, p, o));
				n = n + 1;
			} else {
				s = NodeFactory.createURI(nameOfList + n);
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createLiteral(vocabulary);
				context.add(Triple.create(s, p, o));
				if (n == 1) {
					s = NodeFactory.createURI(nameOfList);
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(nameOfList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				} else {
					s = NodeFactory.createURI(nameOfList + (n - 1));
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(nameOfList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				}
			}
			if (n > 500){
				break;
			}

		}

		if (n > 0) {

			if (n == 1) {
				s = NodeFactory.createURI(nameOfList);
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			} else {
				s = NodeFactory.createURI(nameOfList + (n - 1));
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			}
			s = NodeFactory.createURI(nameOfList);
			p = NodeFactory.createURI(rdf + "type");
			o = NodeFactory.createURI(rdf + "List");
			context.add(Triple.create(s, p, o));
			success = true;
		}
		return success;
	}
}