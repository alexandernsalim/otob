package otob.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bazaar")
public class Bazaar {

    @Id
    private String id;

    private Long bazaarId;
    private String bazaarName;
    private List<BazaarItem> bazaarProducts;
    private Date bazaarStartDate;
    private Date bazaarEndDate;

}
