package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.generator.RandomPasswordGenerator;
import future.phase2.offlinetoonlinebazaar.model.entity.User;
import future.phase2.offlinetoonlinebazaar.model.dto.UserDto;
import future.phase2.offlinetoonlinebazaar.repository.UserRepository;
import future.phase2.offlinetoonlinebazaar.service.EmailService;
import future.phase2.offlinetoonlinebazaar.service.RoleService;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
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

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Override
    public List<UserDto> getAllUser() {
        List<User> userList = userRepository.findAll();

        MapperFacade mapper = mapperFactory.getMapperFacade();
        List<UserDto> users = mapper.mapAsList(userList, UserDto.class);

        return users;
    }

    @Override
    public UserDto registerNewUser(UserDto userDto, String role) {
        User user = new User();
        String password =passwordGenerator.generateRandomPassword();

        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(password));
        user.setRoles(Arrays.asList(roleService.getRoleByName(role)));
        userRepository.save(user);

        String text = "This message contains your password to login into the system.\n";
        text += "Please don't share this password to anyone.\n" + password;

        emailService.sendSimpleMessage(userDto.getEmail(), "Login Password", text);

        return userDto;
    }

}
