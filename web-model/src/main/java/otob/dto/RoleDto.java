package otob.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class RoleDto {

    private Long roleId;

    @NotNull
    private String name;

}
