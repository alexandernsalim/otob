package future.phase2.offlinetoonlinebazaar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String name;

    private String description;

    private double listPrice;

    private double offerPrice;

    private int stock;

    private String picture;

    private String tag;
}
