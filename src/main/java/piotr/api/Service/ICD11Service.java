package piotr.api.Service;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import piotr.DAOs.ICD11;
import piotr.DAOs.MainCategories;
import piotr.DTOs.ICD11FullResponse;
import piotr.DTOs.ICD11TreeView;
import piotr.api.Repository.ICD11Repository;
import piotr.api.Repository.MainCategoriesRepository;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ICD11Service {

    @Autowired
    private ICD11Repository repository;

    @Autowired
    private MainCategoriesRepository mainCategoriesRepository;

    public ICD11TreeView getAllByNameOrICD11(String name, String ICD) throws Exception {
        return getIcd11TreeView(
                (name != null) ? reformatDiseaseName(name) : null, ICD);
    }
    public ICD11TreeView getAllByNameFuzzy(String name, String ICD) {
        return getIcd11TreeViewFuzzy((name != null) ? reformatDiseaseName(name) : null, ICD);
    }

    private String reformatDiseaseName(String name) {
        String[] strings = name.split(" ");
        String s = strings[0];
        strings[0] = s.replace(s.charAt(0), String.valueOf(s.charAt(0)).toUpperCase().charAt(0));
        StringBuilder finalString = new StringBuilder(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            String string = strings[i];
            finalString
                    .append(" ")
                    .append(string);
        }
        return finalString.toString();
    }

    private ICD11TreeView getIcd11TreeView(String name, String ICD) throws Exception {
        if(((name != null) && name.equals("ICD-11")) || ((ICD != null) && ICD.equals("ICD-11"))) {
            ICD11TreeView god_himself = ICD11TreeView.builder()
                    .name("Literally, the whole database")
                    .code("ICD-11")
                    .parent("God himself")
                    .link("https://en.wikipedia.org/wiki/International_Statistical_Classification_of_Diseases_and_Related_Health_Problems")
                    .build();
            god_himself.setChildren(getMainCategories());
            return god_himself;
        }
        if(ICD != null) {
            try {

                ICD11 byId = repository.findById(ICD).get();
                ICD11TreeView tree = getTree(byId);
                return tree;
            }
            catch (NullPointerException ignored){};
        }
        if(name != null) {
            try {
                ICD11 byTitle = (repository.findAllByTitle(name) != null) ? repository.findAllByTitle(name).get(0) : repository.findAllByTitleLike(name).get(0);
                ICD11TreeView tree = getTree(byTitle);
                return tree;
            }
            catch (NullPointerException ignored){};
        }
        ICD11TreeView tree;
        try {
            MainCategories firstMainCategory = mainCategoriesRepository.findAll().get(0);
            ICD11 byId = repository.findById(firstMainCategory.get_id()).get();
            tree = getTree(byId);
        } catch (Exception ex) {
            throw new Exception("SMTH WRONG");
        }
        return tree;
    }

    private ICD11TreeView getIcd11TreeViewFuzzy(String name, String ICD) {
        if(((name != null) && name.equals("ICD-11")) || ((ICD != null) && ICD.equals("ICD-11"))) {
            ICD11TreeView god_himself = ICD11TreeView.builder()
                    .name("Literally, the whole database")
                    .code("ICD-11")
                    .parent("God himself")
                    .link("https://en.wikipedia.org/wiki/International_Statistical_Classification_of_Diseases_and_Related_Health_Problems")
                    .build();
            god_himself.setChildren(getMainCategories());
            return god_himself;
        }
        if(name != null) {
            ICD11 byTitle = repository.findAllByTitleLike(name).get(0);
            ICD11TreeView tree = getTree(byTitle);
            return tree;
        }

        MainCategories firstMainCategory = mainCategoriesRepository.findAll().get(0);
        ICD11 byId = repository.findById(firstMainCategory.get_id()).get();
        ICD11TreeView tree = getTree(byId);
        return tree;
    }

    public List<ICD11FullResponse> getAll() {
        List<ICD11> all = repository.findAll();
        return all
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ByteArrayOutputStream getAllOWL() throws Exception {
        List<ICD11FullResponse> all = getAll();

        IRI IOR = IRI.create("http://owl.api.icd11");

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.createOntology(IOR);
        OWLDataFactory df = ontology.getOWLOntologyManager().getOWLDataFactory();

        OWLClass root = df.getOWLClass(IRI.create(IOR + "#" + "ICD-11"));
        OWLDeclarationAxiom ra = df.getOWLDeclarationAxiom(root);
        ontology.add(ra);

        for (ICD11FullResponse icd : all) {
            OWLClass data = df.getOWLClass(IRI.create(IOR + "#" + icd.get_id()));

            OWLAnnotation title = df.getOWLAnnotation(df.getRDFSLabel(), df.
                    getOWLLiteral("[" + icd.get_id() + "] " + icd.getTitle()));
            OWLAxiom ax1 = df.getOWLAnnotationAssertionAxiom(data.getIRI(), title);
            manager.applyChange(new AddAxiom(ontology, ax1));

            if (!icd.getDefinition().isEmpty()) {
                OWLAnnotation definition = df.getOWLAnnotation(df.getRDFSComment(), df.
                        getOWLLiteral("Definition:\n" + icd.getDefinition()));
                OWLAxiom ax2 = df.getOWLAnnotationAssertionAxiom(data.getIRI(), definition);
                manager.applyChange(new AddAxiom(ontology, ax2));
            }

            OWLAnnotation type = df.getOWLAnnotation(df.getRDFSComment(), df.
                    getOWLLiteral("Type: " + icd.getType()));
            OWLAxiom ax3 = df.getOWLAnnotationAssertionAxiom(data.getIRI(), type);
            manager.applyChange(new AddAxiom(ontology, ax3));

            if (!icd.getSynonyms().isEmpty()) {
                List<String> tmp = icd.getSynonyms();
                StringBuilder synonyms = new StringBuilder("Synonyms:\n");
                for (String synonym : tmp) {
                    synonyms.append(synonym + "\n");
                }
                OWLAnnotation syn = df.getOWLAnnotation(df.getRDFSComment(), df.
                        getOWLLiteral(synonyms.toString()));
                OWLAxiom ax4 = df.getOWLAnnotationAssertionAxiom(data.getIRI(), syn);
                manager.applyChange(new AddAxiom(ontology, ax4));
            }

            if (!icd.getWiki_link().isEmpty()) {
                OWLAnnotation wiki_link = df.getOWLAnnotation(df.getRDFSSeeAlso(), df.
                        getOWLLiteral("Wikipedia: " + icd.getWiki_link()));
                OWLAxiom ax5 = df.getOWLAnnotationAssertionAxiom(data.getIRI(), wiki_link);
                manager.applyChange(new AddAxiom(ontology, ax5));
            }

            OWLClass parent = df.getOWLClass(IOR + "#" + icd.getParent());
            OWLSubClassOfAxiom ax6 = df.getOWLSubClassOfAxiom(data, parent);
            ontology.add(ax6);
        }

        OWLDocumentFormat format = manager.getOntologyFormat(ontology);
        OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
        if (format.isPrefixOWLDocumentFormat()) {
            owlxmlFormat.copyPrefixesFrom(format.asPrefixOWLDocumentFormat());
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ontology.saveOntology(owlxmlFormat, byteArrayOutputStream);
        return byteArrayOutputStream;
//        manager.saveOntology(ontology, owlxmlFormat, IRI.create(new File("tmp/ICD11.owl").toURI()));
//        File file = new File("tmp/ICD11.owl");
//        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String line : lines) {
//            stringBuilder.append(line + "\n");
//        }
//        return stringBuilder.toString();
    }






    private ICD11TreeView mapMainCategoryToTree(MainCategories mainCategories) {
        return ICD11TreeView.builder()
                .code(mainCategories.get_id())
                .name(mainCategories.getTitle())
                .link(mainCategories.getWiki_link())
                .parent("ICD-11")
                .children(new LinkedList<>())
                .build();
    }

    private LinkedList<ICD11TreeView> getMainCategories() {
        List<MainCategories> all = mainCategoriesRepository.findAll();
        return all
                .stream()
                .map(this::mapMainCategoryToTree)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private ICD11FullResponse mapToResponseDTO(ICD11 icd_11) {
        return ICD11FullResponse.builder()
                ._id(icd_11.get_id())
                .title(icd_11.getTitle())
                .definition(icd_11.getDefinition())
                .parent(icd_11.getParent())
                .type(icd_11.getType())
                .synonyms(icd_11.getSynonyms())
                .wiki_link(icd_11.getWiki_link())
                .build();
    }

    private ICD11TreeView getTree(ICD11 icd11) {
        ICD11TreeView build = getTreeNode(icd11);
            build.setChildren(
                    new LinkedList<>(repository.findAllByParent(icd11.get_id())
                    .stream()
                    .map(this::getTreeNode)
                    .collect(Collectors.toList())
                    )
            );
        return build;

    }

    private ICD11TreeView getTreeNode(ICD11 byId) {
        return ICD11TreeView.builder()
                .name(byId.getTitle())
                .code(byId.get_id())
                .link(byId.getWiki_link())
                .parent(byId.getParent())
                .children(new LinkedList<>())
                .build();
    }
}
