package otob.service.api;

import otob.dto.RoleDto;
import otob.entity.Role;

public interface RoleService {

    Role getRoleByName(String name);
    Role addRole(RoleDto roleResponse);

}
