package inferencesAndQueries;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import profiling.util.ProfilingConf;
import profiling.util.ProfilingQueryObject;

public class StartTestProfilingSVG {

	public static void main(String[] args) throws Exception {

		// Initialisation de la configuration
		// Chemin d'accès, noms fichiers...
		new ProfilingConf();
		String UserLanguage = ProfilingConf.preferredLanguage;
		String prefix = ProfilingConf.queryPrefix;
		String titleQuery = "";
		String typeQuery = "";
		String stringQuery = "";
		ArrayList<String> listRules = new ArrayList<String>();
		ArrayList<String> listDatasets = new ArrayList<String>();
		String topSpatial = "";
		String consoleOutput = "";
		ArrayList<ProfilingQueryObject> listQuery = new ArrayList<ProfilingQueryObject>();


		/////////////////////////////////////////////////////
		// Etude des parcelles                             //
		/////////////////////////////////////////////////////

		/////////////////////////////////////////////////////
		// Injection des données nécessaires pour l'étude  //
		/////////////////////////////////////////////////////
		listQuery.clear();
		titleQuery = "";
		typeQuery = "INSERT";

		// Insertion dans le modèle du nameSpace de l'ontologie (pour les rules)
		stringQuery = prefix + "INSERT DATA {res:thisOntology res:hasNameSpace res:.}";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		
		/////////////////////////////
		// Affichage des résultats //
		/////////////////////////////

		titleQuery = "ETUDES DES PARCELLES";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Années étudiées";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (str(?year) AS ?Annee) WHERE {?yearOfStudy rdf:type res:YearForStudy." +
				"?yearOfStudy res:hasYear ?year." +
				"?yearOfStudy res:hasRank ?rank." +
				"} ORDER BY ?rank";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Périodes étudiées";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT (?period AS ?Periode) (str(?year) AS ?AnneeDebut) (str(?nextYear) AS ?AnneeFin) WHERE { " + 
				"?period rdf:type res:Period." +
				"?period res:hasRank ?rank." +
				"?period res:hasBegin ?year." +
				"?period res:hasEnd ?nextYear." +
				"} ORDER BY ?rank";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Parcelles étudiées";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) WHERE {?plot res:isPlot ?true." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Parcelles forestières";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) WHERE {?plot res:isTFPlot ?true." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Propriétés étudiées";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?property AS ?Propriété) WHERE { " +
				"?property res:isPropertyToStudy 'true'^^xsd:Boolean." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "ETUDES STATISTIQUES DES MESURES";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques sur la croissance des arbres";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2015";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2015 diamètres des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueDM) AS ?Moyenne_Diametres_Des_Troncs)"+ 
				" WHERE {" +
				"?plot res:hasTreeTrunkDiameterMean2015 ?valueDM." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));


		titleQuery = "Statistiques 2015 hauteurs des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueHTM) AS ?Moyenne_Hauteurs_Des_Troncs)"+ 
				" WHERE {" +
				"?plot res:hasTreeHeightTrunkMean2015 ?valueHTM." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));


		titleQuery = "Statistiques 2016";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2016 diamètres des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueDM) AS ?Moyenne_Diametres_Des_Troncs)"+ 
				" WHERE {" +
				"?plot res:hasTreeTrunkDiameterMean2016 ?valueDM." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2016 hauteurs des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueHM) AS ?Moyenne_Hauteurs_Des_Troncs)"+ 
				" WHERE {" +
				"?plot res:hasTreeHeightTrunkMean2016 ?valueHM." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2017";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2017 diamètres des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueDM) AS ?Moyenne_Diametres_Des_Troncs)"+ 
				" WHERE {" +
				"?plot res:hasTreeTrunkDiameterMean2017 ?valueDM." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2017 hauteurs des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueHM) AS ?Moyenne_Hauteurs_Des_Troncs)"+ 
				" WHERE {" +
				"?plot res:hasTreeHeightTrunkMean2017 ?valueHM." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Quantiles";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2015";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2015 diamètres des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueDQ1) AS ?Quantile_25)"+ 
				"(str(?valueDQ2) AS ?Quantile_50)"+ 
				"(str(?valueDQ3) AS ?Quantile_75)"+ 
				" WHERE {" +
				"?plot res:hasTreeTrunkDiameterQ12015 ?valueDQ1." +
				"?plot res:hasTreeTrunkDiameterQ22015 ?valueDQ2." +
				"?plot res:hasTreeTrunkDiameterQ32015 ?valueDQ3." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2015 hauteurs des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueHTQ1) AS ?Quantile_25)"+ 
				"(str(?valueHTQ2) AS ?Quantile_50)"+ 
				"(str(?valueHTQ3) AS ?Quantile_75)"+ 
				" WHERE {" +
				"?plot res:hasTreeHeightTrunkQ12015 ?valueHTQ1." +
				"?plot res:hasTreeHeightTrunkQ22015 ?valueHTQ2." +
				"?plot res:hasTreeHeightTrunkQ32015 ?valueHTQ3." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2016";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));		

		titleQuery = "Statistiques 2016 diamètres des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueDQ1) AS ?Quantile_25)"+ 
				"(str(?valueDQ2) AS ?Quantile_50)"+ 
				"(str(?valueDQ3) AS ?Quantile_75)"+ 
				" WHERE {" +
				"?plot res:hasTreeTrunkDiameterQ12016 ?valueDQ1." +
				"?plot res:hasTreeTrunkDiameterQ22016 ?valueDQ2." +
				"?plot res:hasTreeTrunkDiameterQ32016 ?valueDQ3." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2016 hauteurs des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueHQ1) AS ?Quantile_25)"+ 
				"(str(?valueHQ2) AS ?Quantile_50)"+ 
				"(str(?valueHQ3) AS ?Quantile_75)"+ 
				" WHERE {" +
				"?plot res:hasTreeHeightTrunkQ12016 ?valueHQ1." +
				"?plot res:hasTreeHeightTrunkQ22016 ?valueHQ2." +
				"?plot res:hasTreeHeightTrunkQ32016 ?valueHQ3." +

				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));


		titleQuery = "Statistiques 2017";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2017 diamètres des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueDQ1) AS ?Quantile_25)"+ 
				"(str(?valueDQ2) AS ?Quantile_50)"+ 
				"(str(?valueDQ3) AS ?Quantile_75)"+ 
				" WHERE {" +
				"?plot res:hasTreeTrunkDiameterQ12017 ?valueDQ1." +
				"?plot res:hasTreeTrunkDiameterQ22017 ?valueDQ2." +
				"?plot res:hasTreeTrunkDiameterQ32017 ?valueDQ3." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2017 hauteur des troncs";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueHQ1) AS ?Quantile_25)"+ 
				"(str(?valueHQ2) AS ?Quantile_50)"+ 
				"(str(?valueHQ3) AS ?Quantile_75)"+ 
				" WHERE {" +
				"?plot res:hasTreeHeightTrunkQ12017 ?valueHQ1." +
				"?plot res:hasTreeHeightTrunkQ22017 ?valueHQ2." +
				"?plot res:hasTreeHeightTrunkQ32017 ?valueHQ3." +

				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Statistiques 2015 Biomasse des arbres";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?value) AS ?Mediane)"+ 
				" WHERE {" +
				"?plot res:hasBiomassMedian2015 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "ETUDES CATEGORIES";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Nombres d'arbres par catégories";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Nombres d'arbres par catégories pour l'année 2015";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (?categoryName AS ?Categorie) (COUNT(?categoryName) as ?nombre_d_arbres)" +
				" WHERE {" +
				"?plot res:isPlot ?true." +
				"?structuralElement bfo:partOf ?plot." +
				"?observation sosa:hasFeatureOfInterest ?structuralElement." +
				"?observation sosa:observedProperty <http://www.afy.fr/Restinclieres/hasCategory>." +
				"?observation sosa:hasResult ?category." +
				"?category res:hasLocalName ?categoryName." +
				"?observation sosa:phenomenonTime ?timeCategory ." +
				"?timeCategory time:inXSDgYear \"2015\"^^<http://www.w3.org/2001/XMLSchema#gYear> ." +
				"} GROUP BY ?plot ?categoryName ORDER BY ?plot ?categoryName";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));	
		
		titleQuery = "Nombres d'arbres par catégories pour l'année 2016";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (?categoryName AS ?Categorie) (COUNT(?categoryName) as ?nombre_d_arbres)" +
				" WHERE {" +
				"?plot res:isPlot ?true." +
				"?structuralElement bfo:partOf ?plot." +
				"?observation sosa:hasFeatureOfInterest ?structuralElement." +
				"?observation sosa:observedProperty <http://www.afy.fr/Restinclieres/hasCategory>." +
				"?observation sosa:hasResult ?category." +
				"?category res:hasLocalName ?categoryName." +
				"?observation sosa:phenomenonTime ?timeCategory ." +
				"?timeCategory time:inXSDgYear \"2016\"^^<http://www.w3.org/2001/XMLSchema#gYear> ." +
				"} GROUP BY ?plot ?categoryName ORDER BY ?plot ?categoryName";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));	
		
		titleQuery = "Nombres d'arbres par catégories pour l'année 2017";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (?categoryName AS ?Categorie) (COUNT(?categoryName) as ?nombre_d_arbres)" +
				" WHERE {" +
				"?plot res:isPlot ?true." +
				"?structuralElement bfo:partOf ?plot." +
				"?observation sosa:hasFeatureOfInterest ?structuralElement." +
				"?observation sosa:observedProperty <http://www.afy.fr/Restinclieres/hasCategory>." +
				"?observation sosa:hasResult ?category." +
				"?category res:hasLocalName ?categoryName." +
				"?observation sosa:phenomenonTime ?timeCategory ." +
				"?timeCategory time:inXSDgYear \"2017\"^^<http://www.w3.org/2001/XMLSchema#gYear> ." +
				"} GROUP BY ?plot ?categoryName ORDER BY ?plot ?categoryName";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));	
		
		titleQuery = "ETUDE DE LA BIOMASSE";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio biomasse des arbres éligibles au négoce / biomasse totale par parcelle";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio pour le négoce de catégorie I";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio biomasse des arbres éligibles au négoce Cat I/ biomasse totale par parcelle pour l'année 2015";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio)" +
				" WHERE {" +
				"?plot res:hasBiomassBycategoryIRatio2015 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio biomasse des arbres éligibles au négoce Cat I / biomasse totale par parcelle pour l'année 2016";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) " +
				" WHERE {" +
				"?plot res:hasBiomassBycategoryIRatio2016 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio biomasse des arbres éligibles au négoce Cat I / biomasse totale par parcelle pour l'année 2017";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) "+
				" WHERE {" +
				"?plot res:hasBiomassBycategoryIRatio2017 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));


		titleQuery = "Ratio pour le négoce de catégorie II";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio biomasse des arbres éligibles au négoce Cat II/ biomasse totale par parcelle pour l'année 2015";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) "+ 
				" WHERE {" +
				"?plot res:hasBiomassBycategoryIIRatio2015 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio biomasse des arbres éligibles au négoce Cat II / biomasse totale par parcelle pour l'année 2016";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) "+
				" WHERE {" +
				"?plot res:hasBiomassBycategoryIIRatio2016 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio biomasse des arbres éligibles au négoce Cat II/ biomasse totale par parcelle pour l'année 2017";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) " +
				" WHERE {" +
				"?plot res:hasBiomassBycategoryIIRatio2017 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));


		titleQuery = "Ratio pour le négoce de catégorie III";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));


		titleQuery = "Ratio biomasse des arbres éligibles au négoce Cat III/ biomasse totale par parcelle pour l'année 2015";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) " +
				" WHERE {" +
				"?plot res:hasBiomassBycategoryIIIRatio2015 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio biomasse des arbres éligibles au négoce Cat III / biomasse totale par parcelle pour l'année 2016";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) " +
				" WHERE {" +
				"?plot res:hasBiomassBycategoryIIIRatio2016 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Ratio biomasse des arbres éligibles au négoce Cat III / biomasse totale par parcelle pour l'année 2017";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) " +
				" WHERE {" +
				"?plot res:hasBiomassBycategoryIIIRatio2017 ?value." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));


		titleQuery = "ETUDES DU RENDEMENT DES CULTURES";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Rendements des cultures";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Rendement culture 2016";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?sampling AS ?Echantillonnage)(?withNoTagSpeciesName AS ?NomCulture)(str(?value) AS ?Rendement)" +
	            "WHERE {?sampling rdf:type sosa:Sampling." +
				"?sampling res:hasYieldPerHectareMean2016 ?value." +
				"?sampling res:hasSpecies ?species." +
				"?species skos:prefLabel ?speciesName." +
				"FILTER (lang(?speciesName) = '" + UserLanguage + "')" +
				"BIND (STR(?speciesName)  AS ?withNoTagSpeciesName)" +
				"} ORDER BY ?sampling";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Rendement culture 2017";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?sampling AS ?Echantillonnage)(?withNoTagSpeciesName AS ?NomCulture)(str(?value) AS ?Rendement) WHERE {?sampling rdf:type sosa:Sampling." +
				"?sampling res:hasYieldPerHectareMean2017 ?value." +
				"?sampling res:hasSpecies ?species." +
				"?species skos:prefLabel ?speciesName." +
				"FILTER (lang(?speciesName) = '" + UserLanguage + "')" +
				"BIND (STR(?speciesName)  AS ?withNoTagSpeciesName)" +
				"} ORDER BY ?sampling";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Rendement culture 2018";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?sampling AS ?Echantillonnage)(?withNoTagSpeciesName AS ?NomCulture)(str(?value) AS ?Rendement) WHERE {?sampling rdf:type sosa:Sampling." +
				"?sampling res:hasYieldPerHectareMean2018 ?value." +
				"?sampling res:hasSpecies ?species." +
				"?species skos:prefLabel ?speciesName." +
				"FILTER (lang(?speciesName) = '" + UserLanguage + "')" +
				"BIND (STR(?speciesName)  AS ?withNoTagSpeciesName)" +
				"} ORDER BY ?sampling";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Performance rendement culture 2016";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) WHERE {?plot res:isPlot ?true." +
				"?plot res:hasYieldPerHectarePerformance2016 ?value" +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Performance rendement culture 2017";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(?value) AS ?Ratio) WHERE {?plot res:isPlot ?true." +
				"?plot res:hasYieldPerHectarePerformance2017 ?value" +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Performance rendement culture 2018";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) (str(round(?value)) AS ?Ratio) WHERE {?plot res:isPlot ?true." +
				"?plot res:hasYieldPerHectarePerformance2018 ?value" +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "ETUDES PROXIMITE DU LEZ";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Moyennes des distances au Lez par catégories";
		typeQuery = "";
		stringQuery = "";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));

		titleQuery = "Moyennes des distances au Lez par catégories en 2015";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueCat1) AS ?CategorieI )"+ 
				"(str(?valueCat2) AS ?CategorieII )"+ 
				"(str(?valueCat3) AS ?CategorieIII )"+ 
				" WHERE {" +
				"OPTIONAL {?plot res:hasDistanceTo_le_LezBycategoryIMean2015   ?valueCat1 }." +
				"OPTIONAL {?plot res:hasDistanceTo_le_LezBycategoryIIMean2015  ?valueCat2 }." +
				"OPTIONAL {?plot res:hasDistanceTo_le_LezBycategoryIIIMean2015 ?valueCat3 }." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Moyennes des distances au Lez par catégories en 2016";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueCat1) AS ?CategorieI )"+ 
				"(str(?valueCat2) AS ?CategorieII )"+ 
				"(str(?valueCat3) AS ?CategorieIII )"+ 
				" WHERE {" +
				"OPTIONAL {?plot res:hasDistanceTo_le_LezBycategoryIMean2016   ?valueCat1 }." +
				"OPTIONAL {?plot res:hasDistanceTo_le_LezBycategoryIIMean2016  ?valueCat2 }." +
				"OPTIONAL {?plot res:hasDistanceTo_le_LezBycategoryIIIMean2016 ?valueCat3 }." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		titleQuery = "Moyennes des distances au Lez par catégories en 2017";
		typeQuery = "SELECT";
		stringQuery = prefix + "SELECT DISTINCT (?plot AS ?Parcelle) " +
				"(str(?valueCat1) AS ?CategorieI )"+ 
				"(str(?valueCat2) AS ?CategorieII )"+ 
				"(str(?valueCat3) AS ?CategorieIII )"+ 
				" WHERE {" +
				"OPTIONAL {?plot res:hasDistanceTo_le_LezBycategoryIMean2017   ?valueCat1 }." +
				"OPTIONAL {?plot res:hasDistanceTo_le_LezBycategoryIIMean2017  ?valueCat2 }." +
				"OPTIONAL {?plot res:hasDistanceTo_le_LezBycategoryIIIMean2017 ?valueCat3 }." +
				"} ORDER BY ?plot";
		listQuery.add(new ProfilingQueryObject(titleQuery, typeQuery, stringQuery));
		
		
		// Sauvegarde des queries dans un fichier json
		//	ObjectMapper objectMapper = new ObjectMapper();
	    //	objectMapper.writeValue(new File("D:\\var\\www\\Profiling\\queries\\queryForTreesAndCropsStatistics.json"), listQuery);
		
		Instant start0 = Instant.now();
		
		listRules.add("profiling.rules");
		topSpatial = "true";
		consoleOutput = "true";
		String fileNameTestResult = "Result_Test_profiling";

		CreateInferredModelAndRunQueries.InferencesAndQuery(listDatasets, listRules, topSpatial, listQuery, consoleOutput, fileNameTestResult);
		
		Instant end0 = Instant.now();
		System.out.println("Total running time : " + Duration.between(start0, end0).getSeconds() + " secondes");
	}  
}