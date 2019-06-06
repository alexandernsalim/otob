package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.exception.ResourceNotFoundException;
import future.phase2.offlinetoonlinebazaar.generator.IdGenerator;
import future.phase2.offlinetoonlinebazaar.model.dto.RoleDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Role;
import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.repository.RoleRepository;
import future.phase2.offlinetoonlinebazaar.service.RoleService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    IdGenerator idGenerator;

    @Autowired
    RoleRepository roleRepository;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public RoleDto addRole(RoleDto roleRequest) {
        try {
            Role role = new Role();

            role.setName(roleRequest.getName());
            role.setRoleId(idGenerator.getNextId("roleid"));
            roleRepository.save(role);

            mapperFactory.classMap(Role.class, RoleDto.class)
                    .exclude("id")
                    .byDefault().register();
            MapperFacade mapper = mapperFactory.getMapperFacade();
            RoleDto roleDto = mapper.map(role, RoleDto.class);

            return roleDto;
        }catch(Exception e){
            throw new ResourceNotFoundException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }
    }

}
