package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.response.Response;
import otob.service.api.AuthService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends GlobalController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Response<String> login(
        HttpServletRequest request,
        @RequestParam("email") String email,
        @RequestParam("password") String password){

        String response;

        if(authService.login(email, password)){
            request.getSession().setAttribute("user-id", email);

            response = "Login Success";
        }else{
            response = "Login Failed";
        }

        return toResponse(response);
    }

    @PostMapping("/logout")
    private Response<String> logout(HttpServletRequest request){
        request.getSession().invalidate();

        return toResponse("Logout Success");
    }

    @GetMapping("/logged/user")
    public Response<String> getLoggedUser(HttpServletRequest request){
        Object user = request.getSession().getAttribute("user-id");

        return toResponse((user != null) ? user.toString() : "No logged user");
    }


}
