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
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcDistinctEntities extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcDistinctEntities";
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
		
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		new ProfilingConf();
		String rdf = ProfilingConf.rdf;
		Node number = null;
		double nNumber = 0;
		

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Resource s = null;
		Property p = null;
		Resource o = null;
		List<Resource> listProperty = new ArrayList<>();
		List<String> listSubject = new ArrayList<>();
	
		p = model.createProperty(rdf ,"type");
		o = model.createResource(rdf + "Property");

		Selector selector = new SimpleSelector(s, p, o) ;
		StmtIterator stmtIte= model.listStatements(selector);
		
		stmtIte.forEach((statement) -> {
            listProperty.add(statement.getSubject());
        });

		listProperty.forEach((property) -> {
			Resource s1 = null;
			Resource o1 = null;
			Selector selector1 = new SimpleSelector(s1, model.createProperty(property.getURI()), o1) ;
			StmtIterator stmtIteSubj = model.listStatements(selector1);
			stmtIteSubj.forEach((stmSubj) -> {
				if (stmSubj.getSubject().isURIResource() && stmSubj.getObject().isURIResource()) {
					listSubject.add(stmSubj.getSubject().toString());
				}
			});
        });

		// Duplicate checking
		List<String> listDistinctSubject = new ArrayList<>();
		listSubject.forEach((subject) -> {
			if (!listDistinctSubject.contains(subject)) { 
				listDistinctSubject.add(subject);
			}
		});

		nNumber = listDistinctSubject.size();
		// Creating a node for the output parameter
		number = Util.makeIntNode((int) nNumber);
		// Binding the output parameter to the node
		BindingEnvironment env = context.getEnv();
		success = env.bind(args[0], number);

		return success;
	}
}