package otob.service;

import otob.web.model.RoleDto;
import otob.model.entity.Role;

public interface RoleService {

    Role getRoleByName(String name);
    Role addRole(RoleDto roleResponse);

}
