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
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcNearestNeighbor extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcNearestNeighbor";
	}

	@Override
	public int getArgLength() {
		return 4 ;
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
		Node node2 = getArg(1, args, context);
		Node node3 = getArg(2, args, context);
		// Verify the typing of the parameters
		//System.out.println(node3);
		//System.out.println(node3.getLiteralDatatype());
		if (node1.isURI() & node2.isURI() &
				node3.getLiteralValue() instanceof String) {

			Model model = ModelFactory.createModelForGraph(context.getGraph());
			String strDistanceMax = (String) node3.getLiteralValue(); 
			//System.out.println("Ok 1 Neighbor");
			Query query = QueryFactory.create(prefix + 
					"SELECT ?treeN ?d" +
					" WHERE { " +
					"<" + node1.getURI() + ">" + " geo:hasDefaultGeometry ?treeGeom ." +
				    " ?treeGeom geo:hasSerialization ?treeGeomLit ." +  
				    " ?treeN bfo:partOf " + "<" + node2.getURI() + "> ." +
					" ?treeN dcterms:type res:term_Tree ." +
					" ?treeN geo:hasDefaultGeometry ?treeNGeom ." +
				    " ?treeNGeom geo:hasSerialization ?treeNGeomLit ." + 
					" FILTER (<" + node1.getURI() + "> != ?treeN)" +
					" BIND(geof:distance(?treeGeomLit, ?treeNGeomLit, uom:metre) AS ?d)" +
					" FILTER (?d < " + strDistanceMax + ")" +
					" }  ORDER BY ASC(?d)  LIMIT 1" );
					
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			// ResultSetFormatter.out(System.out, result);
			Resource resFirstNeighbor = null ;
			if (result.hasNext()) {
				QuerySolution querySolution = result.next() ;
				resFirstNeighbor = querySolution.getResource("treeN");
			}
			// Creating a node for the output parameter
			
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			if (!(resFirstNeighbor == null)) {
			  Node firstNeighbor = resFirstNeighbor.asNode();
			  success = env.bind(args[3], firstNeighbor);
			}
			if (!success) {
				//System.out.println(node1);
			}
			qe.close();
		}   
		return success;
	}
}