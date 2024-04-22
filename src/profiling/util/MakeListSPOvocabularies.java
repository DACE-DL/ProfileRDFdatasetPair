package profiling.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;

public class MakeListSPOvocabularies {
    
    public static ArrayList<List<String>> makeList(Model model, ArrayList<UriAndNumber> listProperty) {
        ArrayList<List<String>> ListResourcesSPO = new ArrayList<>(3);

        Set<String> distinctSubjectVocabularies = new HashSet<>();
        Set<String> distinctPredicatVocabularies = new HashSet<>();
        Set<String> distinctObjectVocabularies = new HashSet<>();

		// On boucle sur les propriétés
        for (UriAndNumber property : listProperty) {
            Property p = model.createProperty(property.getUri());
            String namespacePredicat = ProfilingUtil.controlNameSpace(p.getNameSpace());
            if (!namespacePredicat.isEmpty()) {
                distinctPredicatVocabularies.add(namespacePredicat);
            }

			// On traite les Sujets pour une propriété donnée
            ResIterator subjects = model.listSubjectsWithProperty(p);
            subjects.forEachRemaining(subject -> {
                if (!subject.isAnon() && subject.isURIResource()) {
                    String namespaceSubject = ProfilingUtil.controlNameSpace(subject.getNameSpace());
                    if (!namespaceSubject.isEmpty()) {
                        distinctSubjectVocabularies.add(namespaceSubject);
                    }
                }
            });

			// On traite les Objets pour une propriété donnée
            StmtIterator statements = model.listStatements(null, p, (Resource) null);
            statements.forEachRemaining(statement -> {
                if (statement.getObject().isURIResource()) {
                    String namespaceObject = ProfilingUtil.controlNameSpace(statement.getObject().asResource().getNameSpace());
                    if (!namespaceObject.isEmpty()) {
                        distinctObjectVocabularies.add(namespaceObject);
                    }
                }
            });
        }

        ListResourcesSPO.add(new ArrayList<>(distinctSubjectVocabularies));
        ListResourcesSPO.add(new ArrayList<>(distinctPredicatVocabularies));
        ListResourcesSPO.add(new ArrayList<>(distinctObjectVocabularies));

        return ListResourcesSPO;
    }
}
