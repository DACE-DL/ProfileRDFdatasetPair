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
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcClassUsageCount extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcClassUsageCount";
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
		String nameOfList = "listClassUsageCount";
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

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Query query = QueryFactory.create(prefix + 
				"SELECT DISTINCT (?o AS ?class) (COUNT(?s) AS ?usage) " +
				" WHERE { " +
				" ?s rdf:type ?o ." +
				" FILTER isIRI(?o) " +
				" } GROUP BY ?o ORDER BY DESC (?usage)"
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new UriAndNumber(querySolution.getResource("class").toString(), querySolution.getLiteral("usage").getInt())) ;
			}
		}

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
		return success;
	}
}