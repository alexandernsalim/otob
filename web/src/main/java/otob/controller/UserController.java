package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.constant.UserApiPath;
import otob.dto.UserDto;
import otob.entity.User;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.api.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(UserApiPath.BASE_PATH)
public class UserController extends GlobalController {

    @Autowired
    private UserService userService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping(UserApiPath.GET_ALL_USER)
    public Response<List<UserDto>> getAllUser() {

        return toResponse(mapper.mapAsList(userService.getAllUser(), UserDto.class));
    }

    @PostMapping(UserApiPath.REGISTER_ADMIN)
    public Response<UserDto> registerNewAdmin(@RequestBody @Valid UserDto userDto) {
        User user = mapper.map(userDto, User.class);

        return toResponse(mapper.map(userService.registerNewUser(user, "ROLE_ADMIN"), UserDto.class));
    }

    @PostMapping(UserApiPath.REGISTER_CASHIER)
    public Response<UserDto> registerNewCashier(@RequestBody @Valid UserDto userDto) {
        User user = mapper.map(userDto, User.class);

        return toResponse(mapper.map(userService.registerNewUser(user, "ROLE_CASHIER"), UserDto.class));
    }

    @PostMapping(UserApiPath.REGISTER_CUSTOMER)
    public Response<UserDto> registerNewCustomer(@RequestBody @Valid UserDto userDto) {
        User user = mapper.map(userDto, User.class);

        return toResponse(mapper.map(userService.registerNewUser(user, "ROLE_CUSTOMER"), UserDto.class));
    }

    @DeleteMapping(UserApiPath.DELETE_USER)
    public Response<Boolean> removeUser(@PathVariable String email) {
        return toResponse(userService.removeUser(email));
    }

}
