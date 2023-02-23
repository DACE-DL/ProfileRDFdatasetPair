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
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcNeighborOfNeighbors extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcNeighborOfNeighbors";
	}

	@Override
	public int getArgLength() {
		return 6 ;
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
		Node node2 = getArg(1, args, context);
		Node node3 = getArg(2, args, context);
		Node node4 = getArg(3, args, context);
		Node node5 = getArg(4, args, context);
		// Verify the typing of the parameters
		//System.out.println(node3);
		//System.out.println(node3.getLiteralDatatype());
		if (node1.isURI() & node2.isURI() & node3.isURI() &
				node4.getLiteralValue() instanceof String &
				node5.getLiteralValue() instanceof String) {

			Model model = ModelFactory.createModelForGraph(context.getGraph());
			String strDistanceMax = (String) node4.getLiteralValue(); 
			String strAngleMax = (String) node5.getLiteralValue(); 
			//System.out.println("Ok 1 Neighbor");
			/*
			 * Boolean bool = false; if (node1.getURI() ==
			 * "http://www.afy.fr/Restinclieres/PA3AFL03A41" && node2.getURI() ==
			 * "http://www.afy.fr/Restinclieres/PA3AFL03A44") { System.out.println("Ok");
			 * System.out.println(node1.getURI()); System.out.println(node2.getURI());
			 * System.out.println(node3.getURI()); bool = true; };
			 */
			
			
			
			Query query = QueryFactory.create(prefix + 
					"SELECT ?treeN ?d" +
					" WHERE { " +
					"<" + node1.getURI() + ">" + " geo:hasDefaultGeometry/geo:hasSerialization ?tree1GeomLit ." + 
		      		" BIND(xsd:decimal(replace( str(?tree1GeomLit), \"^[^0-9.-]*([-]?[0-9.]+) .*$\", \"$1\" )) as ?long1 ) ."+
		    		" BIND(xsd:decimal( replace( str(?tree1GeomLit), \"^.* ([-]?[0-9.]+)[^0-9.]*$\", \"$1\" )) as ?lat1 ) ."+
		    		"<" + node2.getURI() + ">" + " geo:hasDefaultGeometry/geo:hasSerialization ?tree2GeomLit ." + 
		      		" BIND(xsd:decimal(replace( str(?tree2GeomLit), \"^[^0-9.-]*([-]?[0-9.]+) .*$\", \"$1\" )) as ?long2 ) ."+
		    		" BIND(xsd:decimal( replace( str(?tree2GeomLit), \"^.* ([-]?[0-9.]+)[^0-9.]*$\", \"$1\" )) as ?lat2 ) ."+
		    		" BIND(spatialF:azimuthDeg(?lat1, ?long1, ?lat2, ?long2) AS ?azi)" +
		    		" ?treeN bfo:BFO_0000050 " + "<" + node3.getURI() + "> ." +
		    		" ?treeN dcterms:type <http://aims.fao.org/aos/agrovoc/c_7887> ." +
				    " FILTER NOT EXISTS { "+
		    		  "   ?treeN res:hasCloseNeighbor* "+ "<" + node1.getURI() + ">"  +
		    		" } " +
		    		" FILTER NOT EXISTS { "+
		    		  "   ?treeN res:hasCloseNeighbor* "+ "<" + node2.getURI() + ">"  +
		    		" } " + 
					" ?treeN geo:hasDefaultGeometry/geo:hasSerialization ?treeNGeomLit ." +  
				    " BIND(xsd:decimal(replace( str(?treeNGeomLit), \"^[^0-9.-]*([-]?[0-9.]+) .*$\", \"$1\" )) as ?longN ) ."+
		    		" BIND(xsd:decimal( replace( str(?treeNGeomLit), \"^.* ([-]?[0-9.]+)[^0-9.]*$\", \"$1\" )) as ?latN ) ."+
		    		" BIND(spatialF:azimuthDeg(?lat1, ?long1, ?latN, ?longN) AS ?aziN)" +
					" BIND(geof:distance(?tree2GeomLit, ?treeNGeomLit, uom:metre) AS ?d)" +
					" BIND(xsd:decimal(ABS(?aziN - ?azi)) AS ?diffAzi)" +
		     		" FILTER (?d <= " + strDistanceMax + ")" +
		     		" FILTER ( ?diffAzi <= " + strAngleMax + ")" +
					" }  ORDER BY ASC(?diffAzi) ASC(?d)  LIMIT 1" );
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			///ResultSetFormatter.out(System.out, result);
			/*
			 * if (bool) { ResultSetFormatter.out(System.out, result); }
			 */
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
			  success = env.bind(args[5], firstNeighbor);
			}
			if (!success) {
				//System.out.println(node1);
			}
			qe.close();
		}   
		return success;
	}
}