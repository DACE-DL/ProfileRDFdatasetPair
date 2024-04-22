package profiling.util;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class MakeListMostUsedPropertyUsagePerObject {
	
	// Création d'une liste des propriétés et de leur usage dans un triplet
	public static ArrayList<UriAndNumberAndNumberAndNumber> makeList(Model model, ArrayList<UriAndNumber> listMostUsedProperty) {
		
		String prefix = ProfilingConf.queryPrefix;

		ArrayList<UriAndNumberAndNumberAndNumber> ListResources = new ArrayList<UriAndNumberAndNumberAndNumber>();
	
		Query query = QueryFactory.create(prefix + 
			"SELECT ?property (COUNT(?i2) AS ?usage) (COUNT(DISTINCT ?i2) AS ?usage2) WHERE { " +
			// " { dsp:" + nameOfListIn + " rdf:rest*/rdf:first ?element ." +
			// " ?element dsp:asURI ?property ." +
			" ?i1 ?property ?i2 ." +
			" } GROUP BY ?property "
		);
		
 		QueryExecution qe = QueryExecutionFactory.create(query, model);		
		ResultSet result = qe.execSelect();
		if (result.hasNext()) {
			while( result.hasNext() ) {
				QuerySolution querySolution = result.next() ;
				ListResources.add(new UriAndNumberAndNumberAndNumber(querySolution.getResource("property").toString(),
				 querySolution.getLiteral("usage").getInt(),
				 querySolution.getLiteral("usage2").getInt(),
				 querySolution.getLiteral("usage").getInt()/querySolution.getLiteral("usage2").getInt()
				 )) ;
			}
		}
		
		Collections.sort(ListResources, new UriAndNumberAndNumberComparator());

		return ListResources;
	}

	static class UriAndNumberAndNumberComparator implements java.util.Comparator<UriAndNumberAndNumberAndNumber> {
		@Override
		public int compare(UriAndNumberAndNumberAndNumber a, UriAndNumberAndNumberAndNumber b) {
			if (!(b.getNumber() - a.getNumber() == 0)) {
				return b.getNumber() - a.getNumber();
			} else {
				return (b.getNumber2() - a.getNumber2());
			}
		}
	}
}