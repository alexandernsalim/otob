package otob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private int productId;

    private String name;

    private String description;

    private double listPrice;

    private double offerPrice;

    private int stock;
}
