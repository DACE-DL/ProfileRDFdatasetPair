package profiling.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcRatio extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcRatio";
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
		Node totalWoodTradeBiomass = getArg(0, args, context);
		Node totalBiomass = getArg(1, args, context);
		// Verify the typing of the parameters
		if (totalBiomass.isLiteral() && totalWoodTradeBiomass.isLiteral() ) {
			Node ratio = null;
			if (totalBiomass.getLiteralValue() instanceof Number && 
					totalWoodTradeBiomass.getLiteralValue() instanceof Number) {

				Number nTotalBiomass = (Number)totalBiomass.getLiteralValue();
				Number nTotalWoodTradeBiomass = (Number)totalWoodTradeBiomass.getLiteralValue();
				Double dTotalBiomass  = nTotalBiomass.doubleValue();
				Double dTotalWoodTradeBiomass  = nTotalWoodTradeBiomass.doubleValue();
				double dRatio = 0.0;
				// Doing the calculation
				if (dTotalBiomass > 0.0) {
					dRatio = dTotalWoodTradeBiomass/dTotalBiomass;
					// Creating a node for the output parameter
					ratio = Util.makeDoubleNode(dRatio);
				}
			}
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			success = env.bind(args[2], ratio);
		}   
		return success;
	}
}