package piotr.DTOs;

import lombok.*;

import java.util.LinkedList;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ICD11TreeView {
    private String name;
    private String code;
    private String parent;
    private String link;
    private LinkedList<ICD11TreeView> children;
}
