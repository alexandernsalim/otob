package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.model.dto.RoleDto;
import future.phase2.offlinetoonlinebazaar.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends GlobalController{

    @Autowired
    RoleService roleService;

    @GetMapping("/{name}")
    public Response<RoleDto> getRoleByName(@PathVariable String name){
        return toResponse(roleService.getRoleByName(name));
    }

    @PostMapping
    public Response<RoleDto> addRole(@RequestBody RoleDto roleDto){
        return toResponse(roleService.addRole(roleDto));
    }

}
