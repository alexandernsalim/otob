package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.generator.RandomPasswordGenerator;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.model.dto.UserDto;
import future.phase2.offlinetoonlinebazaar.service.EmailService;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController extends GlobalController{

    @Autowired
    private UserService userService;

//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private RandomPasswordGenerator passwordGenerator;

    @PostMapping("/register")
    public Response<UserDto> registerNewUser(@RequestBody UserDto userDto){
        return toResponse(userService.registerNewUser(userDto));
    }

//    @PostMapping("/send/email/{recipient}")
//    public void sendEmail(@PathVariable String recipient){
//        String text = "This message contains your password to login into the system.\n";
//        text += "Please don't share this password to anyone.\n";
//        text += passwordGenerator.generateRandomPassword();
//
//        emailService.sendSimpleMessage(recipient, "TEST", text);
//    }

}
