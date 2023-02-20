package aobra.util;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcGeomLine extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcGeomLine";
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
		new AobraConf();
		String prefix = AobraConf.queryPrefix;
		String rdf = AobraConf.rdf;
		String sf = AobraConf.sf;
		String geo = AobraConf.geo;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		Node node2 = getArg(1, args, context);
		// Verify the typing of the parameters
		//System.out.println(node3);
		//System.out.println(node3.getLiteralDatatype());
		if (node1.isURI() & node2.isURI()) {
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			//System.out.println("Ok 1 Neighbor");
			Query query = QueryFactory.create(prefix + 
					"SELECT ?long ?lat ?tree" +
					" WHERE { " +
					" ?tree dcterms:type <http://aims.fao.org/aos/agrovoc/c_7887> ." +
					" ?tree res:isInLine <" + node2.getURI() + "> ." +
					" ?tree geo:hasDefaultGeometry ?treeGeom ." +
				    " ?treeGeom geo:hasSerialization ?treeGeomLit ." + 
				    " bind( replace( str(?treeGeomLit), \"^[^0-9\\\\.-]*([-]?[0-9\\\\.]+) .*$\", \"$1\" ) as ?long ) ." +
				    " bind( replace( str(?treeGeomLit), \"^.* ([-]?[0-9\\\\.]+)[^0-9\\\\.]*$\" , \"$1\" ) as ?lat ) ." +
					" }  ORDER BY ?tree " );
					
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
		    // ResultSetFormatter.out(System.out, result);
			String resGeom = "" ;
			String strGeom = "" ;
			Integer cpt = 0 ;
			if (result.hasNext()) {
				while( result.hasNext() ) {
				   if (cpt > 0) {
					   strGeom = strGeom + ", " ; 
				   }
				   QuerySolution querySolution = result.next() ;
				   Literal longstr = querySolution.getLiteral("long");
				   Literal latstr = querySolution.getLiteral("lat");
				   strGeom = strGeom + longstr + " " + latstr;
				   cpt = cpt + 1 ;
				}
			if (cpt > 1) {	
				resGeom = "LINESTRING(" + strGeom + ")";
			} else {
				resGeom = "POINT(" + strGeom + ")";
			}
			// System.out.println(resGeom);
			}
			
			// Creating a node for the output parameter
			
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			if (!(resGeom == null)) {
				Node s1 = NodeFactory.createURI(node2.getURI() + "Geom" );
				Node p1 = NodeFactory.createURI(rdf + "type");
				Node o1 = NodeFactory.createURI(sf + "MultiligneString");
				context.add(Triple.create(s1, p1, o1)); 
				// user defined datatype for wktLiteral
				final TypeMapper tm=TypeMapper.getInstance();
				final  RDFDatatype wktLiteral = tm.getSafeTypeByName(geo + "wktLiteral");
				Node p2 = NodeFactory.createURI(geo + "asWKT");
				Node o2 = NodeFactory.createLiteralByValue(resGeom, wktLiteral);
				// System.out.println(o2);
				context.add(Triple.create(s1, p2, o2)); 
				Node p3 = NodeFactory.createURI(geo + "hasSerialization");
				context.add(Triple.create(s1, p3, o2)); 
				success = env.bind(args[2], s1);
			}
			if (!success) {
				// System.out.println("KO");
			}
			qe.close();
		}   
		return success;
	}
}