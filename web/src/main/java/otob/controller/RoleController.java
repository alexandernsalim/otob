package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.constant.path.RoleApiPath;
import otob.dto.RoleDto;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.api.RoleService;

@RestController
@RequestMapping(RoleApiPath.BASE_PATH)
public class RoleController extends GlobalController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping(RoleApiPath.GET_ROLE_BY_NAME)
    public Response<RoleDto> getRoleByName(@PathVariable String roleName) {
        return toResponse(mapper.map(roleService.getRoleByName(roleName), RoleDto.class));
    }

    @PostMapping
    public Response<RoleDto> addRole(@RequestBody RoleDto roleDto) {
        return toResponse(mapper.map(roleService.addRole(roleDto), RoleDto.class));
    }

}
