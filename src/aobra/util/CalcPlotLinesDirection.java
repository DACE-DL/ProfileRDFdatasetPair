package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcPlotLinesDirection extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcPlotLinesDirection";
	}

	@Override
	public int getArgLength() {
		return 2 ;
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
		new AobraConf();
		String prefix = AobraConf.queryPrefix;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		// Verify the typing of the parameters
		//System.out.println(node3);
		//System.out.println(node3.getLiteralDatatype());
		//System.out.println(node1);
		if (node1.isURI()) {
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			//System.out.println(node1);
			Query query = QueryFactory.create(prefix + 
			"SELECT ?cardinalDirection (COUNT(?cardinalDirection) AS ?number)" +
			" WHERE { " +
			" ?line dcterms:type res:term_Tree_Line ." +
			" ?line bfo:partOf " + "<" + node1.getURI() + "> ." +
			" ?line res:hasCardinalDirection ?cardinalDirection ." +
	        " } GROUP BY ?cardinalDirection " +
			" ORDER BY DESC(?number)");
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			//ResultSetFormatter.out(System.out, result);
			Resource resourceCardinalDirection = null;
			if (result.hasNext()) {
				QuerySolution querySolution = result.next() ;
				resourceCardinalDirection = querySolution.getResource("cardinalDirection");
				// Binding the output parameter to the node
				Node cardinalDirection = resourceCardinalDirection.asNode();
				BindingEnvironment env = context.getEnv();
				success = env.bind(args[1], cardinalDirection);
			}
			qe.close();

		}
		return success;
	}
}