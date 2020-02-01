package otob.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import otob.model.entity.BazaarItem;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BazaarDto {

    private Long bazaarId;
    private String bazaarName;
    private Date bazaarStartDate;
    private Date bazaarEndDate;
    private List<BazaarItem> bazaarProducts;

}
