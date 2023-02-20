package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcCumulativeCrownLengthIndex extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcCumulativeCrownLengthIndex";
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
		Node treesHeightTrunk = getArg(1, args, context);
		Node treesDensity = getArg(2, args, context);
		// Verify the typing of the parameters
		if (treesHeight.isLiteral() && treesHeightTrunk.isLiteral() && treesDensity.isLiteral() ) {
			Node index = null;
			if (treesHeight.getLiteralValue() instanceof Number && 
					treesHeightTrunk.getLiteralValue() instanceof Number && 
					treesDensity.getLiteralValue() instanceof Number) {

				Number nTreesHeight = (Number)treesHeight.getLiteralValue();
				Number nTreesHeightTrunk = (Number)treesHeightTrunk.getLiteralValue();
				Number nTreesDensity = (Number)treesDensity.getLiteralValue();
				Double dTreesHeight  = nTreesHeight.doubleValue();
				Double dTreesHeightTrunk  = nTreesHeightTrunk.doubleValue();
				Double dTreesDensity  = nTreesDensity.doubleValue();
				double dIndex = 0.0;
				// Doing the calculation
				if (dTreesHeightTrunk > 0.0 && dTreesDensity > 0.0) {
					dIndex = ((dTreesHeight/100) - (dTreesHeightTrunk/100)) * (dTreesDensity / 500);
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