package piotr.DAOs;

import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@AllArgsConstructor
@Data
@Document("ICD-11")
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class ICD11 {
    private String _id;
    private String title;
    private String definition;
    private String parent;
    private String type;
    private ArrayList<String> synonyms;
    private String wiki_link;
}
