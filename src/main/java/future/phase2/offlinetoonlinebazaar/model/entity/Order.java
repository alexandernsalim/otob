package future.phase2.offlinetoonlinebazaar.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order")
public class Order {

    @Id
    private String id;

    private Long ordId;
    private String usrEmail;
    private String ordDate;
    private List<CartItem> ordItems;
    private int totItem;
    private Long totPrice;
    private String ordStatus;

}
