package otob.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.model.constant.Role;
import otob.model.constant.path.UserApiPath;
import otob.web.model.UserDto;
import otob.model.entity.User;
import otob.util.mapper.BeanMapper;
import otob.model.response.Response;
import otob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(UserApiPath.BASE_PATH)
public class UserController extends GlobalController {

    @Autowired
    private UserService userService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<UserDto>> getAllUser() {

        return toResponse(mapper.mapAsList(userService.getAllUser(), UserDto.class));
    }

    @PostMapping(UserApiPath.REGISTER_ADMIN)
    public Response<UserDto> registerNewAdmin(@RequestBody @Valid UserDto userDto) {
        User user = mapper.map(userDto, User.class);

        return toResponse(mapper.map(userService.registerNewUser(user, Role.ADMIN), UserDto.class));
    }

    @PostMapping(UserApiPath.REGISTER_CASHIER)
    public Response<UserDto> registerNewCashier(@RequestBody @Valid UserDto userDto) {
        User user = mapper.map(userDto, User.class);

        return toResponse(mapper.map(userService.registerNewUser(user, Role.CASHIER), UserDto.class));
    }

    @PostMapping(UserApiPath.REGISTER_CUSTOMER)
    public Response<UserDto> registerNewCustomer(@RequestBody @Valid UserDto userDto) {
        User user = mapper.map(userDto, User.class);

        return toResponse(mapper.map(userService.registerNewUser(user, Role.CUSTOMER), UserDto.class));
    }

    @PutMapping(UserApiPath.CHANGE_PASSWORD)
    public Response<Boolean> changePassword(
            HttpServletRequest request,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        HttpSession session = request.getSession(true);
        String email = session.getAttribute("userId").toString();

        return toResponse(userService.changePassword(email, oldPassword, newPassword));
    }

    @DeleteMapping(UserApiPath.DELETE_USER)
    public Response<Boolean> removeUser(@PathVariable String email) {
        return toResponse(userService.removeUser(email));
    }

}
