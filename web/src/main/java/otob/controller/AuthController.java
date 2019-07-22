package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otob.constant.Role;
import otob.constant.Status;
import otob.constant.path.AuthApiPath;
import otob.dto.AuthDto;
import otob.entity.User;
import otob.generator.RandomTextGenerator;
import otob.response.Response;
import otob.service.api.AuthService;
import otob.service.api.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(AuthApiPath.BASE_PATH)
public class AuthController extends GlobalController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private RandomTextGenerator textGenerator;

    private HttpSession session;

    @PostMapping(AuthApiPath.LOGIN)
    public Response<AuthDto> login(
            HttpServletRequest request,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        AuthDto response;
        User user;
        session = request.getSession(true);

        if (authService.login(email, password)) {
            user = userService.getUserByEmail(email);

            session.setAttribute("userId", email);
            session.setAttribute("role", user.getRoles().get(0).getName());
            session.setAttribute("isLogin", Status.LOGIN_TRUE);

            response = AuthDto.builder()
                    .userId(email)
                    .role(user.getRoles().get(0).getName())
                    .build();
        } else {
            response = AuthDto.builder()
                    .userId(session.getAttribute("userId").toString())
                    .role(Role.GUEST)
                    .build();
        }

        return toResponse(response);
    }

    @PostMapping(AuthApiPath.LOGOUT)
    private Response<String> logout(HttpServletRequest request) {
        session = request.getSession(true);

        session.setAttribute("userId", textGenerator.generateRandomUserId());
        session.setAttribute("role", Role.GUEST);
        session.setAttribute("isLogin", Status.LOGIN_FALSE);

        return toResponse(HttpStatus.OK);
    }

}
