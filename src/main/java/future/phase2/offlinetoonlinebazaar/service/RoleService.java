package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.dto.RoleDto;

public interface RoleService {

    RoleDto getRoleByName(String name);
    RoleDto addRole(RoleDto roleResponse);

}
