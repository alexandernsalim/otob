package otob.model.filter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderFilter {

    private String orderDate;
    private String orderStatus;

}
