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
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcMakeListPropertyOfInterest extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcMakeListPropertyOfInterest";
	}

	@Override
	public int getArgLength() {
		return 0;
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
		String rdf = ProfilingConf.rdf;
		String prefix = ProfilingConf.queryPrefix;
		String nameOfList = "listURIofPropertyOfInterest";
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		ArrayList<UriAndUriAndUri> ListResources = new ArrayList<UriAndUriAndUri>();
		ArrayList<String> ListProperty = new ArrayList<String>();
		ArrayList<UriAndUri> ListPairOfClass = new ArrayList<UriAndUri>();
		
		Integer n = 0;
		Node s = NodeFactory.createURI(dsp + "sujet");
		Node p = NodeFactory.createURI(dsp + "predicat");
		Node o = NodeFactory.createURI(dsp + "objet");
		Node b = NodeFactory.createBlankNode();
		Node u1 = NodeFactory.createURI(dsp + "class1");
		Node u2 = NodeFactory.createURI(dsp + "class2");
		Node u3 = NodeFactory.createURI(dsp + "property");
		Node pu1 = NodeFactory.createURI(dsp + "asClass1");
		Node pu2 = NodeFactory.createURI(dsp + "asClass2");
		Node pu3 = NodeFactory.createURI(dsp + "asProperty");

		Model model = ModelFactory.createModelForGraph(context.getGraph());
		Query query = QueryFactory.create(prefix + 
				"SELECT ?property WHERE { " +
					" dsp:listURIofProperty rdf:rest*/rdf:first ?property . " +
				" } " );	
		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next();
				ListProperty.add(querySolution.getResource("property").toString()) ;
			}
		}

		Query query2 = QueryFactory.create(prefix + 
				"SELECT DISTINCT ?class1 ?class2 WHERE { " +
		 				" dsp:listURIofClassOfInterest rdf:rest*/rdf:first ?element . " +
		 				" ?element dsp:asClass1 ?class1 ." + 
		 				" ?element dsp:asClass2 ?class2 ." +
		 		    	" } "  );	
		QueryExecution qe2 = QueryExecutionFactory.create(query2, model);		
		ResultSet result2 = qe2.execSelect();
		if (result2.hasNext()) {
			while( result2.hasNext() ) {
				QuerySolution querySolution2 = result2.next();
				ListPairOfClass.add(new UriAndUri(querySolution2.getResource("class1").toString(), querySolution2.getResource("class2").toString())) ;
			}
		}

		ListProperty.forEach((property) -> {
			ListPairOfClass.forEach((pairOfclass) -> {
				//System.out.println(property);
		   		//System.out.println(pairOfclass.getUri1());
				//System.out.println(pairOfclass.getUri2());
				//System.out.println("******  ******");
				Query query3 = QueryFactory.create(prefix +
						"SELECT DISTINCT ?class1 ?property ?class2 " +
						" WHERE { " +
						" values ?property {<" + property + "> }" +
						" values ?class1 {<" + pairOfclass.getUri1() + "> }" +
						" values ?class2 {<" + pairOfclass.getUri2() + "> }" +
						" FILTER EXISTS {?intanceClass1 rdf:type ?class1 . ?intanceClass2 rdf:type ?class2 . ?intanceClass1 ?property ?instanceClass2 } " +
						" } GROUP BY ?class1 ?property ?class2" );
				QueryExecution qe3 = QueryExecutionFactory.create(query3, model);
				ResultSet result3 = qe3.execSelect();
				if (result3.hasNext()) {
					while( result3.hasNext() ) {
						QuerySolution querySolution3 = result3.next() ;
				        //System.out.println(querySolution3.getResource("property").toString());
		   		        //System.out.println(querySolution3.getResource("class1").toString());
						//System.out.println(querySolution3.getResource("class2").toString());
						//System.out.println("**************************");
		 				ListResources.add(new UriAndUriAndUri(querySolution3.getResource("class1").toString(), querySolution3.getResource("property").toString(), querySolution3.getResource("class2").toString())) ;
				 	}
				}
			});	
        });


		for (UriAndUriAndUri resource : ListResources) {
			if (n == 0) {
				s = NodeFactory.createURI(dsp + nameOfList);
				p = NodeFactory.createURI(rdf + "first");
				
				b = NodeFactory.createBlankNode();
				u1 = NodeFactory.createURI(resource.getUri1());
				u2 = NodeFactory.createURI(resource.getUri2());
				u3 = NodeFactory.createURI(resource.getUri3());
				context.add(Triple.create(b, pu1, u1));
				context.add(Triple.create(b, pu2, u2));
				context.add(Triple.create(b, pu3, u3));

				context.add(Triple.create(s, p, b));
				n = n + 1;
			} else {
				s = NodeFactory.createURI(dsp + nameOfList + n);
				p = NodeFactory.createURI(rdf + "first");
				
				b = NodeFactory.createBlankNode();
				u1 = NodeFactory.createURI(resource.getUri1());
				u2 = NodeFactory.createURI(resource.getUri2());
				u3 = NodeFactory.createURI(resource.getUri3());
				context.add(Triple.create(b, pu1, u1));
				context.add(Triple.create(b, pu2, u2));
				context.add(Triple.create(b, pu3, u3));

				context.add(Triple.create(s, p, b));
				if (n == 1) {
					s = NodeFactory.createURI(dsp + nameOfList);
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(dsp + nameOfList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				} else {
					s = NodeFactory.createURI(dsp + nameOfList + (n - 1));
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(dsp + nameOfList + n);
					context.add(Triple.create(s, p, o));
					n = n + 1;
				}
			}
		}

		if (n > 0) {

			if (n == 1) {
				s = NodeFactory.createURI(dsp + nameOfList);
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			} else {
				s = NodeFactory.createURI(dsp + nameOfList + (n - 1));
				p = NodeFactory.createURI(rdf + "rest");
				o = NodeFactory.createURI(rdf + "nil");
				context.add(Triple.create(s, p, o));
			}
			s = NodeFactory.createURI(dsp + nameOfList);
			p = NodeFactory.createURI(rdf + "type");
			o = NodeFactory.createURI(rdf + "List");
			context.add(Triple.create(s, p, o));
			success = true;
		}
		return success;
	}
}