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
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcSubclassUsage extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcSubclassUsage";
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
		String prefix = ProfilingConf.queryPrefix;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		Node number = null;
		Integer nNumber = 0;

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Query query = QueryFactory.create(prefix + 
			"SELECT (COUNT(*) AS ?usage) " +
			" WHERE { " +
			" ?s rdfs:subClassOf ?o ." +
				" FILTER isIRI(?o) " +
				" FILTER isIRI(?s) " +
			" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			QuerySolution querySolution = result.next();
			
			nNumber = querySolution.getLiteral("usage").getInt();
			// Creating a node for the output parameter
			number = Util.makeIntNode(nNumber);
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			success = env.bind(args[0], number);
		}

		
		return success;
	}
}