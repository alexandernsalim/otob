package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.model.dto.UserDto;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController extends GlobalController{

    @Autowired
    private UserService userService;

    @GetMapping
    public Response<List<UserDto>> getAllUser(){
        return toResponse(userService.getAllUser());
    }

    @PostMapping("/register/user")
    public Response<UserDto> registerNewUser(@RequestBody UserDto userDto){
        return toResponse(userService.registerNewUser(userDto, "USER"));
    }

    @PostMapping("/register/admin")
    public Response<UserDto> registerNewAdmin(@RequestBody UserDto userDto){
        return toResponse(userService.registerNewUser(userDto, "ADMIN"));
    }

    @PostMapping("/register/cashier")
    public Response<UserDto> registerNewCashier(@RequestBody UserDto userDto){
        return toResponse(userService.registerNewUser(userDto, "CASHIER"));
    }

}
