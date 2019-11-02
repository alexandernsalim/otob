package otob.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "office")
public class Office {

    @Id
    private String id;

    private String officeId;
    private String officeName;
    private String officeLocation;

}
