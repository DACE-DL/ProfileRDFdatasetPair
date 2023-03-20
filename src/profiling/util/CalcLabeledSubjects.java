package profiling.util;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcLabeledSubjects extends BaseBuiltin {

	double nNumber = 0;
	double l = 0;
	Integer n = 0;

	@Override
	public String getName() {
		return "calcLabeledSubjects";
	}

	@Override
	public int getArgLength() {
		return 0;
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
		String rdfs = ProfilingConf.rdfs;
		Node number = null;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Resource s1 = null;
		Property p1 = null;
		Resource o1 = null;
	
		p1 = model.createProperty(rdfs ,"label");
		
		Selector selector = new SimpleSelector(s1, p1, o1) ;
		StmtIterator stmtIte= model.listStatements(selector);
		
		nNumber = IteratorUtils.size(stmtIte);
		
		// Creating a node for the output parameter
		number = Util.makeIntNode((int) nNumber);
		// Binding the output parameter to the node
		BindingEnvironment env = context.getEnv();
		success = env.bind(args[0], number);
		return success;
	}
}