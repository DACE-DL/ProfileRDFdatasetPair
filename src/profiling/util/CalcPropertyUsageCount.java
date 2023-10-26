package profiling.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcPropertyUsageCount extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcPropertyUsageCount";
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
		String nameOfList = "listPropertyUsageCount";
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		ArrayList<UriAndNumber> ListResources = new ArrayList<UriAndNumber>();
	
		Integer n = 0;
		Node s = NodeFactory.createURI(dsp + "sujet");
		Node p = NodeFactory.createURI(dsp + "predicat");
		Node o = NodeFactory.createURI(dsp + "objet");
		Node b = NodeFactory.createBlankNode();
		Node u = NodeFactory.createURI(dsp + "uri");
		Node v = Util.makeIntNode(0);
		Node pu = NodeFactory.createURI(dsp + "asURI");
		Node pv = NodeFactory.createURI(dsp + "asValue");

		Resource s1 = null;
		Property p1 = null;
		Resource o1 = null;

		Instant start0 = Instant.now();

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		p1 = model.createProperty(rdf ,"type");
		o1 = model.createResource(rdf + "Property");

		Selector selector = new SimpleSelector(s1, p1, o1) ;
		StmtIterator stmtIte= model.listStatements(selector);
		Instant end = Instant.now();
		System.out.println("Running time extraction properties : " + Duration.between(start0, end).toMillis() + " millisecondes");
		stmtIte.forEach((statement) -> {
			//Instant endTemp1 = Instant.now();
			Resource s2 = null;
			Property p2 = model.createProperty(statement.getSubject().getURI());
			Resource o2 = null;
			Selector selector2 = new SimpleSelector(s2, p2, o2) ;
			StmtIterator stmtIte2 = model.listStatements(selector2);
			List<Statement> listStmtIte2 = stmtIte2.toList();
			ListResources.add(new UriAndNumber(statement.getSubject().getURI(), listStmtIte2.size() )) ;
			stmtIte2.close();
			//Instant endTemp2 = Instant.now();
			//System.out.println("Running time extraction usage for " + statement.getSubject().getURI() + " : "+ Duration.between(endTemp1, endTemp2).toMillis() + " millisecondes");
        });
		stmtIte.close();
		end = Instant.now();
		System.out.println("Running time extraction properties usage : " + Duration.between(start0, end).getSeconds() + " secondes");

		Instant end0 = Instant.now();
		System.out.println("Running time extraction : " + Duration.between(start0, end0).getSeconds() + " secondes");
		
		Collections.sort(ListResources, new UriAndNumberComparator());

		Instant end1 = Instant.now();
		System.out.println("Running time sort : " + Duration.between(end0, end1).toMillis() + " millisecondes");

		try {
			ProfilingUtil.makeJsonUriAndNumberFile(ListResources, nameOfList + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Instant end2 = Instant.now();
		System.out.println("Running time make json file : " + Duration.between(end1, end2).toMillis() + " millisecondes");

		for (UriAndNumber resource : ListResources) {
			if (n == 0) {
				s = NodeFactory.createURI(dsp + nameOfList);
				p = NodeFactory.createURI(rdf + "first");
				
				b = NodeFactory.createBlankNode();
				u = NodeFactory.createURI(resource.getUri());
				v = Util.makeIntNode(resource.getNumber());
				context.add(Triple.create(b, pu, u));
				context.add(Triple.create(b, pv, v));

				context.add(Triple.create(s, p, b));
				n = n + 1;
			} else {
				s = NodeFactory.createURI(dsp + nameOfList + n);
				p = NodeFactory.createURI(rdf + "first");
				
				b = NodeFactory.createBlankNode();
				u = NodeFactory.createURI(resource.getUri());
				v = Util.makeIntNode(resource.getNumber());
				context.add(Triple.create(b, pu, u));
				context.add(Triple.create(b, pv, v));

				context.add(Triple.create(s, p, b));
				if (n == 1) {
					s = NodeFactory.createURI(dsp + nameOfList);
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(dsp + nameOfList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				} else {
					s = NodeFactory.createURI(dsp + nameOfList + (n - 1));
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(dsp + nameOfList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				}
			}
		}

		if (n > 0) {

			if (n == 1) {
				s = NodeFactory.createURI(dsp + nameOfList);
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			} else {
				s = NodeFactory.createURI(dsp + nameOfList + (n - 1));
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			}
			s = NodeFactory.createURI(dsp + nameOfList);
			p = NodeFactory.createURI(rdf + "type");
			o = NodeFactory.createURI(rdf + "List");
			context.add(Triple.create(s, p, o));
			success = true;
		}
		Instant end3 = Instant.now();
		System.out.println("Running time make RDF list : " + Duration.between(end2, end3).toMillis() + " millisecondes");
		return success;
	}

	class UriAndNumberComparator implements java.util.Comparator<UriAndNumber> {
		@Override
		public int compare(UriAndNumber a, UriAndNumber b) {
			return b.getNumber() - a.getNumber();
		}
	}
}