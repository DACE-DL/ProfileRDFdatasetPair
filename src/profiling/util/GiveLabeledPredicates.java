package profiling.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class GiveLabeledPredicates {
	// Méthode pour compter le nombre de prédicats ayant des labels dans le modèle
    public static Integer giveNumber(Model model) {
		String rdfs = ProfilingConf.rdfs;
        // Utilisation d'un Set pour stocker les prédicats uniques qui ont des labels
        Set<Property> labeledPredicates = new HashSet<>();
        
        // Itération sur tous les triplets du modèle
        StmtIterator stmtIte = model.listStatements();
        
        // Parcours de chaque triplet
        while (stmtIte.hasNext()) {
            Statement stmt = stmtIte.next();
            Property predicate = stmt.getPredicate();
            
            // Vérification si ce prédicat a un label
			Property labelProperty = model.createProperty(rdfs ,"label");
		
			Selector selector = new SimpleSelector((Resource) predicate, labelProperty, (RDFNode) null) ;
            StmtIterator labelIterator = model.listStatements(selector);
            if (labelIterator.hasNext()) {
                labeledPredicates.add(predicate);
            }
            // Fermeture du labelIterator pour libérer les ressources
            labelIterator.close();
        }
        
        // Fermeture du StmtIterator pour libérer les ressources
        stmtIte.close();
        
        // Retourner la taille du Set qui représente le nombre de prédicats uniques ayant des labels
        return labeledPredicates.size();
    }
}