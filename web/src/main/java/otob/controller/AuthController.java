package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.enumerator.Status;
import otob.generator.RandomTextGenerator;
import otob.response.Response;
import otob.service.api.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends GlobalController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RandomTextGenerator textGenerator;

    private HttpSession session;

    @PostMapping("/login")
    public Response<String> login(
        HttpServletRequest request,
        @RequestParam("email") String email,
        @RequestParam("password") String password){

        String response;
        session = request.getSession();

        if(!isAuthorized(request)){
            if(authService.login(email, password)){
                session.setAttribute("userId", email);
                session.setAttribute("isLogin", Status.LOGIN_TRUE.getStatus());

                response = "Login Success";
            }else{
                response = "Login Failed";
            }
        }else{
            response = "Already Logged In";
        }

        return toResponse(response);
    }

    @PostMapping("/logout")
    private Response<String> logout(HttpServletRequest request){
        String response;

        session = request.getSession();

        if(!isAuthorized(request)){
            response = "Not Logged In";
        }else{
            session.setAttribute("userId", textGenerator.generateRandomUserId());
            session.setAttribute("isLogin", Status.LOGIN_FALSE.getStatus());
            response = "Logout Success";
        }

        return toResponse(response);
    }

    @GetMapping("/userId")
    public Response<String> getUserId(HttpServletRequest request){
        Object user = request.getSession().getAttribute("userId");

        return toResponse((user != null) ? user.toString() : "No logged user");
    }


}
