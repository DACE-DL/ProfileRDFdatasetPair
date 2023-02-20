package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcGetLocalName extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcGetLocalName";
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
		Node nodeProperty = getArg(0, args, context);

		// Verify the typing of the parameters
		if (nodeProperty.isURI()) {
			Node localName = null;
			
				

					// Creating a node for the output parameter
					//localName = Util.nodeProperty.getLocalName();
					System.out.println(localName);
					// Binding the output parameter to the node
					BindingEnvironment env = context.getEnv();
					success = env.bind(args[1], localName);
				
			
		} 
		return success;
	}   
	
}
