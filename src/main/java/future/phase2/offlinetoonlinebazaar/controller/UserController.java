package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.model.entity.User;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.model.dto.UserDto;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController extends GlobalController{

    @Autowired
    private UserService userService;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @GetMapping
    public Response<List<UserDto>> getAllUser(){
        return toResponse(convertToListDto(userService.getAllUser()));
    }

    @PostMapping("/register/customer")
    public Response<User> registerNewCustomer(@RequestBody @Valid UserDto userDto){
        User user = convertToEntity(userDto);

        return toResponse(convertToDto(userService.registerNewUser(user, "ROLE_CUSTOMER")));
    }

    @PostMapping("/register/admin")
    public Response<UserDto> registerNewAdmin(@RequestBody @Valid UserDto userDto){
        User user = convertToEntity(userDto);

        return toResponse(convertToDto(userService.registerNewUser(user, "ROLE_ADMIN")));
    }

    @PostMapping("/register/cashier")
    public Response<UserDto> registerNewCashier(@RequestBody @Valid UserDto userDto){
        User user = convertToEntity(userDto);

        return toResponse(convertToDto(userService.registerNewUser(user, "ROLE_CASHIER")));
    }

    @DeleteMapping("/delete/{email}")
    public Response<Boolean> removeUser(@PathVariable String email){
        return toResponse(userService.removeUser(email));
    }


    //Private
    private User convertToEntity(UserDto userDto){
        mapperFactory.classMap(UserDto.class, User.class).byDefault();

        MapperFacade mapper = mapperFactory.getMapperFacade();
        User user = mapper.map(userDto, User.class);

        return user;
    }

    private List<UserDto> convertToListDto(List<User> users){
        MapperFacade mapper = mapperFactory.getMapperFacade();
        List<UserDto> userDtos = mapper.mapAsList(users, UserDto.class);

        return userDtos;
    }

    private UserDto convertToDto(User user) {
        mapperFactory.classMap(User.class, UserDto.class)
                .exclude("password").exclude("id")
                .exclude("roles").register();

        MapperFacade mapper = mapperFactory.getMapperFacade();
        UserDto userDto = mapper.map(user, UserDto.class);

        return userDto;
    }

}
