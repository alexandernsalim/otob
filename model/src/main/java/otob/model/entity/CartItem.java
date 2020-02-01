package otob.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String productId;
    private String cartItemName;
    private String cartItemReturnReason;
    private String cartItemCondition;
    private double cartItemOfferPrice;
    private int cartItemQty;

}
