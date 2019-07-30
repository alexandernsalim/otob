package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import otob.model.constant.Status;
import otob.model.entity.User;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.service.RoleAccessService;
import otob.service.AuthService;
import otob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Override
    public boolean login(String email, String password) {
        if (!userService.checkUser(email)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        User user = userService.getUserByEmail(email);
        String dbPassword = user.getPassword();

        return encoder.matches(password, dbPassword);
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

}
