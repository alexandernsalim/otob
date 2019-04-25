package future.phase2.offlinetoonlinebazaar.model.dto;

import future.phase2.offlinetoonlinebazaar.validator.EmailFormatConstraint;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @NotNull
    @NotEmpty
    @EmailFormatConstraint
    private String email;

}
