package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.dto.RoleDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Role;

public interface RoleService {

    Role getRoleByName(String name);
    Role addRole(RoleDto roleResponse);

}
