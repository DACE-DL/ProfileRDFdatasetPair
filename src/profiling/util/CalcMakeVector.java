package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class CalcMakeVector extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcMakeVector";
	}

	@Override
	public int getArgLength() {
		return 3;
		// Premier paramètre en entrée	: le nom du node anonyme avec
		//  lequel on travail. Ce nom se compose comme suit : nom de la parcelle, 
		//  nom de la propriété étudiée des éléments de la parcelle et année
		//  etudiée
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

		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node = getArg(0, args, context);
		// Verify the typing of the parameters
		if (node.isBlank()) {
			String strVector = "c(0.0)";
			int nbr = 0;
			// Touts les triplets ayant pour objet
			//  le node transmit en paramètre sont extrait du modèle (context).  
			Iterator<Triple> it = context.find(node, (Node) null, (Node) null);
			if (it.hasNext()) {
				List<Double> values = new ArrayList<Double>();
				Number number = null;
				while( it.hasNext() ) {
					Triple triple = it.next();
					// Si le sujet du triplet est une valeur numérique
					if (triple.getObject().getLiteralValue() instanceof Number) { 
						nbr++;
						number = (Number)triple.getObject().getLiteralValue();
						// Les valeurs extraites sont placées dans la liste 'values'
						values.add(number.doubleValue());
					}
				}
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
				Node vectorSize = Util.makeIntNode(nbr);;
				// Binding the output parameter to the node
				BindingEnvironment env = context.getEnv();
				success = env.bind(args[1], vector);
				success = env.bind(args[2], vectorSize);
			}
		}   
		return success;
	}
}