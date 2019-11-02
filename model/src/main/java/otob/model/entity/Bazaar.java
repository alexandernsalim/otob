package otob.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "bazaar")
public class Bazaar {

    @Id
    private String id;

    private String bazaarId;
    private List<String> bazaarProducts;
    private String bazaarStartDate;
    private String bazaarEndDate;
    private Office bazaarLocation;

}
