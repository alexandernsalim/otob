package otob.service.impl;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import otob.model.constant.Role;
import otob.model.constant.Status;
import otob.model.constant.path.AuthApiPath;
import otob.model.entity.User;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.service.RoleAccessService;
import otob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleAccessService roleAccessService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private List<otob.model.entity.Role> roles;
    private List<String> roleAccess;
    private otob.model.entity.Role roleCustomer;
    private User user;

    @Before
    public void setUp() {
        initMocks(this);

        roleCustomer = otob.model.entity.Role.builder()
                .roleId(1L)
                .name("ROLE_CUSTOMER")
                .build();

        roleAccess = new ArrayList<>();
        roleAccess.add(AuthApiPath.BASE_PATH + AuthApiPath.LOGIN);

        roles = new ArrayList<>();
        roles.add(roleCustomer);

        user = User.builder()
                .email("user@mail.com")
                .password("80YUMi*C&s")
                .roles(roles)
                .build();
    }

    @Test
    public void loginTest() {
        when(userService.checkUser(user.getEmail()))
                .thenReturn(true);
        when(userService.getUserByEmail(user.getEmail()))
                .thenReturn(user);
        when(encoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);

        boolean result = authServiceImpl.login(user.getEmail(), user.getPassword());

        verify(userService).checkUser(user.getEmail());
        verify(userService).getUserByEmail(user.getEmail());
        verify(encoder).matches(user.getPassword(), user.getPassword());
        assertTrue(result);
    }

    @Test
    public void loginUserNotFoundTest() {
        when(userService.checkUser(user.getEmail()))
                .thenReturn(false);

        try {
            authServiceImpl.login(user.getEmail(), user.getPassword());
        } catch (CustomException ex) {
            verify(userService).checkUser(user.getEmail());
            TestCase.assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void isAuthenticatedTest() {
        session.setAttribute("isLogin", Status.LOGIN_TRUE);

        when(request.getSession(true))
                .thenReturn(session);
        when(session.getAttribute("isLogin"))
                .thenReturn(Status.LOGIN_TRUE);

        boolean result = authServiceImpl.isAuthenticated(request);

        verify(session).getAttribute("isLogin");
        assertTrue(result);
    }

    @Test
    public void isAuthenticatedFalseTest() {
        session.setAttribute("isLogin", Status.LOGIN_FALSE);

        when(request.getSession(true))
                .thenReturn(session);
        when(session.getAttribute("isLogin"))
                .thenReturn(Status.LOGIN_FALSE);

        boolean result = authServiceImpl.isAuthenticated(request);

        verify(session).getAttribute("isLogin");
        assertTrue(!result);
    }

    @Test
    public void isAuthorizedTest() {
        when(request.getSession(true))
                .thenReturn(session);
        when(session.getAttribute("role"))
                .thenReturn(Role.CUSTOMER);
        when(roleAccessService.getAccessByRole(Role.CUSTOMER))
                .thenReturn(roleAccess);
        when(request.getServletPath())
                .thenReturn(AuthApiPath.BASE_PATH + AuthApiPath.LOGIN);

        boolean result = authServiceImpl.isAuthorized(request);

        verify(request).getSession(true);
        verify(session).getAttribute("role");
        verify(roleAccessService).getAccessByRole(Role.CUSTOMER);
        verify(request).getServletPath();
        assertTrue(result);
    }

    @Test
    public void isAuthorizedFalseTest() {
        roleAccess.remove(0);
        roleAccess.add("/api/auth/logout");

        when(request.getSession(true))
                .thenReturn(session);
        when(session.getAttribute("role"))
                .thenReturn(Role.CUSTOMER);
        when(roleAccessService.getAccessByRole(Role.CUSTOMER))
                .thenReturn(roleAccess);
        when(request.getServletPath())
                .thenReturn(AuthApiPath.BASE_PATH + AuthApiPath.LOGIN);

        boolean result = authServiceImpl.isAuthorized(request);

        verify(request).getSession(true);
        verify(session).getAttribute("role");
        verify(roleAccessService).getAccessByRole(Role.CUSTOMER);
        verify(request).getServletPath();
        assertTrue(!result);
    }

    @After
    public void tearDown() {

    }

}
