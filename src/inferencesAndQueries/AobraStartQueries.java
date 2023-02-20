package inferencesAndQueries;

import java.time.Duration;
import java.time.Instant;

public class AobraStartQueries {

	// Lancement initial pour création modèle inferé et traitement des requêtes
	// Les fichiers déposés sur le serveur (\var\www\aobra) servent à paramétrer le modéle
	//  à inférer (choix des ontologies, choix des régles à appliquer...) et les requêtes
	//  à executer
	public static void main(String[] args) throws Exception {

		Instant start0 = Instant.now();
		
		AobraCreateInferredModelAndRunQueries.InferencesAndQuery();
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}