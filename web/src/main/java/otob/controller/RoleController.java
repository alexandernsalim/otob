package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.dto.RoleDto;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.api.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends GlobalController{

    @Autowired
    private RoleService roleService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping("/{name}")
    public Response<RoleDto> getRoleByName(@PathVariable String name){
        return toResponse(mapper.map(roleService.getRoleByName(name), RoleDto.class));
    }

    @PostMapping
    public Response<RoleDto> addRole(@RequestBody RoleDto roleDto){
        return toResponse(mapper.map(roleService.addRole(roleDto), RoleDto.class));
    }

}
