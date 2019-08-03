package otob.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otob.model.constant.path.AuthApiPath;
import otob.model.response.Response;
import otob.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(AuthApiPath.BASE_PATH)
public class AuthController extends GlobalController {

    @Autowired
    private AuthService authService;

    @PostMapping(AuthApiPath.LOGIN)
    public Response<String> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        return toResponse(authService.login(request, response, email, password).getReasonPhrase());
   }

    @PostMapping(AuthApiPath.LOGOUT)
    private Response<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return toResponse(authService.logout(request, response).getReasonPhrase());
    }

}
