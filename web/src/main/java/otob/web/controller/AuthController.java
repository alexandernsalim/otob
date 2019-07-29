package otob.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otob.model.constant.Role;
import otob.model.constant.Status;
import otob.model.constant.path.AuthApiPath;
import otob.model.entity.User;
import otob.util.generator.RandomTextGenerator;
import otob.model.response.Response;
import otob.service.AuthService;
import otob.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private HttpSession session;

    @PostMapping(AuthApiPath.LOGIN)
    public Response<String> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        User user;
        session = request.getSession(true);

        if (authService.login(email, password)) {
            user = userService.getUserByEmail(email);

            session.setAttribute("userId", email);
            session.setAttribute("role", user.getRoles().get(0).getName());
            session.setAttribute("isLogin", Status.LOGIN_TRUE);

            Cookie userId = new Cookie("user-id", email);
            userId.setHttpOnly(false);
            userId.setSecure(false);

            Cookie userRole = new Cookie("user-role", user.getRoles().get(0).getName());
            userRole.setHttpOnly(false);
            userRole.setSecure(false);

            response.addCookie(userId);
            response.addCookie(userRole);

            return toResponse(HttpStatus.ACCEPTED);
        } else {
            Cookie userId = new Cookie("user-id", textGenerator.generateRandomUserId());
            userId.setHttpOnly(false);
            userId.setSecure(false);


            Cookie userRole = new Cookie("user-role", Role.GUEST);
            userRole.setHttpOnly(false);
            userRole.setSecure(false);

            response.addCookie(userId);
            response.addCookie(userRole);

            return toResponse(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(AuthApiPath.LOGOUT)
    private Response<String> logout(HttpServletRequest request) {
        request.getSession(true).invalidate();
        logger.info("Session invalidated");

        return toResponse(HttpStatus.OK);
    }

}
