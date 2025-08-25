package co.com.bancolombia.model.role;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Role {
    private String id;
    private String name;
    private String description;
}
