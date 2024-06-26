package profiling.util;

import java.util.ArrayList;


public class TraitListLanguagesClass {
	static Boolean noCommonLanguage = true;
	// Recherche des LPT liés aux languages
	public static ArrayList<Lpt> makeList(ArrayList<Uri> listLanguageSource, ArrayList<Uri> listLanguageTarget) {
		
		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();
		Boolean noTagSourceLanguage	= false;
		Boolean noTagTargetLanguage	= false;

		if (listLanguageSource.size() == 0) {
			noTagSourceLanguage	= true;
		}
		if (listLanguageTarget.size() == 0) {
			noTagTargetLanguage	= true;
		}

		// Si pas de tags language pour le jeux source ou target 
		// LPT 3.1.5: Class terminological data quality
		if (noTagSourceLanguage || noTagTargetLanguage) {
			listLpts.add(new Lpt("LPT3.1.5")); // à vérifier !!!
		}

		// Si au moins une langue pour les datasets source et target
		if (!noTagSourceLanguage & !noTagTargetLanguage) {
			// On compare les langues des jeux de données 
			listLanguageSource.forEach((sourceLanguage) -> {
				listLanguageTarget.forEach((targetLanguage) -> {
					if (sourceLanguage.getUri().toString().equals(targetLanguage.getUri().toString())) {
						noCommonLanguage = false;
					}			
				});
			});
			if (noCommonLanguage) {
				// LPT 3.1.4: Class terminological multilingual problem
				listLpts.add(new Lpt("LPT3.1.4"));
			}
		}
	
		return listLpts;
	}

	
}