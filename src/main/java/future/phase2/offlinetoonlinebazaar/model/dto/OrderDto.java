package future.phase2.offlinetoonlinebazaar.model.dto;

import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String usrEmail;
    private String ordDate;
    private List<CartItem> ordItems;
    private int totItem;
    private Long totPrice;
    private String ordStatus;
    private List<String> outOfStockProducts;

}
