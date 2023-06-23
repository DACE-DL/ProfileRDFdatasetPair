package profiling.util;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcPropertyUsageDistinctPerObject extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcPropertyUsageDistinctPerObject";
	}

	@Override
	public int getArgLength() {
		return 5;
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
		String prefix = ProfilingConf.queryPrefix;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;
	
		int nObjectCount = 0;
		int nUsageSum = 0;
		double nUsageMean = 0.0 ;
		int nUsageMax = 0;
		int nUsageMin = 0;
		

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Query query = QueryFactory.create(prefix + 
			"SELECT DISTINCT (COUNT(?object) AS ?objectCount) (SUM(?usage) AS ?usageSum) (MIN(?usage) AS ?usageMin) (MAX(?usage) AS ?usageMax)" +
				" WHERE { " +
					"{ SELECT DISTINCT (?s AS ?object) (COUNT(DISTINCT ?element) AS ?usage) " +
					" WHERE { " +
					"dsp:listURIofProperty rdf:rest*/rdf:first ?element ." +
					" ?s ?element ?o ." +
					" FILTER isIRI(?s) " +
					" } GROUP BY ?s }" +
				" } "
		);			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			QuerySolution querySolution = result.next() ;
			nObjectCount = querySolution.getLiteral("objectCount").getInt();
			nUsageSum = querySolution.getLiteral("usageSum").getInt();
			nUsageMin = querySolution.getLiteral("usageMin").getInt();
			nUsageMax = querySolution.getLiteral("usageMax").getInt();
			
		}

		Node objectCount = null;
		Node usageSum = null;
		Node usageMean = null;
		Node usageMax = null;
		Node usageMin = null;
		
			
		// Creating a node for the output parameter
		objectCount = Util.makeIntNode(nObjectCount);
		usageSum = Util.makeIntNode(nUsageSum );
		nUsageMean = (double) nUsageSum/(double) nObjectCount;	
		usageMean = Util.makeDoubleNode(nUsageMean);
		usageMax = Util.makeIntNode(nUsageMax );
		usageMin = Util.makeIntNode(nUsageMin );

		// Binding the output parameter to the node
		BindingEnvironment env = context.getEnv();
		env.bind(args[0], objectCount);
		env.bind(args[1], usageSum);
		env.bind(args[2], usageMean);
		env.bind(args[3], usageMin);
		env.bind(args[4], usageMax);

		success = true;

		return success;
	}
}