package profiling.util;

import java.util.ArrayList;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
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

public class CalcPropertyHierarchyDeep extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcPropertyHierarchyDeep";
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
		// Verify the typing of the parameters
		//System.out.println(node1);
		//System.out.println(node1.getLiteralDatatype());
		if (node1.isURI()) {
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			Query query = QueryFactory.create(prefix + 
			"SELECT ?Property ?Subproperty WHERE { <" + node1.getURI() + "> rdf:rest*/rdf:first ?element ." +
			" ?element dsp:asProperty ?Property ." +
			" ?element dsp:asSubproperty ?Subproperty ." +		
			" } " 
			);

			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			// ResultSetFormatter.out(System.out, result);
			Node number = null;
			Node loop = null;
			Integer nNumber = 0;
			ArrayList<UriAndUri> ListResources = new ArrayList<UriAndUri>();
			ArrayList<UriAndUri> ListResourcesTemp = new ArrayList<UriAndUri>();
			ArrayList<UriAndUri> ListResourcesTemp1 = new ArrayList<UriAndUri>();
			ArrayList<UriAndUri> ListResourcesTemp2 = new ArrayList<UriAndUri>();
			Integer maxDeep = 0;
			Integer maxDeepTemp = 0;
			String propertyName ="";
			String subpropertyName ="";
			String propertyNameTemp ="";
			Boolean infiniteLoop = false;
			Boolean infiniteLoopTemp = false;
			
			if (result.hasNext()) {
				// If there is a list, it means that there is already a first level of hierarchy
				maxDeep = 1;
				while( result.hasNext() ) {
					QuerySolution querySolution = result.next() ;
					ListResources.add(new UriAndUri(querySolution.getResource("Property").toString(), querySolution.getResource("Subproperty").toString())) ;
				}
				// Finding the maximum hierarchical depth
				
				// Duplication of the list for searches
				ListResourcesTemp = ListResources ;
				// Each line of the list of propertyes and their subpropertyes is processed
				for (UriAndUri resource : ListResources) {
				    maxDeepTemp = 1;
					propertyName = resource.getUri1();
					subpropertyName = resource.getUri2();
					propertyNameTemp = subpropertyName;
					
					// For a subproperty, we want to know if it is in the original list as a property
					ListResourcesTemp1.clear();
					for (UriAndUri resourceTemp : ListResourcesTemp) {
						// We get all the lines where it appears as a property
						if (resourceTemp.getUri1().equals(propertyNameTemp)) {
							ListResourcesTemp1.add(new UriAndUri(resourceTemp.getUri1(), resourceTemp.getUri2())) ;
						}	
					}
					
					while (!ListResourcesTemp1.isEmpty()) {	
						maxDeepTemp = maxDeepTemp + 1;
						ListResourcesTemp2.clear();
						for (UriAndUri resourceTemp1 : ListResourcesTemp1) {
							// We are looking for an Infinite Loop
							if (resourceTemp1.getUri2().equals(propertyName)) {
								infiniteLoopTemp = true;
								// We break the processing of the loop
								ListResourcesTemp1.clear();
							} 

							for (UriAndUri resourceTemp : ListResourcesTemp) {
								if (resourceTemp.getUri1().equals(resourceTemp1.getUri2())) {
									ListResourcesTemp2.add(new UriAndUri(resourceTemp.getUri1(), resourceTemp.getUri2())) ;
								}	
							}
						}	
						
						if (!ListResourcesTemp2.isEmpty()) {
							if (!infiniteLoopTemp) {
								ListResourcesTemp1 = ListResourcesTemp2;
							} else {
								ListResourcesTemp1.clear();
							}
						} else {
							ListResourcesTemp1.clear();
						}
					}
					if (infiniteLoopTemp) {
						infiniteLoop = true;
						infiniteLoopTemp = false;
					}
					if (maxDeepTemp > maxDeep) {
						maxDeep = maxDeepTemp;
					}	
				}	
				nNumber = maxDeep;
			}	
			// Creating a node for the output parameter
			number = Util.makeIntNode(nNumber);
			loop = NodeFactory.createLiteral(infiniteLoop.toString(), XSDDatatype.XSDboolean);
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			success = env.bind(args[1], number);
			if (success) {
				success = env.bind(args[2], loop );
			}
			qe.close();
		}
		return success;
	}
}