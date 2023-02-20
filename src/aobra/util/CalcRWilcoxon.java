package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcRWilcoxon extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcRWilcoxon";
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
		Node javaVector1 = getArg(0, args, context);
		Node javaVector2 = getArg(1, args, context);
		// Verify the typing of the parameters
		if (javaVector1.isLiteral() && javaVector2.isLiteral()) {
			Node pValue = null;
			if (javaVector1.getLiteralValue() instanceof String && javaVector2.getLiteralValue() instanceof String) {
				if (!javaVector1.getLiteralValue().equals("") && !javaVector2.getLiteralValue().equals(""))  {
					//System.out.println(javaVector1.getLiteralValue());
					String strjavaVector1 = (String) javaVector1.getLiteralValue();
					String strjavaVector2 = (String) javaVector2.getLiteralValue();
					//System.out.println(strjavaVector1);
					// Doing the calculation
					Double nPValue = RUtil.Wilcoxon(strjavaVector1, strjavaVector2);
					//System.out.println(nPValue);
					// Creating a node for the output parameter
					pValue = Util.makeDoubleNode(nPValue);
					//System.out.println(pValue);
					// Binding the output parameter to the node
					BindingEnvironment env = context.getEnv();
					success = env.bind(args[2], pValue);
				}
			}
		} 
		return success;
	}   
	
}
