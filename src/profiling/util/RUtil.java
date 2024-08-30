package profiling.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;


// Il faut en premier lieu que R soit installé sur la machine
// Sous R lancer l'installation du package rJava :  install.packages("rJava")
// Pour connaitre le répertoire d'installaton de rJava (jri.dll) : system.file("jri",package="rJava")
// Afin que le chemin de java.library.path soit correct mettre dans le PATH de Windows les répertoires d'intallation de rJava et de R
// si différent ! C:\Users\conde\AppData\Local\R\win-library\4.2\rJava\jri\x64 et C:\Program Files\R\R-4.2.2\bin\x64;

public class RUtil {
    
	public static double Sum(String javaVector) {
		REXP result = null;
		double sum = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		// le vecteur créer dans un context JAVA est placé dans une variable R
		rengine.eval("rVector <-" + javaVector);
		// Calcul de la somme en utilisant la syntaxe R
		rengine.eval("sumVal=sum(rVector)");
		// Récupèration de la somme 
		result = rengine.eval("sumVal");
		sum = result.asDouble();

		rengine.end();
		return sum;
	}	
	
	public static double Mean(String javaVector) {
		REXP result = null;
		double mean = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		// le vecteur créer dans un context JAVA est placé dans une variable R
		rengine.eval("rVector <-" + javaVector);
		// Calcul de la moyenne en utilisant la syntaxe R
		rengine.eval("meanVal=mean(rVector)");
		// Récupération de la moyenne 
		result = rengine.eval("meanVal");
		mean = result.asDouble();
		//mean = 318.0;
		//mean = 318.0 - Math.random() * ( 420 - 400 );
		//mean = Math.random() * ( 420 - 400 );
		//System.out.println("Initial double value is " + mean);

		//String hexStringRepresentation = Double.toHexString(mean);
		//System.out.println("Hex value is " + hexStringRepresentation);

		rengine.end();
		return mean;
	}
	public static double Median(String javaVector) {
		REXP result = null;
		double median = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		// le vecteur créer dans un context JAVA est placé dans une variable R
		rengine.eval("rVector <-" + javaVector);
		// Calcul de la médiane en utilisant la syntaxe R
		rengine.eval("medianVal=median(rVector)");
		// Récupération de la médiane 
		result = rengine.eval("medianVal");
		median = result.asDouble();

		rengine.end();
		return median;
	}

	public static double Sd(String javaVector) {
		REXP result = null;
		double sd = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		// le vecteur créer dans un context JAVA est placé dans une variable R
		rengine.eval("rVector <-" + javaVector);
		// Calcul de l'écart type en utilisant la syntaxe R
		rengine.eval("sdVal=sd(rVector)");
		// Récupération de l'écart type 
		result = rengine.eval("sdVal");
		sd = result.asDouble();

		rengine.end();
		return sd;
	}
	
	public static double Var(String javaVector) {
		REXP result = null;
		double var = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		// le vecteur créer dans un context JAVA est placé dans une variable R
		rengine.eval("rVector <-" + javaVector);
		// Calcul de la variance en utilisant la syntaxe R
		rengine.eval("varVal=var(rVector)");
		// Récupération de la variance
		result = rengine.eval("varVal");
		var = result.asDouble();

		rengine.end();
		return var;
	}
	
	public static double Quantile(String javaVector,String prob) {
		REXP result = null;
		double qt = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		// le vecteur créer dans un context JAVA est placé dans une variable R
		rengine.eval("rVector <-" + javaVector);
		rengine.eval("rProb <-" + prob);
		// Calcul de la variance en utilisant la syntaxe R
		rengine.eval("varQuantile=quantile(rVector,probs=rProb)");
		// Récupération de la variance
		result = rengine.eval("varQuantile");
		qt = result.asDouble();

		rengine.end();
		return qt;
	}
	
	
	public static String AsJavaVector(String strVector) {
        // A supprimer
		String strJavaVector = "c(0.0)";

		String[] arrOfStr = strVector.split(";", 0); 
		if (arrOfStr.length>1) {
			strJavaVector = "c(" ;
			Boolean first = true;
			for (String a : arrOfStr) {
				if (first) {
					first = false;
				} else {
					strJavaVector = strJavaVector.concat(",");	
				}
				strJavaVector = strJavaVector.concat(a);
			}
			strJavaVector = strJavaVector.concat(")");
			//System.out.println("strJavaVector");
			//System.out.println(strJavaVector);
		}
		return strJavaVector;
	}
	
	public static double Shapiro(String javaVector) {
		REXP result = null;
		double var = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		// le vecteur créer dans un context JAVA est placé dans une variable R
		rengine.eval("rVector <-" + javaVector);
		// Calcul de la variance en utilisant la syntaxe R
		rengine.eval("varVal=shapiro.test(rVector)$p.value");
		// Récupération de la variance
		result = rengine.eval("varVal");
		var = result.asDouble();

		rengine.end();
		return var;
	}
	
	public static double KS(String javaVector) {
		REXP result = null;
		double var = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		// le vecteur créer dans un context JAVA est placé dans une variable R
		rengine.eval("rVector <-" + javaVector);
		// Calcul de la variance en utilisant la syntaxe R
		rengine.eval("varVal=(ks.test(rVector,\"pnorm\",mean(rVector),sd(rVector)))$p.value");
		// Récupération de la variance
		result = rengine.eval("varVal");
		var = result.asDouble();

		rengine.end();
		return var;
	}
	
	public static double Wilcoxon(String javaVector1, String javaVector2) {
		REXP result = null;
		Double var = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		// le vecteur créer dans un context JAVA est placé dans une variable R
		rengine.eval("rVector1 <-" + javaVector1);
		rengine.eval("rVector2 <-" + javaVector2);
		// Calcul de la variance en utilisant la syntaxe R
		rengine.eval("varVal=(wilcox.test(rVector1, rVector2))$p.value");
		// Récupération de la variance
		result = rengine.eval("varVal");
		var = result.asDouble();
		//System.out.println(javaVector1);
		//System.out.println(javaVector2);
		//System.out.println(result);
		//System.out.println(var);

		rengine.end();
		return var;

	}
	
	public static double AreaWKTPolygone(String wKTPolygone) {

		Path pathLibrarySf = Paths.get(ProfilingConf.folderForScriptR , ProfilingConf.fileNameScriptRSf);
		String librarylocation = pathLibrarySf.toString().replace("\\","/");
		//System.out.println(librarylocation);
		
		REXP result = null;
		double area = 0.00;
		// Démarage moteur R
		Rengine rengine = Rengine.getMainEngine();
		if(rengine == null)
			rengine = new Rengine(new String[] {"--nosave"}, false, null);
		
		//calling required libraries
		//String librarylocation= rengine.eval(".libPaths()").asString();
		//System.out.println("location of the R libraries:" + librarylocation);
		
		// Mise en place de sf
		//rengine.eval("source(file=\"D:/var/www/scriptR/sf.R\")");
		
		rengine.eval("source(file=\""+ librarylocation +"\")");
		
		// le polygone créer dans un context JAVA est placé dans une variable R sfc
		rengine.eval("polygone <- st_as_sfc(\"" + wKTPolygone + "\")");	
		//rengine.eval("polygone <- st_as_sfc(\"POLYGON ((3.85814230908693 43.7078928083093 ,3.85821194095464 43.7078932584668 ,3.85836072786939 43.7069126875986 ,3.8582661829975 43.7069071077705 ,3.85831668738707 43.7065789174642 ,3.85820258530704 43.7065705840649 ,3.85826540453139 43.7062032190276 ,3.85896660411972 43.7062459779223 ,3.85888189455432 43.7068417085174 ,3.85880470074365 43.7068369042593 ,3.85878926828091 43.7069634034435 ,3.85870540055734 43.7069580653245 ,3.85855785125612 43.7080686535345 ,3.85863235089928 43.7080773801429 ,3.85862357875053 43.7081417275462 ,3.85870556783625 43.7081475680194 ,3.8586513027368 43.7085393185673 ,3.85858083383099 43.7085342987411 ,3.85854696708 43.7087404029131 ,3.85766847020686 43.7086882208638 ,3.85770226558507 43.7084758805546 ,3.85780910943248 43.7084821023117 ,3.85783339095781 43.7083465198559 ,3.85790404770207 43.7083501633426 ,3.85792538117605 43.7082725435412 ,3.85808900049097 43.7082771521884 ,3.85814230908693 43.7078928083093 ))\")");	
		
		//REXP x = null;
		//System.out.println(x=rengine.eval("polygone"));
				
		// le crs WGS 84 est affecté au sfc
		rengine.eval("st_crs(polygone) <- 4326");
		
		//REXP y = null;
		//System.out.println(y=rengine.eval("st_crs(polygone)"));
		
		// Calcul de l'aire en utilisant la syntaxe R
		rengine.eval("area <- st_area(polygone)");
		
		//REXP z2 = null;
		//System.out.println(z2=rengine.eval("area"));
		
		
		// changement d'unité de métre carré à hectare 
		rengine.eval("areaHA=units::set_units(x = area, value = ha)");
		// Récupération de l'aire
		result = rengine.eval("areaHA");
		
		area = result.asDouble();
		//System.out.println(area);
		rengine.end();
		return area;
	}
	
}
