package piotr.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ICD11FullResponse {
    private String _id;
    private String title;
    private String definition;
    private String parent;
    private String type;
    private ArrayList<String> synonyms;
    private String wiki_link;
}
