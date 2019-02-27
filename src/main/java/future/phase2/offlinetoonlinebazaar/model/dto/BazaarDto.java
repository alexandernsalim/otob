package future.phase2.offlinetoonlinebazaar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BazaarDto {

    private String bazaarId;
    private String name;
    private Date startDate;
    private Date endDate;

}
