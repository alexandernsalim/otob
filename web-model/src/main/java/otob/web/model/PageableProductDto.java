package otob.web.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class PageableProductDto implements Serializable {

    private int totalPage;
    private List<ProductDto> products;

}
