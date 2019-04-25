package future.phase2.offlinetoonlinebazaar.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoleDto {

    private Long roleId;

    @NotNull
    private String name;

}
