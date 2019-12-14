package piotr.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import piotr.DAOs.ICD11;
import piotr.DAOs.MainCategories;
import piotr.DTOs.ICD11FullResponse;
import piotr.DTOs.ICD11TreeView;
import piotr.api.Repository.ICD11Repository;
import piotr.api.Repository.MainCategoriesRepository;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ICD11Service {

    @Autowired
    private ICD11Repository repository;

    @Autowired
    private MainCategoriesRepository mainCategoriesRepository;

    public ICD11TreeView getAllByNameOrICD11(String name, String ICD) {
        return getIcd11TreeView(reformatDiseaseName(name), ICD);
    }
    public ICD11TreeView getAllByNameFuzzy(String name, String ICD) {
        return getIcd11TreeViewFuzzy(reformatDiseaseName(name), ICD);
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

    private ICD11TreeView getIcd11TreeView(String name, String ICD) {
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
            ICD11 byTitle = repository.findAllByTitle(name).get(0);
            ICD11TreeView tree = getTree(byTitle);
            return tree;
        }
        if(ICD != null) {
            ICD11 byId = repository.findById(ICD).get();
            ICD11TreeView tree = getTree(byId);
            return tree;
        }
        MainCategories firstMainCategory = mainCategoriesRepository.findAll().get(0);
        ICD11 byId = repository.findById(firstMainCategory.get_id()).get();
        ICD11TreeView tree = getTree(byId);
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
        ICD11TreeView root;
        if(!icd11.getParent().equals("ICD-11")) {
            ICD11 byId = repository.findById(icd11.getParent()).get();
            root = getTreeNode(byId);
            LinkedList<ICD11TreeView> rootChildren = root.getChildren();
            List<ICD11TreeView> brothers = repository.findAllByParent(icd11.getParent()).stream()
                    .map(this::getTreeNode)
                    .collect(Collectors.toList());
            List<ICD11TreeView> children = repository.findAllByParent(icd11.get_id()).stream()
                    .map(this::getTreeNode)
                    .filter(icd11TreeView -> !icd11TreeView.getName().equals(build.getName()))
                    .collect(Collectors.toList());
            ICD11TreeView base = brothers.get(0);
            base.setChildren(new LinkedList<>(children));
            brothers.set(0, base);
            rootChildren.addAll(brothers);
            root.setChildren(rootChildren);
            return root;
        } else {
            root = build;
            LinkedList<ICD11TreeView> rootChildren = root.getChildren();
            List<ICD11TreeView> brothers = repository.findAllByParent(icd11.getParent()).stream()
                    .map(this::getTreeNode)
                    .collect(Collectors.toList());
            rootChildren.addAll(brothers);
            root.setChildren(rootChildren);
        }
        return root;

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
