package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcRMedianVector extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcRMedianVector";
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
			Node median = null;
			if (javaVector.getLiteralValue() instanceof String) {
				if (!javaVector.getLiteralValue().equals(""))  {
					String strJavaVector = (String) javaVector.getLiteralValue();
					// Doing the calculation
					double nMedian = RUtil.Median(strJavaVector);

					// Creating a node for the output parameter
					median = Util.makeDoubleNode(nMedian);
					//System.out.println(median);
					// Binding the output parameter to the node
					BindingEnvironment env = context.getEnv();
					success = env.bind(args[1], median);
				}
			}
		} 
		return success;
	}   
	
}
