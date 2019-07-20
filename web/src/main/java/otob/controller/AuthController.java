package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.constant.Role;
import otob.constant.Status;
import otob.dto.AuthDto;
import otob.entity.User;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.generator.RandomTextGenerator;
import otob.response.Response;
import otob.service.api.AuthService;
import otob.service.api.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends GlobalController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private RandomTextGenerator textGenerator;

    private HttpSession session;

    @PostMapping("/login")
    public Response<String> login(
            HttpServletRequest request,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        AuthDto response;
        User user;
        session = request.getSession(false);

        if (!isAuthenticated(request)) {
            if (authService.login(email, password)) {
                user = userService.getUserByEmail(email);

                session.setAttribute("userId", email);
                session.setAttribute("role", user.getRoles().get(0).getName());
                session.setAttribute("isLogin", Status.LOGIN_TRUE);

                response = AuthDto.builder()
                        .userId(email)
                        .role(user.getRoles().get(0).getName())
                        .isLogin(true)
                        .build();
            } else {
                response = AuthDto.builder()
                        .userId(session.getAttribute("userId").toString())
                        .role(Role.GUEST)
                        .isLogin(false)
                        .build();
            }
        } else {
            throw new CustomException(
                    ErrorCode.LOGGED_IN.getCode(),
                    ErrorCode.LOGGED_IN.getMessage()
            );
        }

        return toResponse(response);
    }

    @PostMapping("/logout")
    private Response<String> logout(HttpServletRequest request) {
        String response;

        session = request.getSession();

        if (!isAuthenticated(request)) {
            response = "Not Logged In";
        } else {
            session.setAttribute("userId", textGenerator.generateRandomUserId());
            session.setAttribute("isLogin", Status.LOGIN_FALSE);
            response = "Logout Success";
        }

        return toResponse(response);
    }

    @GetMapping("/userId")
    public Response<String> getUserId(HttpServletRequest request) {
        Object user = request.getSession().getAttribute("userId");

        return toResponse((user != null) ? user.toString() : "No logged user");
    }


}
