package piotr.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import piotr.DAOs.ICD11;
import piotr.DTOs.ICD11FullResponse;
import piotr.api.Repository.ICD11Repository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ICD11Service {

    @Autowired
    private ICD11Repository icd_11Repository;

    public List<ICD11FullResponse> getAllByNameOrICD11(String name, String ICD, Pageable pageable) {
        if(name != null) {
            List<ICD11> allByTitle = icd_11Repository.findAllByTitle(name, pageable);

            List<ICD11FullResponse> collect = allByTitle.stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
            return collect;
        }
        if(ICD != null) {
            List<ICD11FullResponse> collect = List.of(icd_11Repository.findById(ICD).get()).stream().map(this::mapToResponseDTO).collect(Collectors.toList());
            return collect;
        }
        Page<ICD11> all = icd_11Repository.findAll(pageable);
        List<ICD11FullResponse> collect = all.getContent().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
        return collect;
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
}
