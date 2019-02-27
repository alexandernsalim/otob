package future.phase2.offlinetoonlinebazaar.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bazaar")
public class Bazaar {
    @Id
    private String _id;

    private Long bazaarId;

    @NotNull
    private String name;

    private Date startDate;
    private Date endDate;
    private Boolean activeStatus;

}
