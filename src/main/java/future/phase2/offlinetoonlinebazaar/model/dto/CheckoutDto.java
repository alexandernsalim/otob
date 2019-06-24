package future.phase2.offlinetoonlinebazaar.model.dto;

import future.phase2.offlinetoonlinebazaar.model.entity.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckoutDto {

    private Order order;
    private List<String> outOfStockProducts;

}
