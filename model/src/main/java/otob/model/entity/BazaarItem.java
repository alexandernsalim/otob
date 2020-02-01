package otob.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BazaarItem {

    private int productId;
    private String productName;
    private String productCondition;
    private String productCategory;
    private String productReturnReason;
    private String productGrade;
    private double productListPrice;
    private double productOfferPrice;
    private int productStock;

}
