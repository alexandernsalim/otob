package otob.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "role")
public class Role {

    @Id
    private String id;

    @NotNull
    private Long roleId;

    @NotNull
    private String name;

}
