package aobra.util;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcDistanceBetweenLines extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcDistanceBetweenLines";
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
		new AobraConf();
		String prefix = AobraConf.queryPrefix;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		// Verify the typing of the parameters
		if (node1.isURI()) {
			Node number = null;
			double nNumber = 0;
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			Query query = QueryFactory.create(prefix + 
					"SELECT ?plot (MIN(?d) as ?distance)" +
					" WHERE { " +
					" ?line1 bfo:BFO_0000050 " + "<" + node1.getURI() + "> ." +
					" ?line1 dcterms:type <http://www.afy.fr/AgroforestryVoc/caf_43> ." +
					" ?line1 geo:hasDefaultGeometry/geo:hasSerialization ?line1GeomLit. " +
					" ?line2 bfo:BFO_0000050 " + "<" + node1.getURI() + "> ."+
					" ?line2 dcterms:type <http://www.afy.fr/AgroforestryVoc/caf_43> ." +
					" ?line2 geo:hasDefaultGeometry/geo:hasSerialization ?line2GeomLit. " +
					" FILTER (?line1 != ?line2)" +
					" BIND(geof:distance(?line1GeomLit, ?line2GeomLit, uom:metre) AS ?d)" +
					" }  GROUP BY ?plot "  );

			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			//ResultSetFormatter.out(System.out, result);
			if (result.hasNext()) {
				QuerySolution querySolution = result.next() ;
				nNumber = querySolution.getLiteral("distance").getDouble() ;
				// Creating a node for the output parameter
				number = Util.makeDoubleNode((double) nNumber);
				// Binding the output parameter to the node
				BindingEnvironment env = context.getEnv();
				success = env.bind(args[1], number);
			}
			qe.close();
		}   
		return success;
	}
}