package otob.model.entity;

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

    private String orderId;
    private String userEmail;
    private String orderDate;
    private List<CartItem> orderItems;
    private int orderTotalItem;
    private Long orderTotalPrice;
    private String orderStatus;

}
