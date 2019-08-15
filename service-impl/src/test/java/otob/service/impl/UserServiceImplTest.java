package otob.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import otob.model.entity.Role;
import otob.model.entity.User;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.repository.UserRepository;
import otob.service.CartService;
import otob.service.EmailService;
import otob.service.RoleService;
import otob.util.generator.RandomTextGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
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
    private User userRequest;
    private String password;
    private String encodedPassword;
    private List<User> users;
    private Role roleCustomer;
    private List<Role> roles;
    private String subject;
    private String text;

    @Before
    public void setUp() {
        initMocks(this);

        roleCustomer = Role.builder()
                .roleId(1L)
                .name("ROLE_CUSTOMER")
                .build();

        roles = new ArrayList<>();
        roles.add(roleCustomer);

        password = "80YUMi*C&s";
        encodedPassword = "$2a$10$QxE04MupekPQ4QNcrEoID.qtIawZmQIB6pKz943hgpcNZNVChQ9cq";

        user1 = User.builder()
                .email("user1@mail.com")
                .password(password)
                .roles(roles)
                .build();

        userRequest = User.builder()
                .email("user1@mail.com")
                .password(encodedPassword)
                .roles(roles)
                .build();

        users = new ArrayList<>();
        users.add(user1);

        subject = "Login Password";
        text = "This message contains your password to login into the system.\n" +
                "Please don't share this password to anyone.\n" +
                "80YUMi*C&s";
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
        when(userRepository.existsByEmail(user1.getEmail()))
                .thenReturn(true);
        when(userRepository.findByEmail(user1.getEmail()))
                .thenReturn(user1);

        User result = userServiceImpl.getUserByEmail(user1.getEmail());

        verify(userRepository).existsByEmail(user1.getEmail());
        verify(userRepository).findByEmail(user1.getEmail());
        assertEquals(user1, result);
    }

    @Test
    public void getUserByEmailNotFoundTest() {
        when(userRepository.existsByEmail(user1.getEmail()))
                .thenReturn(false);

        try {
            userServiceImpl.getUserByEmail(user1.getEmail());
        } catch (CustomException ex) {
            verify(userRepository).existsByEmail(user1.getEmail());
            assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void checkUserTest() {
        when(userRepository.existsByEmail(user1.getEmail()))
                .thenReturn(true);

        boolean result = userServiceImpl.checkUser(user1.getEmail());

        verify(userRepository).existsByEmail(user1.getEmail());
        assertTrue(result);
    }

    @Test
    public void registerNewUserTest() {
        when(userRepository.existsByEmail(user1.getEmail()))
                .thenReturn(false);
        when(textGenerator.generateRandomPassword())
                .thenReturn(password);
        when(encoder.encode(password))
                .thenReturn(encodedPassword);
        when(roleService.getRoleByName(otob.model.constant.Role.CUSTOMER))
                .thenReturn(roleCustomer);

        User result = userServiceImpl.registerNewUser(user1, otob.model.constant.Role.CUSTOMER);

        verify(userRepository).existsByEmail(user1.getEmail());
        verify(textGenerator).generateRandomPassword();
        verify(encoder).encode(user1.getPassword());
        verify(roleService).getRoleByName(otob.model.constant.Role.CUSTOMER);
        verify(userRepository).save(userRequest);
        verify(emailService).sendSimpleMessage(user1.getEmail(), subject, text);
        assertEquals(user1.getEmail(), result.getEmail());
    }

    @Test
    public void registerNewUserExistsTest() {
        when(userRepository.existsByEmail(user1.getEmail()))
                .thenReturn(true);

        try {
            userServiceImpl.registerNewUser(user1, otob.model.constant.Role.CUSTOMER);
        } catch (CustomException ex) {
            verify(userRepository).existsByEmail(user1.getEmail());
            assertEquals(ErrorCode.EMAIL_EXISTS.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void removeUserTest() {
        when(userRepository.existsByEmail(user1.getEmail()))
                .thenReturn(true);
        when(userRepository.deleteByEmail(user1.getEmail()))
                .thenReturn(1L);
        when(cartService.removeUserCart(user1.getEmail()))
                .thenReturn(Boolean.TRUE);

        Boolean result = userServiceImpl.removeUser(user1.getEmail());

        verify(userRepository).existsByEmail(user1.getEmail());
        verify(userRepository).deleteByEmail(user1.getEmail());
        verify(cartService).removeUserCart(user1.getEmail());
        assertTrue(result);
    }

    @Test
    public void removeUserNotFoundTest() {
        when(userRepository.existsByEmail(user1.getEmail()))
                .thenReturn(false);

        try {
            userServiceImpl.removeUser(user1.getEmail());
        } catch (CustomException ex) {
            verify(userRepository).existsByEmail(user1.getEmail());
            assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(roleService);
        verifyNoMoreInteractions(emailService);
        verifyNoMoreInteractions(cartService);
        verifyNoMoreInteractions(encoder);
        verifyNoMoreInteractions(textGenerator);
    }

}
