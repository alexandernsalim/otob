package future.phase2.offlinetoonlinebazaar.model.response;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserResponse {

    @NotNull
    @NotEmpty
    private String email;

}
