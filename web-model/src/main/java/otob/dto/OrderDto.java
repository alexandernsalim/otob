package otob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import otob.entity.CartItem;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String orderId;
    private String userEmail;
    private String ordDate;
    private List<CartItem> ordItems;
    private int totItem;
    private Long totPrice;
    private String ordStatus;

}
