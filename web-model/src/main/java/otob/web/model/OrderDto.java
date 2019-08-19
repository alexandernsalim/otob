package otob.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import otob.model.entity.CartItem;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {

    private String ordId;
    private String userEmail;
    private String ordDate;
    private List<CartItem> ordItems;
    private int totItem;
    private Long totPrice;
    private String ordStatus;

}
