package otob.web.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageableOrderDto {

    private int totalPage;
    private List<OrderDto> orders;

}
