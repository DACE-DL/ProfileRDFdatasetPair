package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcCultivatedLaneOpennessIndex extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcCultivatedLaneOpennessIndex";
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
				Node treesHeight = getArg(0, args, context);		
				Node distanceBetweenLines = getArg(1, args, context);
				
				// Verify the typing of the parameters
				if (distanceBetweenLines.isLiteral() && treesHeight.isLiteral() ) {
					Node index = null;
					if (distanceBetweenLines.getLiteralValue() instanceof Number && 
							treesHeight.getLiteralValue() instanceof Number) {

						Number nDistanceBetweenLines = (Number)distanceBetweenLines.getLiteralValue();
						Number nTreesHeight = (Number)treesHeight.getLiteralValue();
						Double dDistanceBetweenLines  = nDistanceBetweenLines.doubleValue();
						Double dTreesHeight  = nTreesHeight.doubleValue();
						double dIndex = 0.0;
						// Doing the calculation
						if (dDistanceBetweenLines > 0.0) {
							dIndex = (dTreesHeight/100)/dDistanceBetweenLines;
							// Creating a node for the output parameter
							index = Util.makeDoubleNode(dIndex);
						}
					}
					// Binding the output parameter to the node
					BindingEnvironment env = context.getEnv();
					success = env.bind(args[2], index);
				}   
				return success;
	}
}