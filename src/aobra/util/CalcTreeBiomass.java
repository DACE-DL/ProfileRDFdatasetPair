package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcTreeBiomass extends BaseBuiltin {
  
  @Override
  public String getName() {
    return "calcTreeBiomass";
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
    Node diameter = getArg(0, args, context);
    Node height = getArg(1, args, context);
   
    // Verify the typing of the parameters
    if (diameter.isLiteral() && height.isLiteral() ) {
      Node biomass = null;
      if (diameter.getLiteralValue() instanceof Number && 
    		  height.getLiteralValue() instanceof Number) {
        
        Number  nvDiameter = (Number)diameter.getLiteralValue();
        Number nvHeight = (Number)height.getLiteralValue();
        // Doing the calculation
        double nBiomass = (Math.PI * Math.pow((nvDiameter.doubleValue()/2), 2.0) * nvHeight.doubleValue())/1000000;
        // Creating a node for the output parameter
        biomass = Util.makeDoubleNode(nBiomass);
        // Binding the output parameter to the node
        BindingEnvironment env = context.getEnv();
        success = env.bind(args[2], biomass);
      } 
    }   
    return success;
  }
}