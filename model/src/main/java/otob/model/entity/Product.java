package otob.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product") 
public class Product {
    @Id
    private String id;

    private Long productId;
    private String productName;
    private String productCondition;
    private String productCategory;
    private String productReturnReason;
    private String productGrade;
    private double productListPrice;
    private double productOfferPrice;
    private int productStock;
}
