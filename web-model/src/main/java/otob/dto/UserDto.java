package otob.dto;

import lombok.Data;
import otob.validator.EmailFormatConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @NotNull
    @NotEmpty
    @EmailFormatConstraint
    private String email;

}
