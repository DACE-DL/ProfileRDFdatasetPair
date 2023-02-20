package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcTestSPARQL extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcTestSPARQL";
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
		new AobraConf();
		String prefix = AobraConf.queryPrefix;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		Node node2 = getArg(1, args, context);
		// Verify the typing of the parameters

		if (node1.isBlank() & node2.isBlank()) {
			Double distance = 0.0 ;
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			distance = (double) model.size() ;
			Query query = QueryFactory.create(prefix + "SELECT (?plot AS ?Parcelle) (?tree AS ?Arbre) " +
					"WHERE {" + " ?plot res:isAFPlot ?true ." +
					" ?tree rdf:type afy:StructuralElement ." +
					" ?tree dcterms:type <http://aims.fao.org/aos/agrovoc/c_7887> ." +
					" ?tree geo:sfWithin ?plot ." +
					"}  ORDER by ?plot");
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet results = qe.execSelect();
			ResultSetFormatter.out(System.out, results);
			// Creating a node for the output parameter
			Node distanceNode = Util.makeDoubleNode(distance);
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			success = env.bind(args[2], distanceNode);
			System.out.println("Ok");

		}   
		return success;
	}
}