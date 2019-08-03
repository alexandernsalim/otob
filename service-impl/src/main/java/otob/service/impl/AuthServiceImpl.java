package otob.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import otob.model.constant.Role;
import otob.model.constant.Status;
import otob.model.entity.User;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.service.AuthService;
import otob.service.RoleAccessService;
import otob.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleAccessService roleAccessService;

    @Autowired
    private PasswordEncoder encoder;

    private HttpSession session;
    private static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public HttpStatus login(HttpServletRequest request, HttpServletResponse response, String email, String password) {
        if (!userService.checkUser(email)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        User user = userService.getUserByEmail(email);
        String dbPassword = user.getPassword();

        boolean authenticated = encoder.matches(password, dbPassword);

        if (authenticated) {
            session.setAttribute("userId", email);
            session.setAttribute("role", user.getRoles().get(0).getName());
            session.setAttribute("isLogin", Status.LOGIN_TRUE);
        }

        setCookie(session, response, email, authenticated);

        return authenticated ? HttpStatus.ACCEPTED : HttpStatus.UNAUTHORIZED;
    }

    @Override
    public HttpStatus logout(HttpServletRequest request, HttpServletResponse response) {
        session = request.getSession(true);

        Cookie userId = new Cookie("user-id", "");
        userId.setMaxAge(0);

        Cookie userRole = new Cookie("user-role", "");
        userRole.setMaxAge(0);

        Cookie isLogin = new Cookie("is-login", "");
        isLogin.setMaxAge(0);

        session.invalidate();
        response.addCookie(userId);
        response.addCookie(userRole);
        response.addCookie(isLogin);

        logger.info("Session invalidated");

        return HttpStatus.OK;
    }

    @Override
    public boolean isAuthenticated(HttpServletRequest request) {
        session = request.getSession(true);

        return session.getAttribute("isLogin").equals(Status.LOGIN_TRUE);
    }

    @Override
    public boolean isAuthorized(HttpServletRequest request) {
        session = request.getSession(true);
        List<String> access = roleAccessService.getAccessByRole(session.getAttribute("role").toString());

        return access.stream().anyMatch(s -> {
            Pattern pattern = Pattern.compile(s);
            Matcher matcher = pattern.matcher(request.getServletPath());

            return matcher.matches();
        });
    }

    private void setCookie(HttpSession session, HttpServletResponse response, String email, boolean authenticated) {
        User user = userService.getUserByEmail(email);
        List<String> roleList = new ArrayList<>(Arrays.asList("ROLE_ADMIN", "ROLE_CASHIER", "ROLE_CUSTOMER"));

        Cookie userId = new Cookie("user-id", authenticated ? email : session.getAttribute("userId").toString());
        userId.setHttpOnly(false);
        userId.setSecure(false);
        userId.setPath("/");
        userId.setMaxAge(3600);

        Cookie userRole = new Cookie("user-role", authenticated ?
                String.valueOf(roleList.indexOf(user.getRoles().get(0).getName())+1) : "4");
        userRole.setHttpOnly(false);
        userRole.setSecure(false);
        userRole.setPath("/");
        userRole.setMaxAge(3600);

        Cookie isLogin = new Cookie("is-login", authenticated ? Status.LOGIN_TRUE : Status.LOGIN_FALSE);
        isLogin.setHttpOnly(false);
        isLogin.setSecure(false);
        isLogin.setPath("/");
        isLogin.setMaxAge(3600);

        response.addCookie(userId);
        response.addCookie(userRole);
        response.addCookie(isLogin);
    }

}
