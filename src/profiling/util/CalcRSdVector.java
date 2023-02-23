package profiling.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcRSdVector extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcRSdVector";
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
			Node sd = null;
			if (javaVector.getLiteralValue() instanceof String) {
				if (!javaVector.getLiteralValue().equals(""))  {
					String strJavaVector = (String) javaVector.getLiteralValue();
					// Doing the calculation
					double nSd = RUtil.Sd(strJavaVector);

					// Creating a node for the output parameter
					sd = Util.makeDoubleNode(nSd);
					//System.out.println(sd);
					// Binding the output parameter to the node
					BindingEnvironment env = context.getEnv();
					success = env.bind(args[1], sd);
				}
			}
		} 
		return success;
	}   
	
}
