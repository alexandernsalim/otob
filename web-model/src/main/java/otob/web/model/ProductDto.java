package otob.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

    private String productId;
    private String name;
    private String condition;
    private String category;
    private String returnReason;
    private String grade;
    private double listPrice;
    private double offerPrice;
    private int stock;

}
