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

public class CalcMakeListClassOfInterest extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcMakeListClassOfInterest";
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
		String nameOfList = "listURIofClassOfInterest";
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
		Integer n = 0;
		Node s = NodeFactory.createURI(dsp + "sujet");
		Node p = NodeFactory.createURI(dsp + "predicat");
		Node o = NodeFactory.createURI(dsp + "objet");
		Node b = NodeFactory.createBlankNode();
		Node u1 = NodeFactory.createURI(dsp + "class1");
		Node u2 = NodeFactory.createURI(dsp + "class2");
		Node pu1 = NodeFactory.createURI(dsp + "asClass1");
		Node pu2 = NodeFactory.createURI(dsp + "asClass2");

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Query query = QueryFactory.create(prefix + 
				"SELECT DISTINCT (?elementClass1 AS ?class1) (?elementClass2 AS ?class2)" +
				" WHERE { " +
				"dsp:listURIofClassDefined rdf:rest*/rdf:first ?elementClass1 ." +
				"dsp:listURIofClassDefined rdf:rest*/rdf:first ?elementClass2 ." +
				"FILTER EXISTS {?s1 rdf:type ?elementClass1 .?s2 rdf:type ?elementClass2 .?s1 ?p ?s2 } " +
				//"FILTER  (?elementClass1 = <http://dbkwik.webdatacommons.org/marvelcinematicuniverse.wikia.com/class/armor> || ?elementClass2 = <http://dbkwik.webdatacommons.org/marvelcinematicuniverse.wikia.com/class/armor>) " +
				" } GROUP BY ?elementClass1 ?elementClass2" );	
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				// System.out.println(querySolution.getResource("class1").toString());
				// System.out.println(querySolution.getResource("class2").toString());
				ListResources.add(new UriAndUri(querySolution.getResource("class1").toString(), querySolution.getResource("class2").toString())) ;
			}
		}

		try {
			ProfilingUtil.makeJsonUriAndUriFile(ListResources, nameOfList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("OK liste");

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