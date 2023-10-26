package profiling.util;

public class GiveRQuantile {
	
	// Calcul du Quantile d'un vecteur
	public static double giveDouble(String strJavaVector, String strProb) {
		double nQuantile = 0.0;
		
		if (!strJavaVector.equals("") &&
				!strProb.equals(""))  {
			// Doing the calculation
			nQuantile = RUtil.Quantile(strJavaVector,strProb);
			//System.out.println(nQuantile);
		}
	
		return nQuantile;
	}	
}