package profiling.util;

import java.util.Iterator;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcTotalTreesBiomass extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcTotalTreesBiomass";
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

		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node = getArg(0, args, context);
		// Verify the typing of the parameters
		if (node.isBlank()) {
			Double ntotalBiomass = 0.0 ;
			Iterator<Triple> it = context.find(node, (Node) null, (Node) null);
			if (it.hasNext()) {
				Number number = null;
				while( it.hasNext() ) {
					Triple triple = it.next();
					if (triple.getObject().getLiteralValue() instanceof Number) { 
						number = (Number)triple.getObject().getLiteralValue();
						ntotalBiomass = ntotalBiomass + number.doubleValue();
					}
				}

				// Creating a node for the output parameter
				Node totalBiomass = Util.makeDoubleNode(ntotalBiomass);;
				// Binding the output parameter to the node
				BindingEnvironment env = context.getEnv();
				success = env.bind(args[1], totalBiomass);
				// System.out.println("Ok");
			}
		}   
		return success;
	}
}