package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.exception.EmailExistsException;
import future.phase2.offlinetoonlinebazaar.generator.RandomPasswordGenerator;
import future.phase2.offlinetoonlinebazaar.model.entity.User;
import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.repository.UserRepository;
import future.phase2.offlinetoonlinebazaar.service.EmailService;
import future.phase2.offlinetoonlinebazaar.service.RoleService;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private PasswordEncoder encoder;

    @Autowired
    private RandomPasswordGenerator passwordGenerator;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User registerNewUser(User userRequest, String role){
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new EmailExistsException(
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
    public Boolean removeUser(String email) {
        return (userRepository.deleteByEmail(email) == 1) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public String forgetPassword(String email) {
        return null;
    }

}
