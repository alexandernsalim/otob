package future.phase2.offlinetoonlinebazaar.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
public class Product {
    @Id
    private String _id;

    private Long productId;

    private String name;

    private String description;

    private double listPrice;

    private double offerPrice;

    private int stock;
}
