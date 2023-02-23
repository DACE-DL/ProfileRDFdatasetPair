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
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcOkTreatmentNeighbors extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcOkTreatmentNeighbors";
	}

	@Override
	public int getArgLength() {
		return 3 ;
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

		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		Node nodeTotalNumberOfTrees = getArg(1, args, context);
		Node nodePercent = getArg(2, args, context);
		// Verify the typing of the parameters
		if (node1.isURI() 
				&& nodeTotalNumberOfTrees.getLiteralValue() instanceof Number 
				&& nodePercent.getLiteralValue() instanceof Number) {
			
			int  TotalNumberOfTrees = (int)nodeTotalNumberOfTrees.getLiteralValue();
			double Percent = (double)nodePercent.getLiteralValue(); 
			double nNumber = 0;
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			Query query = QueryFactory.create(prefix + 
					"SELECT (count(distinct ?tree) as ?number) " +
					" WHERE { " +
					"?tree bfo:BFO_0000050 " + "<" + node1.getURI() + "> ." +
					" ?tree rdf:type afy:StructuralElement ." +
					" ?tree dcterms:type <http://aims.fao.org/aos/agrovoc/c_7887> ." +
					" ?tree res:hasCloseNeighbor ?treeN ." +	
					" } " );			
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			if (result.hasNext()) {
				QuerySolution querySolution = result.next() ;
				nNumber = querySolution.getLiteral("number").getDouble() ;
				//System.out.println((TotalNumberOfTrees - ((TotalNumberOfTrees/100)*Percent)));
				if (nNumber >= (TotalNumberOfTrees - ((TotalNumberOfTrees/100)*Percent))) {
					success = true;
				}
			}
			qe.close();
		}   
		return success;
	}
}