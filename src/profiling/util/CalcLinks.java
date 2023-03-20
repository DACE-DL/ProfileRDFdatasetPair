package profiling.util;

import java.util.ArrayList;
import java.util.List;

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

public class CalcLinks extends BaseBuiltin {

	double nNumber = 0;
	double l = 0;
	Integer n = 0;

	@Override
	public String getName() {
		return "calcLinks";
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
		String rdf = ProfilingConf.rdf;
		Node number = null;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Resource s1 = null;
		Property p1 = null;
		Resource o1 = null;
		List<Resource> listProperty = new ArrayList<>();
	
		p1 = model.createProperty(rdf ,"type");
		o1 = model.createResource(rdf + "Property");

		Selector selector = new SimpleSelector(s1, p1, o1) ;
		StmtIterator stmtIte= model.listStatements(selector);
		
		stmtIte.forEach((statement) -> {
            listProperty.add(statement.getSubject());
        });

		listProperty.forEach((property) -> {
			Resource s2 = null;
			Resource o2 = null;
			Selector selector1 = new SimpleSelector(s2, model.createProperty(property.getURI()), o2) ;
			StmtIterator stmtIte1 = model.listStatements(selector1);
			stmtIte1.forEach((stm) -> {
				if (stm.getSubject().isResource() && stm.getObject().isResource()) {
					if (!(stm.getSubject().isAnon() || stm.getObject().isAnon())) {
						if (stm.getSubject().getNameSpace() != stm.getObject().asResource().getNameSpace()) {
							nNumber ++;
							// System.out.println("sub: " + stm.getSubject().getNameSpace());
							// System.out.println("obj: " + stm.getObject().asResource().getNameSpace());
						}
					}	
				}			
			});
        });
		// Creating a node for the output parameter
		number = Util.makeIntNode((int) nNumber);
		// Binding the output parameter to the node
		BindingEnvironment env = context.getEnv();
		success = env.bind(args[0], number);
		return success;
	}
}