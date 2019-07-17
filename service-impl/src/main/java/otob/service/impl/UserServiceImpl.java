package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import otob.entity.User;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.generator.RandomPasswordGenerator;
import otob.repository.UserRepository;
import otob.service.api.CartService;
import otob.service.api.EmailService;
import otob.service.api.RoleService;
import otob.service.api.UserService;

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
    private RandomPasswordGenerator passwordGenerator;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerNewUser(User userRequest, String role){
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new CustomException(
                ErrorCode.EMAIL_EXISTS.getCode(),
                ErrorCode.EMAIL_EXISTS.getMessage()
            );
        }

        User user = new User();
        String password = passwordGenerator.generateRandomPassword();

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
    public Boolean checkUser(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean removeUser(String email) {
        return (userRepository.deleteByEmail(email) == 1 && cartService.removeUserCart(email)) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public String forgetPassword(String email) {
        return null;
    }

}
