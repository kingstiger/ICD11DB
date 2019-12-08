package piotr.DTOs;

import lombok.*;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
