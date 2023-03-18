package profiling.util;

import java.util.ArrayList;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcMakeListClassAndSubClass extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcMakeListClassAndSubClass";
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
		String prefix = ProfilingConf.queryPrefix;
		String nameOfList = "listURIsOfClassAndSubclass";
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
	
		Integer n = 0;
		Node s = NodeFactory.createURI(dsp + "sujet");
		Node p = NodeFactory.createURI(dsp + "predicat");
		Node o = NodeFactory.createURI(dsp + "objet");
		Node b = NodeFactory.createBlankNode();
		Node u1 = NodeFactory.createURI(dsp + "class");
		Node u2 = NodeFactory.createURI(dsp + "subclass");
		Node pu1 = NodeFactory.createURI(dsp + "asClass");
		Node pu2 = NodeFactory.createURI(dsp + "asSubclass");

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Query query = QueryFactory.create(prefix + 
				"SELECT DISTINCT (?o AS ?class) (?s AS ?subclass) " +
				" WHERE { " +
				" ?s rdfs:subClassOf ?o ." +
				" FILTER isIRI(?o) " +
				" FILTER isIRI(?s) " +
				" } ORDER BY ?s ?o "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new UriAndUri(querySolution.getResource("class").toString(), querySolution.getResource("subclass").toString())) ;
			}
		}

		for (UriAndUri resource : ListResources) {
			if (n == 0) {
				s = NodeFactory.createURI(dsp + nameOfList);
				p = NodeFactory.createURI(rdf + "first");
				
				b = NodeFactory.createBlankNode();
				u1 = NodeFactory.createURI(resource.getUri1());
				u2 = NodeFactory.createURI(resource.getUri2());
				context.add(Triple.create(b, pu1, u1));
				context.add(Triple.create(b, pu2, u2));

				context.add(Triple.create(s, p, b));
				n = n + 1;
			} else {
				s = NodeFactory.createURI(dsp + nameOfList + n);
				p = NodeFactory.createURI(rdf + "first");
				
				b = NodeFactory.createBlankNode();
				u1 = NodeFactory.createURI(resource.getUri1());
				u2 = NodeFactory.createURI(resource.getUri2());
				context.add(Triple.create(b, pu1, u1));
				context.add(Triple.create(b, pu2, u2));

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
		return success;
	}
}