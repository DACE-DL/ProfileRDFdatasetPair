package aobra.util;

import java.util.Iterator;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcNodeMean extends BaseBuiltin {
  
  @Override
  public String getName() {
    return "calcNodeMean";
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
	  Node node = getArg(0, args, context);
	  // Verify the typing of the parameters
	  System.out.println("coucou1 de calc");
	  if (node.isBlank()) {
		  System.out.println("coucou2 de calc");
		  Node mean = null;
		  Iterator<Triple> it = context.find(node, (Node) null, (Node) null);
		  if (it.hasNext()) {
			  System.out.println("coucou3 de calc");
			  int div = 0;
			  double num = 0.0;
			  double nMean = 0.0;
			  while( it.hasNext() ) {
				  Triple triple = it.next();
				  if (triple.getObject().getLiteralValue() instanceof Number) { 
					  div = div + 1;
					  Number number = (Number)triple.getObject().getLiteralValue();
					  num = num + number.doubleValue();
				  }
			  }
			  // Doing the calculation
			  if (div > 0.0) {
				  nMean = num / div;
			  }
			  // Creating a node for the output parameter
			  mean = Util.makeDoubleNode(nMean);
			  // Binding the output parameter to the node
			  BindingEnvironment env = context.getEnv();
			  success = env.bind(args[1], mean);
		  }

	  }   
	  return success;
  }
}