package otob.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "role_access")
public class RoleAccess {

    @Id
    private String id;

    @Field("eligible_access")
    private List<String> eligibleAccess;

}
