package profiling.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcTreeDensityIndex extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcTreeDensityIndex";
	}

	@Override
	public int getArgLength() {
		return 4;
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
				Node treesHeight = getArg(0, args, context);		
				Node distanceBetweenLines = getArg(1, args, context);
				Node distanceBetweenTrees = getArg(2, args, context);
				// Verify the typing of the parameters
				if (treesHeight.isLiteral() && distanceBetweenLines.isLiteral() && distanceBetweenTrees.isLiteral() ) {
					Node index = null;
					if (treesHeight.getLiteralValue() instanceof Number && 
							distanceBetweenLines.getLiteralValue() instanceof Number && 
							distanceBetweenTrees.getLiteralValue() instanceof Number) {

						Number nTreesHeight = (Number)treesHeight.getLiteralValue();
						Number nDistanceBetweenLines = (Number)distanceBetweenLines.getLiteralValue();
						Number nDistanceBetweenTrees = (Number)distanceBetweenTrees.getLiteralValue();
						Double dTreesHeight  = nTreesHeight.doubleValue();
						Double dDistanceBetweenLines  = nDistanceBetweenLines.doubleValue();
						Double dDistanceBetweenTrees  = nDistanceBetweenTrees.doubleValue();
						double dIndex = 0.0;
						// Doing the calculation
						if (dDistanceBetweenLines > 0.0 && dDistanceBetweenTrees > 0.0) {
							dIndex = Math.pow((dTreesHeight/100), 2)/(dDistanceBetweenLines*dDistanceBetweenTrees);
							// Creating a node for the output parameter
							index = Util.makeDoubleNode(dIndex);
						}
					}
					// Binding the output parameter to the node
					BindingEnvironment env = context.getEnv();
					success = env.bind(args[3], index);
				}   
				return success;
	}
}