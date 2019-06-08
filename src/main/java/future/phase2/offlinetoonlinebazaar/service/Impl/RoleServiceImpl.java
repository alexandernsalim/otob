package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.exception.ResourceNotFoundException;
import future.phase2.offlinetoonlinebazaar.generator.IdGenerator;
import future.phase2.offlinetoonlinebazaar.model.dto.RoleDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Role;
import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.repository.RoleRepository;
import future.phase2.offlinetoonlinebazaar.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new ResourceNotFoundException(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
            );
        }
    }

}
