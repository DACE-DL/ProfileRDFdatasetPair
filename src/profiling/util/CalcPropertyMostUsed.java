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
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcPropertyMostUsed extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcPropertyMostUsed";
	}

	@Override
	public int getArgLength() {
		return 2;
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
		String nameOfList = "listPropertyMostUsed";
		// Check we received the correct number of parameters
		checkArgs(length, context);
		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		
		boolean success = false;

		Integer n = 0;
		Node s = NodeFactory.createURI(dsp + "sujet");
		Node p = NodeFactory.createURI(dsp + "predicat");
		Node o = NodeFactory.createURI(dsp + "objet");
		Node b = NodeFactory.createBlankNode();
		Node u = NodeFactory.createURI(dsp + "uri");
		Node v = Util.makeIntNode(0);
		Node pu = NodeFactory.createURI(dsp + "asURI");
		Node pv = NodeFactory.createURI(dsp + "asValue");

		ArrayList<UriAndNumber> listProperty = new ArrayList<UriAndNumber>();
		//System.out.println("OK calcPropertyMostUsed");
		if (node1.isLiteral()) {
			if (node1.getLiteralValue() instanceof Number) {
				Number quantile = (Number)node1.getLiteralValue();
				Double dQuantile  = quantile.doubleValue();
				//System.out.println("Quantile : " + dQuantile);
				Model model = ModelFactory.createModelForGraph(context.getGraph());
				Query query = QueryFactory.create(prefix + 
							"SELECT (?uri AS ?property) (?val AS ?usage) WHERE { dsp:listPropertyUsageCount rdf:rest*/rdf:first ?element ." +
							" ?element dsp:asURI ?uri ." +
							" ?element dsp:asValue ?val ." +
							"FILTER (?val > " + dQuantile + ") " +
							" } ORDER BY DESC (?usage)"
				);			
				QueryExecution qe = QueryExecutionFactory.create(query, model);		
					ResultSet result = qe.execSelect();
					if (result.hasNext()) {
						while( result.hasNext() ) {
							QuerySolution querySolution = result.next() ;
							listProperty.add(new UriAndNumber(querySolution.getResource("property").toString(), querySolution.getLiteral("usage").getInt())) ;
						}
					}
					try {
						ProfilingUtil.makeJsonUriAndNumberFile(listProperty, nameOfList + ".json");
					} catch (Exception e) {
						e.printStackTrace();
					}
					// System.out.println("OK liste");
					
					for (UriAndNumber resource : listProperty) {
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
					Node number = null;
					number = Util.makeIntNode((int) n);
					// Binding the output parameter to the node
					BindingEnvironment env = context.getEnv();
					success = env.bind(args[1], number);				
			}	
		}
		return success;
	}
}