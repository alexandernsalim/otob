package otob.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import otob.entity.Role;
import otob.entity.User;
import otob.generator.RandomTextGenerator;
import otob.repository.UserRepository;
import otob.service.api.CartService;
import otob.service.api.EmailService;
import otob.service.api.RoleService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private EmailService emailService;

    @Mock
    private CartService cartService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private RandomTextGenerator textGenerator;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user1;
    private List<User> users;
    private List<Role> roles;

    @Before
    public void setUp() {
        initMocks(this);

        Role roleCustomer = Role.builder()
                .roleId(1L)
                .name("ROLE_CUSTOMER")
                .build();

        roles = new ArrayList<>();
        roles.add(roleCustomer);

        user1 = User.builder()
                .email("user1@mail.com")
                .password("80YUMi*C&s")
                .roles(roles)
                .build();

        users = new ArrayList<>();
        users.add(user1);

    }

    @Test
    public void getAllUserTest() {
        when(userRepository.findAll())
                .thenReturn(users);

        List<User> result = userServiceImpl.getAllUser();

        verify(userRepository).findAll();
        assertEquals(users, result);
    }

    @Test
    public void getUserByEmailTest() {
        when(userRepository.findByEmail(user1.getEmail()))
                .thenReturn(user1);

        User result = userServiceImpl.getUserByEmail(user1.getEmail());

        verify(userRepository).findByEmail(user1.getEmail());
        assertEquals(user1, result);
    }

    @Test
    public void checkUserTest() {

    }

    @Test
    public void registerNewUserTest() {

    }

    @Test
    public void removeUserTest() {

    }

    @After
    public void teardown() {

    }

}
