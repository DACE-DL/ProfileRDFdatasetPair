package profiling.util;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcGiveLabelOfRessource extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcGiveLabelOfRessource";
	}

	@Override
	public int getArgLength() {
		return 2 ;
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
		new ProfilingConf();
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		// Verify the typing of the parameters
		if (node1.isURI()) {
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			Node resLabel = null ;
			Literal label = null ;
			Resource resource = null;
			resource = model.createResource(node1.getURI());
			label = ProfilingUtil.getBestStringLiteral(resource);
			if (label != null) {
			// Creating a node for the output parameter
			resLabel = NodeFactory.createLiteral(label.getLexicalForm().toString())   ;
			}
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			if (!(resLabel == null)) {
			  success = env.bind(args[1], resLabel);
			}
			if (!success) {
				//System.out.println(node1);
			}
		}   
		return success;
	}
}