package piotr.DAOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("main_categories")
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class MainCategories
{
    private String _id;
    private String title;
    private String definition;
    private String parent;
    private String type;
    private String wiki_link;
}
