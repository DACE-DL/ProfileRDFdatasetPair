package profiling.util;

import java.util.Iterator;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcIsInLine extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcIsInLine";
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
		String dsp = ProfilingConf.dsp;
		String dcterms = ProfilingConf.dcterms;
		String bfo = ProfilingConf.bfo;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node1 = getArg(0, args, context);
		Node node2 = getArg(1, args, context);
		// Verify the typing of the parameters
		if (node1.isURI() & node2.isURI()) {
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			Query query = QueryFactory.create(prefix + 
					"SELECT ?treeN ?line" +
					" WHERE { " +
					"<" + node2.getURI() + ">" + " dsp:hasCloseNeighbor*|^dsp:hasCloseNeighbor* ?treeN ." +
					"?treeN dsp:isInLine ?line ." +
					" }  ORDER BY ?line ?treeN  LIMIT 1" );			
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			Resource resLine = null ;
			if (result.hasNext()) {
				while( result.hasNext() ) {
				   QuerySolution querySolution = result.next() ;
				   resLine = querySolution.getResource("line");
				}
			} else {
				//System.out.println("Je n'ai pas de voisin avec une ligne");
				//System.out.println(node1.getURI());
				//System.out.println(node2.getURI());
				// Création d'un numéro de ligne
				Integer number = 1;
				Integer numberTemp = 0;
				RDFDatatype dt = XSDDatatype.XSDinteger;
				Node s = NodeFactory.createURI(node1.getURI() + "CounterLine");
				Node p = NodeFactory.createURI(dsp + "hasCounterValue");
				Iterator<Triple> itCompteur = context.find(s, p, (Node) null);
				Node o = null;
				if (itCompteur.hasNext()) {
					while( itCompteur.hasNext() ) {
						Triple triple = itCompteur.next();
						if ((Integer) triple.getObject().getLiteralValue() > numberTemp ) {
							numberTemp = (Integer) triple.getObject().getLiteralValue();
						} 
					}	
						number = numberTemp + 1;
					    o = NodeFactory.createLiteralByValue(number, dt);
						context.add(Triple.create(s, p, o));
				} else {
				    o = NodeFactory.createLiteralByValue("1", dt);
					context.add(Triple.create(s, p, o));
					
				}
				//System.out.println("Creation ligne");
				//System.out.println(number);
				resLine = ResourceFactory.createResource(node1.getURI() + "Line" + number);
				Node s2 = NodeFactory.createURI(node1.getURI() + "Line" + number);
				Node p2 = NodeFactory.createURI(dcterms + "type");
				Node o2 = NodeFactory.createURI("http://www.afy.fr/AgroforestryVoc/caf_43");
				context.add(Triple.create(s2, p2, o2)); 
				Node p3 = NodeFactory.createURI(bfo + "BFO_0000050");
				Node o3 = NodeFactory.createURI(node1.getURI());
				context.add(Triple.create(s2, p3, o3));
			
			}
			// Creating a node for the output parameter
			
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			if (!(resLine == null)) {
			  Node line = resLine.asNode();
			  success = env.bind(args[2], line);
			}
			if (!success) {
				//System.out.println(node1);
			}
			qe.close();
		}   
		return success;
	}
}