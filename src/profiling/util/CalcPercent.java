package profiling.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcPercent extends BaseBuiltin {
  
  @Override
  public String getName() {
    return "calcPercent";
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
    Node number1 = getArg(0, args, context);
    Node number2 = getArg(1, args, context);
   
    // Verify the typing of the parameters
    if (number1.isLiteral() && number2.isLiteral() ) {
    	Node percent = null;
    	if (number1.getLiteralValue() instanceof Number && 
    			number2.getLiteralValue() instanceof Number) {

    		Number nvNumber1 = (Number)number1.getLiteralValue();
    		Number nvNumber2 = (Number)number2.getLiteralValue();
    		// Doing the calculation
    		if (nvNumber2.doubleValue() > 0.0) {
    			double nPercent = (nvNumber1.doubleValue() / nvNumber2.doubleValue())*100;
    			// Creating a node for the output parameter
    			percent = Util.makeDoubleNode(nPercent);
    		}
    		// Binding the output parameter to the node
    		BindingEnvironment env = context.getEnv();
    		success = env.bind(args[2], percent);
    	} 
    }   
    return success;
  }
}