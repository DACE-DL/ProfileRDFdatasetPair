package profiling.util;

import java.util.ArrayList;
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
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcListClass extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcListClass";
	}

	@Override
	public int getArgLength() {
		return 1;
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
		String dsp = ProfilingConf.dsp;
		String prefix = ProfilingConf.queryPrefix;
		String rdf = ProfilingConf.rdf;
		String nameOfList = "listClass";
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		ArrayList<Uri> listResources = new ArrayList<Uri>();
		Integer n = 0;
		Node s = NodeFactory.createURI(dsp + "sujet");
		Node p = NodeFactory.createURI(dsp + "predicat");
		Node o = NodeFactory.createURI(dsp + "objet");

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Query query = QueryFactory.create(prefix + 
				"SELECT DISTINCT (?o AS ?class) " +
				" WHERE { " +
				" ?s rdf:type ?o ." +
				" FILTER isIRI(?o) " +
				" FILTER (?o != <http://www.w3.org/2002/07/owl#Class>) " +
				" FILTER (?o != <http://www.w3.org/2000/01/rdf-schema#Class>) " +
				" FILTER (?o != <http://www.w3.org/2004/02/skos/core#Concept>) " +
				" FILTER (?o != <http://www.w3.org/2002/07/owl#Thing>) " +
				" FILTER (?o != <http://www.w3.org/2002/07/owl#Nothing>) " +
				" FILTER (?o != <http://www.w3.org/1999/02/22-rdf-syntax-ns#List>) " +  
				" FILTER (?o != <http://www.w3.org/2002/07/owl#AnnotationProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#ObjectProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#DatatypeProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#FunctionalProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#InverseFunctionalProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#TransitiveProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#SymmetricProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#AsymmetricProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#ReflexiveProperty>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#IrreflexiveProperty>) " + 
				" FILTER (?o != <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property>) " + 
				" FILTER (?o != <http://www.w3.org/2002/07/owl#Ontology>) " +                                      
				" } ORDER BY ?o" );			
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				Uri uri = new Uri(querySolution.getResource("class").toString());
				listResources.add(uri) ;
			}
		}
		// n = listResources.size() ;
		try {
			ProfilingUtil.makeJsonUriFile(listResources, nameOfList);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// System.out.println("OK liste");
        
		
		for (Uri uri : listResources) {
			if (n == 0) {
				s = NodeFactory.createURI(dsp + "listURIofClass");
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createURI(uri.getUri());
				context.add(Triple.create(s, p, o));
				n = n + 1;
			} else {
				s = NodeFactory.createURI(dsp + "listURIofClass" + n);
				p = NodeFactory.createURI(rdf + "first");
				o = NodeFactory.createURI(uri.getUri());
				context.add(Triple.create(s, p, o));
				if (n == 1) {
					s = NodeFactory.createURI(dsp + "listURIofClass");
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(dsp + "listURIofClass" + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				} else {
					s = NodeFactory.createURI(dsp + "listURIofClass" + (n - 1));
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(dsp + "listURIofClass" + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				}
			}
		}

		if (n > 0) {

			if (n == 1) {
				s = NodeFactory.createURI(dsp + "listURIofClass");
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			} else {
				s = NodeFactory.createURI(dsp + "listURIofClass" + (n - 1));
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			}
			s = NodeFactory.createURI(dsp + "listURIofClass");
			p = NodeFactory.createURI(rdf + "type");
			o = NodeFactory.createURI(rdf + "List");
			context.add(Triple.create(s, p, o));
			success = true;
		}
		
		Node number = null;
		number = Util.makeIntNode((int) n);
		// Binding the output parameter to the node
		BindingEnvironment env = context.getEnv();
		success = env.bind(args[0], number);
		return success;		
	}
}