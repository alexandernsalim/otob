package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import otob.entity.User;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.service.api.AuthService;
import otob.service.api.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public boolean login(String email, String password) {
        if(userService.checkUser(email).equals(Boolean.FALSE)) {
            throw new CustomException(
                ErrorCode.USER_NOT_FOUND.getCode()
                , ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        User user = userService.getUserByEmail(email);
        String dbPassword = user.getPassword();

        return encoder.matches(password, dbPassword);
    }

}
