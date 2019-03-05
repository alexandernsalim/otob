package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.model.entity.User;
import future.phase2.offlinetoonlinebazaar.model.request.UserRequest;
import future.phase2.offlinetoonlinebazaar.model.response.UserResponse;
import future.phase2.offlinetoonlinebazaar.repository.RoleRepository;
import future.phase2.offlinetoonlinebazaar.repository.UserRepository;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Override
    public UserResponse registerNewUser(UserRequest userRequest) {
        User user = new User();

        user.setEmail(userRequest.getEmail());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("USER")));
        userRepository.save(user);

        mapperFactory.classMap(UserRequest.class, UserResponse.class)
                .exclude("password")
                .byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        UserResponse userResponse = mapper.map(userRequest, UserResponse.class);

        return userResponse;
    }

}
