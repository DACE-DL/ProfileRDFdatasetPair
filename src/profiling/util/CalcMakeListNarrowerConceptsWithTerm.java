package profiling.util;

import java.util.ArrayList;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import com.fasterxml.jackson.core.JsonProcessingException;

public class CalcMakeListNarrowerConceptsWithTerm extends BaseBuiltin {

	@Override
	public String getName() {
		return "calcMakeListNarrowerConceptsWithTerm";
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
		String rdf = ProfilingConf.rdf;
		// Check we received the correct number of parameters
		checkArgs(length, context);

		boolean success = false;

		// Retrieve the input arguments
		Node node = getArg(0, args, context);
		// Verify the typing of the parameters
		if (node.isLiteral()) {	
			String term = node.getLiteral().toString();
			//System.out.println(term);
			ArrayList<String> ListResources = new ArrayList<String>();
			Integer n = 0;
			Node s = NodeFactory.createURI(dsp + "sujet");
			Node p = NodeFactory.createURI(dsp + "predicat");
			Node o = NodeFactory.createURI(dsp + "objet");

			
			try {
				ListResources = NcboApiUtil.resourceAndDecendants(term);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			for (String resource : ListResources) {
				if (n==0){
					s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term);
					p = NodeFactory.createURI(rdf + "first");
					o = NodeFactory.createURI(resource);		
					context.add(Triple.create(s, p, o));
					n = n+1;
				} else {
					s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term + n);
					p = NodeFactory.createURI(rdf + "first");
					o = NodeFactory.createURI(resource);		
					context.add(Triple.create(s, p, o));
					if (n==1){
						s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term);
						p = NodeFactory.createURI(rdf + "rest");
						o = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term + n);		
						context.add(Triple.create(s, p, o));
						n = n+1;
					} else {
						s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term + (n-1));
						p = NodeFactory.createURI(rdf + "rest");
						o = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term + n);		
						context.add(Triple.create(s, p, o));
						n = n+1;
					}
				}
			}
			
			try {
				ListResources = AgrovocApiUtil.resourcesAndNarrower(term);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			for (String resource : ListResources) {
				if (n==0){
					s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term);
					p = NodeFactory.createURI(rdf + "first");
					o = NodeFactory.createURI(resource);		
					context.add(Triple.create(s, p, o));
					n = n+1;
				} else {
					s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term + n);
					p = NodeFactory.createURI(rdf + "first");
					o = NodeFactory.createURI(resource);		
					context.add(Triple.create(s, p, o));
					if (n==1){
						s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term);
						p = NodeFactory.createURI(rdf + "rest");
						o = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term + n);		
						context.add(Triple.create(s, p, o));
						n = n+1;
					} else {
						s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term + (n-1));
						p = NodeFactory.createURI(rdf + "rest");
						o = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term + n);		
						context.add(Triple.create(s, p, o));
						n = n+1;
					}
				}
			}

			if (n>0){
				
				if (n==1) {
					s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term);
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(rdf + "nil");		
					context.add(Triple.create(s, p, o));
				} else {
					s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term + (n-1));
					p = NodeFactory.createURI(rdf + "rest");
					o = NodeFactory.createURI(rdf + "nil");		
					context.add(Triple.create(s, p, o));
				}		
				s = NodeFactory.createURI(dsp + "listNarrowerConceptsOf" + term);
				p = NodeFactory.createURI(rdf + "type");
				o = NodeFactory.createURI(rdf + "List");		
				context.add(Triple.create(s, p, o));
				success = true;
			}
		}   
		return success;
	}
}