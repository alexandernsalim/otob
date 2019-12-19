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

    private String bazaarId;
    private String bazaarName;
    private List<BazaarItem> bazaarProducts;
    private Date bazaarStartDate;
    private Date bazaarEndDate;

}
