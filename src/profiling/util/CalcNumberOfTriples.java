package profiling.util;

import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcNumberOfTriples extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcNumberOfTriples";
	}

	@Override
	public int getArgLength() {
		return 1 ;
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
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		Node number = null;
		double nNumber = 0;
		Model model = ModelFactory.createModelForGraph(context.getGraph());
	
		nNumber= model.size();
		// Creating a node for the output parameter
		number = Util.makeIntNode((int) nNumber);
		// Binding the output parameter to the node
		BindingEnvironment env = context.getEnv();
		success = env.bind(args[0], number);
		  
		return success;
	}
}