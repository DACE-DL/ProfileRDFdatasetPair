package profiling.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcTreeBasalArea extends BaseBuiltin {
  
  @Override
  public String getName() {
    return "calcTreeBasalArea";
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
    Node circumference = getArg(0, args, context);
   
    // Verify the typing of the parameters
    if (circumference.isLiteral()) {
      Node basalArea = null;
      if (circumference.getLiteralValue() instanceof Number) {
        
        Number nvCircumference = (Number)circumference.getLiteralValue();
        // Doing the calculation
        double nCircumference = (Math.pow((nvCircumference.doubleValue()/100), 2.0) / (4 * Math.PI)) ;
        // Creating a node for the output parameter
        basalArea = Util.makeDoubleNode(nCircumference);
        //System.out.println("Basal area :" + basalArea);
        // Binding the output parameter to the node
        BindingEnvironment env = context.getEnv();
        success = env.bind(args[1], basalArea);
      } 
    }   
    return success;
  }
}