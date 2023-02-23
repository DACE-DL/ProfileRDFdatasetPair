package webService;

import java.util.ArrayList;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import profiling.util.ProfilingUtil;
import queriesForWebService.ProfilingQueryPlots;

@Path("/plots")
public class ProfilingService {
	
	@GET
	@Path("/list")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public ArrayList<Plot> getPlots() throws Exception {
		ArrayList<Plot> listPlots = new ArrayList<Plot>();
		ResultSet resultSet = null;
		try {
			resultSet = ProfilingQueryPlots.PlotsList("fr");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String id = null;
		String idOld = "firstTime";
	    String gps = null;
	    ArrayList<String> listLabels = new ArrayList<String>();
		while (resultSet.hasNext()) {
			QuerySolution result = resultSet.next();
			
			if (idOld.contains("firstTime")) {
				idOld = result.get("id").toString();
			}
			
			id = result.get("id").toString();
			
			if (!id.contentEquals(idOld)) {
				Plot plot = new Plot();
				plot = createPlot(idOld,gps,listLabels);
				listPlots.add(plot);
				idOld=id;
				gps = result.get("gps").toString();
				listLabels.clear();
				listLabels.add(result.get("prefLabel").toString());
			} else {
				gps = result.get("gps").toString();
				listLabels.add(result.get("prefLabel").toString());
			}
			
			if (!resultSet.hasNext()) {
				Plot plot = new Plot();
				plot = createPlot(idOld,gps,listLabels);
				listPlots.add(plot);
			}
			
		} 
		System.out.println("coucou liste");
		return listPlots;
	}  

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public ArrayList<Plot> getPlot( @PathParam("id") String id ) {
		System.out.println(id);
		System.out.println(ProfilingUtil.decodeHexToStringUTF8(id));
		ArrayList<Plot> listPlots = new ArrayList<Plot>();
		ArrayList<String> listLabels = new ArrayList<String>();
		Plot plot = new Plot("<www/agro/plot1>", "<www/agro/plot1>", listLabels);
		listPlots.add(plot);
		return listPlots;
	}  
	
	public Plot createPlot(String id, String gps, ArrayList<String> listLabels) {
		Plot plot = new Plot();
		plot.setId(id);
		plot.setGps(gps);
		plot.setPrefLabels(listLabels);
		return plot;
	}
		// Pour tester
	    // http://127.0.0.1:8085/ProfilingWebService/api_Profiling/plots/list
		// http://localhost:8085/ProfilingWebService/api_Profiling/plots/<www/agro/plot1>
	    // http://localhost:8085/ProfilingWebService/api_Profiling/plots/&lt;www/agro/plot1&gt;
	
	
}
