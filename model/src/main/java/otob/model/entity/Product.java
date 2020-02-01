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

    //TODO Change productId data type to String
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
