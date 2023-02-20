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

public class CalcAzimutLine extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcAzimutLine";
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
		//System.out.println(node3);
		//System.out.println(node3.getLiteralDatatype());
		//System.out.println(node1);
		if (node1.isURI()) {
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			//System.out.println(node1);
			Query query = QueryFactory.create(prefix + 
					"SELECT DISTINCT ?azi " +
					" WHERE { " +
					" ?tree1 res:isInLine " + "<" + node1.getURI() + "> ." +
					" ?tree1 geo:hasDefaultGeometry ?tree1Geom ." +
					" ?tree1Geom geo:hasSerialization ?tree1GeomLit ." + 
					" BIND(xsd:decimal( replace( str(?tree1GeomLit), \"^[^0-9.-]*([-]?[0-9.]+) .*$\", \"$1\" )) as ?long1 ) ."+
		    		" BIND(xsd:decimal( replace( str(?tree1GeomLit), \"^.* ([-]?[0-9.]+)[^0-9.]*$\", \"$1\" )) as ?lat1 ) ."+
					" ?tree2 res:isInLine " + "<" + node1.getURI() + "> ." +
					" ?tree2 geo:hasDefaultGeometry ?tree2Geom ." +
					" ?tree2Geom geo:hasSerialization ?tree2GeomLit ." + 
					" BIND(xsd:decimal( replace( str(?tree2GeomLit), \"^[^0-9.-]*([-]?[0-9.]+) .*$\", \"$1\" )) as ?long2 ) ."+
					" BIND(xsd:decimal( replace( str(?tree2GeomLit), \"^.* ([-]?[0-9.]+)[^0-9.]*$\", \"$1\" )) as ?lat2 ) ."+
					" BIND(spatialF:azimuthDeg(?lat1, ?long1, ?lat2, ?long2) AS ?azi) ." +
					" FILTER (?tree1 != ?tree2) ." +
					" }  ORDER BY ?azi " );

			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			// ResultSetFormatter.out(System.out, result);
			Node number = null;
			double nMoyAzi = 0.0 ;
			double nAzi = 0.0;
			double divMoyAzi = 0.0;
			if (result.hasNext()) {
				while( result.hasNext() ) {
					QuerySolution querySolution = result.next() ;
					divMoyAzi = divMoyAzi + 1;
					//System.out.println(querySolution.getLiteral("azi"));
					nAzi = querySolution.getLiteral("azi").getDouble() ;
					if (nAzi <= 180) {
						nMoyAzi = nMoyAzi + nAzi;
					} else {
						nAzi = nAzi - 180;
						nMoyAzi = nMoyAzi + nAzi;
					}
				}
				nMoyAzi = nMoyAzi/divMoyAzi;		
				// Creating a node for the output parameter
				number = Util.makeIntNode((int) nMoyAzi);
				// Binding the output parameter to the node
				BindingEnvironment env = context.getEnv();
				success = env.bind(args[1], number);
				qe.close();
			}   

		}
		return success;
	}
}