package queriesForWebService;

import org.apache.jena.rdf.model.InfModel;

public final class AobraSingletonInfModel {

	private static InfModel infModel = null;

	public static void initModel() throws Exception
	{
		if (infModel == null){
			System.out.println("InfModel under construction");
			AobraQueryCreationInfModel.creationModel();
			System.out.println("InfModel builds");
		}
	}
	
	public static InfModel getModel() throws Exception
	{
		if (infModel == null){
			System.out.println("InfModel is not built!");
			AobraQueryCreationInfModel.creationModel();
		}
		return infModel;
	}

	public static void setModel(InfModel modelInfered) {
		infModel = modelInfered;
		System.out.println("InfModel is now built!");
	}
}
