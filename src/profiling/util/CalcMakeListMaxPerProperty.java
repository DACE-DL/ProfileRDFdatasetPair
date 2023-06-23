package profiling.util;

import java.util.ArrayList;
import java.util.List;

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
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcMakeListMaxPerProperty extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcMakeListMaxPerProperty";
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
		String xsd = ProfilingConf.xsd;
		String rdf = ProfilingConf.rdf;
		String prefix = ProfilingConf.queryPrefix;
		String nameOfList = dsp + "listOfMaxPerProperty";
		ArrayList<UriAndStringAndBigNumber> ListResources = new ArrayList<UriAndStringAndBigNumber>();
		Node b = NodeFactory.createBlankNode();
		Node u = NodeFactory.createURI(dsp + "uri");
		Node st = NodeFactory.createLiteral("str");
		Node v = Util.makeIntNode(0);
		Node pu = NodeFactory.createURI(dsp + "asURI");
		Node ps = NodeFactory.createURI(dsp + "asStr");
		Node pv = NodeFactory.createURI(dsp + "asValue");

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
	
		p1 = model.createProperty(rdf ,"type");
		o1 = model.createResource(rdf + "Property");

		Selector selector = new SimpleSelector(s1, p1, o1) ;
		StmtIterator stmtIte= model.listStatements(selector);
		
		stmtIte.forEach((statement) -> {
            listProperty.add(statement.getSubject());
        });

		listProperty.forEach((property) -> {
			//System.out.println("Property : " + property.getURI());
			Query query = QueryFactory.create(prefix +
					"SELECT ?datatype (Max(?o) AS ?usage) " +
					" WHERE { " +
					" ?s <" + property.getURI() + "> ?o ." +
					" FILTER ( datatype(?o) = xsd:integer || datatype(?o) = xsd:float || datatype(?o) = xsd:double || datatype(?o) = xsd:decimal || datatype(?o) = xsd:dateTime ) " +
					" } GROUP BY (datatype(?o) AS ?datatype) ORDER BY DESC (?usage)");
			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet result = qe.execSelect();
			if (result.hasNext()) {
				while (result.hasNext()) {
					QuerySolution querySolution = result.next();
					//System.out.println("datatype : " + querySolution.get("datatype"));
					//System.out.println("usage : " + querySolution.getLiteral("usage"));
					if (querySolution.getLiteral("usage") != null) {
						ListResources.add(new UriAndStringAndBigNumber(property.getURI().toString(), querySolution.get("datatype").toString(), querySolution.getLiteral("usage").getLong()));
					}
				}
			}
        });

		// System.out.println("OK liste");

		for (UriAndStringAndBigNumber resource : ListResources) {
			if (n == 0) {
				s = NodeFactory.createURI(nameOfList);
				p = NodeFactory.createURI(rdf + "first");
				
				b = NodeFactory.createBlankNode();
				u = NodeFactory.createURI(resource.getUri());
				st = NodeFactory.createLiteral(resource.getStr());
				v = Util.makeDoubleNode(resource.getNumber());
				
				context.add(Triple.create(b, pu, u));
				context.add(Triple.create(b, ps, st));
				context.add(Triple.create(b, pv, v));

				context.add(Triple.create(s, p, b));
				n = n + 1;
			} else {
				s = NodeFactory.createURI(nameOfList + n);
				p = NodeFactory.createURI(rdf + "first");
				
				b = NodeFactory.createBlankNode();
				u = NodeFactory.createURI(resource.getUri());
				st = NodeFactory.createLiteral(resource.getStr());
				v = Util.makeDoubleNode(resource.getNumber());
				
				context.add(Triple.create(b, pu, u));
				context.add(Triple.create(b, ps, st));
				context.add(Triple.create(b, pv, v));

				context.add(Triple.create(s, p, b));
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