package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import otob.model.entity.User;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.util.generator.RandomTextGenerator;
import otob.service.CartService;
import otob.service.EmailService;
import otob.service.RoleService;
import otob.service.UserService;
import otob.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CartService cartService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RandomTextGenerator textGenerator;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        if (!checkUser(email)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        return userRepository.findByEmail(email);
    }

    @Override
    public User registerNewUser(User userRequest, String role) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new CustomException(
                    ErrorCode.EMAIL_EXISTS.getCode(),
                    ErrorCode.EMAIL_EXISTS.getMessage()
            );
        }

        User user = new User();
        String password = textGenerator.generateRandomPassword();

        user.setEmail(userRequest.getEmail());
        user.setPassword(encoder.encode(password));
        user.setRoles(Arrays.asList(roleService.getRoleByName(role)));

        userRepository.save(user);

        String text = "This message contains your password to login into the system.\n";
        text += "Please don't share this password to anyone.\n" + password;

        emailService.sendSimpleMessage(userRequest.getEmail(), "Login Password", text);

        return user;
    }

    @Override
    public Boolean changePassword(String email, String oldPassword, String newPassword) {
        if(!userRepository.existsByEmail(email)){
            throw new CustomException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        User user = userRepository.findByEmail(email);

        if(encoder.matches(oldPassword, user.getPassword())){
            user.setPassword(encoder.encode(newPassword));

            userRepository.save(user);

            return true;
        }else{
            throw new CustomException(
                ErrorCode.PASSWORD_NOT_MATCH.getCode(),
                ErrorCode.PASSWORD_NOT_MATCH.getMessage()
            );
        }
    }

    @Override
    public Boolean checkUser(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean removeUser(String email) {
        if (!checkUser(email)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        return (userRepository.deleteByEmail(email) == 1 && cartService.removeUserCart(email)) ? Boolean.TRUE : Boolean.FALSE;
    }

}
