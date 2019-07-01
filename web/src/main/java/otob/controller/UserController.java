package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.dto.UserDto;
import otob.entity.User;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.impl.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController extends GlobalController{

    @Autowired
    private UserService userService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<UserDto>> getAllUser(){
        return toResponse(mapper.mapAsList(userService.getAllUser(), UserDto.class));
    }

    @PostMapping("/register/customer")
    public Response<UserDto> registerNewCustomer(@RequestBody @Valid UserDto userDto){
        User user = mapper.map(userDto, User.class);

        return toResponse(mapper.map(userService.registerNewUser(user, "ROLE_CUSTOMER"), UserDto.class));
    }

    @PostMapping("/register/admin")
    public Response<UserDto> registerNewAdmin(@RequestBody @Valid UserDto userDto){
        User user = mapper.map(userDto, User.class);

        return toResponse(mapper.map(userService.registerNewUser(user, "ROLE_ADMIN"), UserDto.class));
    }

    @PostMapping("/register/cashier")
    public Response<UserDto> registerNewCashier(@RequestBody @Valid UserDto userDto){
        User user = mapper.map(userDto, User.class);

        return toResponse(mapper.map(userService.registerNewUser(user, "ROLE_CASHIER"), UserDto.class));
    }

    @DeleteMapping("/delete/{email}")
    public Response<Boolean> removeUser(@PathVariable String email){
        return toResponse(userService.removeUser(email));
    }

}
