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
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcDistanceBetweenTreesOfLine extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcDistanceBetweenTreesOfLine";
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
		new ProfilingConf();
		String prefix = ProfilingConf.queryPrefix;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		Node node2 = getArg(1, args, context);
		Node node3 = getArg(2, args, context);
		// Verify the typing of the parameters
		//System.out.println(node3.getLiteralLexicalForm());
		//System.out.println(node3.getLiteralDatatype());
		//System.out.println(node3.toString());
		if (node1.isURI() & node2.isURI()) {
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			//System.out.println("Ok 1 Neighbor");
			Query query = QueryFactory.create(prefix + 
					"SELECT ?tree1 (MIN(?dist) AS ?distance)  " +
					" WHERE { " +
					" ?tree1 dcterms:type <http://aims.fao.org/aos/agrovoc/c_7887> ." +
					" ?tree1 res:isInLine <" + node2.getURI() + "> ." +
					" ?tree1 geo:hasDefaultGeometry ?tree1Geom ." +
				    " ?tree1Geom geo:hasSerialization ?tree1GeomLit ." + 
				    " ?tree1 afy:hasProperty ?propertyOfElementHeightTrunk ." +
	                " ?propertyOfElementHeightTrunk dcterms:type <http://www.cropontology.org/rdf/CO_357:1000042> ." +
	                " ?tree1 sosa:isFeatureOfInterestOf ?observationHeightTrunk1 ." +
	                " ?observationHeightTrunk1 sosa:observedProperty ?propertyOfElementHeightTrunk ." +  
	                " ?observationHeightTrunk1 sosa:phenomenonTime ?timeHeightTrunk1 ." +
	                " ?timeHeightTrunk1 time:inXSDgYear ?year ." +
				    " ?tree2 dcterms:type <http://aims.fao.org/aos/agrovoc/c_7887> ." +
					" ?tree2 res:isInLine <" + node2.getURI() + "> ." +
					" ?tree2 geo:hasDefaultGeometry ?tree2Geom ." +
				    " ?tree2Geom geo:hasSerialization ?tree2GeomLit ." +
				    " ?tree2 sosa:isFeatureOfInterestOf ?observationHeightTrunk2 ." +
	                " ?observationHeightTrunk2 sosa:observedProperty ?propertyOfElementHeightTrunk ." +  
	                " ?observationHeightTrunk2 sosa:phenomenonTime ?timeHeightTrunk2 ." +
	                " ?timeHeightTrunk2 time:inXSDgYear ?year ." +
				    " FILTER (?tree1 != ?tree2) ." +
				    " FILTER (str(?year) = str(" + node3.getLiteralLexicalForm() + ")) ." +
					" BIND((geof:distance(?tree1GeomLit, ?tree2GeomLit, uom:metre)) as ?dist) ." +
					" }  GROUP BY ?tree1 ORDER BY ?distance " );
					
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet results = qe.execSelect();
			//ResultSetFormatter.out(System.out, results);
			boolean first = true;
			double nMoyDist = 0.0 ;
			double nDist = 0.0;
			double nRefDist = 0.0;
			double divMoyDist = 0.0;
			if (results.hasNext()) {
				while( results.hasNext() ) {
					QuerySolution querySolution = results.next() ;
					nDist = querySolution.getLiteral("distance").getDouble() ;
					if (first) {
						nRefDist = nDist;
						first = false;
					}
					if (nDist/nRefDist < 1.8) {
						divMoyDist = divMoyDist + 1;
						nMoyDist = nMoyDist + nDist;
						if (nDist > nRefDist) {
							nRefDist = nDist;
						}
					}	
				}
				nMoyDist = nMoyDist/divMoyDist;	
			// Creating a node for the output parameter
			Node distanceNode = Util.makeDoubleNode(nMoyDist);
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			success = env.bind(args[3], distanceNode);
			// System.out.println("Ok");
			}
			qe.close();
		}   
		return success;
	}
}