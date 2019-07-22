package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import otob.constant.Role;
import otob.constant.Status;
import otob.entity.User;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.generator.RandomTextGenerator;
import otob.service.api.AuthService;
import otob.service.api.RoleAccessService;
import otob.service.api.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleAccessService roleAccessService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RandomTextGenerator textGenerator;

    @Override
    public boolean login(String email, String password) {
        if(userService.checkUser(email).equals(Boolean.FALSE)) {
            throw new CustomException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        User user = userService.getUserByEmail(email);
        String dbPassword = user.getPassword();

        return encoder.matches(password, dbPassword);
    }

    private HttpSession session;

    @Override
    public boolean isAuthenticated(HttpServletRequest request) {
        session = request.getSession(true);

        if (session.getAttribute("isLogin") != null &&
                session.getAttribute("isLogin").equals(Status.LOGIN_TRUE)) {

            return true;
        } else {
            if (session.getAttribute("userId") == null) {
                session.setAttribute("userId", textGenerator.generateRandomUserId());
                session.setAttribute("role", Role.GUEST);
                session.setAttribute("isLogin", Status.LOGIN_FALSE);
            }

            return false;
        }
    }

    @Override
    public boolean isAuthorized(HttpServletRequest request) {
        session = request.getSession(true);

        List<String> access = roleAccessService.getAccessByRole(session.getAttribute("role").toString());

        if (access.contains(request.getServletPath())) {
            return true;
        }

        return false;
    }

}
