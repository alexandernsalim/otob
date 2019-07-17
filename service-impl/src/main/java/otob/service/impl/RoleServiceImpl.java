package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otob.dto.RoleDto;
import otob.entity.Role;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.generator.IdGenerator;
import otob.repository.RoleRepository;
import otob.service.api.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    IdGenerator idGenerator;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role addRole(RoleDto roleRequest) {
        try {
            Role role = new Role();

            role.setName(roleRequest.getName());
            role.setRoleId(idGenerator.getNextId("roleid"));

            return roleRepository.save(role);
        }catch(Exception e){
            throw new CustomException(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
            );
        }
    }

}
