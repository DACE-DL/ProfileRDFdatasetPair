package profiling.util;

import org.apache.jena.graph.Node;
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

public class CalcDistanceBetween2Elements extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcDistanceBetween2Elements";
	}

	@Override
	public int getArgLength() {
		return 3;
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
		String prefix = ProfilingConf.queryPrefix;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		Node node2 = getArg(1, args, context);
		// Verify the typing of the parameters

		if (node1.isURI() & node2.isURI()) {
			Double distance = 0.0 ;
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			// distance = (double) model.size() ;
			Query query = QueryFactory.create(prefix + "SELECT ?distance " + 
					"WHERE {" + 
					"<" + node1.getURI() + ">" + " geo:hasDefaultGeometry ?geom1 ." +
					" ?geom1 geo:hasSerialization ?geom1Lit ." + 
					"<" + node2.getURI() + ">" + " geo:hasDefaultGeometry ?geom2 ." +
					" ?geom2 geo:hasSerialization ?geom2Lit ." + 
					" BIND((geof:distance(?geom1Lit, ?geom2Lit, uom:metre)) as ?distance) ." +
					"} ");
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			// ResultSetFormatter.out(System.out, results);
			if (result.hasNext()) {
				QuerySolution querySolution = result.next() ;
				distance = querySolution.getLiteral("distance").getDouble() ;
			}
			// Creating a node for the output parameter
			Node distanceNode = Util.makeDoubleNode(distance);
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			success = env.bind(args[2], distanceNode);
			// System.out.println("Ok");
			qe.close();
		}   
		return success;
	}
}