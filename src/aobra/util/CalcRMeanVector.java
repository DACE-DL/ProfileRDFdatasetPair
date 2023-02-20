package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcRMeanVector extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcRMeanVector";
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
		Node javaVector = getArg(0, args, context);

		// Verify the typing of the parameters
		if (javaVector.isLiteral()) {
			Node mean = null;
			if (javaVector.getLiteralValue() instanceof String) {
				if (!javaVector.getLiteralValue().equals(""))  {
					String strJavaVector = (String) javaVector.getLiteralValue();
					// Doing the calculation
					double nMean = RUtil.Mean(strJavaVector);

					// Creating a node for the output parameter
					mean = Util.makeDoubleNode(nMean);
					//System.out.println(mean);
					// Binding the output parameter to the node
					BindingEnvironment env = context.getEnv();
					success = env.bind(args[1], mean);
				}
			}
		} 
		return success;
	}   
	
}
