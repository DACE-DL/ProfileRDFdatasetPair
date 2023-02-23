package profiling.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcTreeDensity extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcTreeDensity";
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

		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node distanceBetweenLines = getArg(0, args, context);
		Node distanceBetweenTrees = getArg(1, args, context);
		// Verify the typing of the parameters
		if (distanceBetweenLines.isLiteral() && distanceBetweenTrees.isLiteral() ) {
			Node density = null;
			if (distanceBetweenLines.getLiteralValue() instanceof Number && 
					distanceBetweenTrees.getLiteralValue() instanceof Number) {

				Number nDistanceBetweenLines = (Number)distanceBetweenLines.getLiteralValue();
				Number nDistanceBetweenTrees = (Number)distanceBetweenTrees.getLiteralValue();
				Double dDistanceBetweenLines  = nDistanceBetweenLines.doubleValue();
				Double dDistanceBetweenTrees  = nDistanceBetweenTrees.doubleValue();
				double dDensity = 0.0;
				// Doing the calculation
				if (dDistanceBetweenLines > 0.0 && dDistanceBetweenTrees > 0.0) {
					dDensity = 10000/dDistanceBetweenLines/dDistanceBetweenTrees;
					// Creating a node for the output parameter
					density = Util.makeDoubleNode(dDensity);
				}
			}
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			success = env.bind(args[2], density);
		}   
		return success;
	}
}