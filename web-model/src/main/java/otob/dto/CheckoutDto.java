package otob.dto;

import lombok.Builder;
import lombok.Data;
import otob.entity.Order;

import java.util.List;

@Data
@Builder
public class CheckoutDto {

    private Order order;
    private List<String> outOfStockProducts;

}
