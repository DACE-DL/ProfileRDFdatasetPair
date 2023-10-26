package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class CalcMakeVectorWithListUriAndNumber extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcMakeVectorWithListUriAndNumber";
	}

	@Override
	public int getArgLength() {
		return 3;
		// Premier paramètre en entrée	: le nom de la liste RDF de type <UriAndNumber>
		// Deuxième paramètre en sortie : une chaine litérale de la
		//  forme "c(x, y, z, ....)" (vecteur pour l'utilisation de R)
		//  les valeurs x, y et z sont les valeurs numériques liés au node
		//  par une relation (quelle qu'elle soit)
		// Troisième paramètre en sortie: la taille du vecteur
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
		List<Double> values = new ArrayList<Double>();
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node = getArg(0, args, context);
		// Verify the typing of the parameters
		if (node.isURI()) {
			String strVector = "c(0.0)";
			int nbr = 0;
			
			Model model = ModelFactory.createModelForGraph(context.getGraph());
			Query query = QueryFactory.create(prefix + 
				"SELECT (?uri AS ?Property) (?val AS ?usage) WHERE {"+
				"<" + node.getURI() + ">" +
				" rdf:rest*/rdf:first ?element ." +
				" ?element dsp:asURI ?uri ." +
				" ?element dsp:asValue ?val ." +
				" } "
			);			
			QueryExecution qe = QueryExecutionFactory.create(query, model);		
			ResultSet result = qe.execSelect();
			if (result.hasNext()) {
				while( result.hasNext() ) {
					QuerySolution querySolution = result.next() ;
					values.add(querySolution.getLiteral("usage").getDouble()) ;
				}
			}
			nbr = values.size();
			//System.out.println(values.size());
			if (!(values.size()==0)) {
				Collections.sort(values);
				// On formate le vecteur sous la forme (c(x,y,z,...))
				if (values.size()>1) {
					strVector = "c(" ;
					Boolean first = true;
					for (Double d : values) {
						if (first) {
							first = false;
						} else {
							strVector = strVector.concat(",");	
						}
						strVector = strVector.concat(d.toString());
					}
					strVector = strVector.concat(")");
					//System.out.println(strVector);
				}	
			}
			// Creating a node for the output parameter
			Node vector = NodeFactory.createLiteral(strVector);
			Node vectorSize = Util.makeIntNode(nbr);
			// Binding the output parameter to the node
			BindingEnvironment env = context.getEnv();
			success = env.bind(args[1], vector);
			success = env.bind(args[2], vectorSize);	
		}   
		return success;
	}
}