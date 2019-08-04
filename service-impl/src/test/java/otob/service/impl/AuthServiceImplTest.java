package otob.service.impl;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import otob.model.constant.Role;
import otob.model.constant.Status;
import otob.model.constant.path.AuthApiPath;
import otob.model.entity.User;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.service.RoleAccessService;
import otob.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;
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
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

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
    public void loginSuccessTest() {
        when(userService.checkUser(user.getEmail()))
                .thenReturn(true);
        when(userService.getUserByEmail(user.getEmail()))
                .thenReturn(user);
        when(encoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);

        HttpStatus result = authServiceImpl.login(request, response, user.getEmail(), user.getPassword());

        verify(userService).checkUser(user.getEmail());
        verify(userService, times(2)).getUserByEmail(user.getEmail());
        verify(encoder).matches(user.getPassword(), user.getPassword());
        verify(session).setAttribute("userId", user.getEmail());
        verify(session).setAttribute("role", user.getRoles().get(0).getName());
        verify(session).setAttribute("isLogin", Status.LOGIN_TRUE);
        verify(response, times(3)).addCookie(cookieCaptor.capture());
        assertEquals(HttpStatus.ACCEPTED, result);
    }

    @Test
    public void loginFailTest() {
        when(userService.checkUser(user.getEmail()))
                .thenReturn(true);
        when(userService.getUserByEmail(user.getEmail()))
                .thenReturn(user);
        when(encoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(false);
        when(session.getAttribute("userId"))
                .thenReturn(UUID.randomUUID().toString());

        HttpStatus result = authServiceImpl.login(request, response, user.getEmail(), user.getPassword());

        verify(userService).checkUser(user.getEmail());
        verify(userService, times(2)).getUserByEmail(user.getEmail());
        verify(encoder).matches(user.getPassword(), user.getPassword());
        verify(session).getAttribute("userId");
        verify(response, times(3)).addCookie(cookieCaptor.capture());
        assertEquals(HttpStatus.UNAUTHORIZED, result);
    }

    @Test
    public void loginUserNotFoundTest() {
        when(userService.checkUser(user.getEmail()))
                .thenReturn(false);

        try {
            authServiceImpl.login(request, response, user.getEmail(), user.getPassword());
        } catch (CustomException ex) {
            verify(userService).checkUser(user.getEmail());
            TestCase.assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void logoutTest() {
        ArgumentCaptor<Cookie> captor = ArgumentCaptor.forClass(Cookie.class);

        when(request.getSession(true))
            .thenReturn(session);

        HttpStatus result = authServiceImpl.logout(request, response);

        verify(request).getSession(true);
        verify(session).invalidate();
        verify(response, times(3)).addCookie(captor.capture());
        assertEquals(HttpStatus.OK, result);
    }

    @Test
    public void isAuthenticatedTest() {
        session.setAttribute("isLogin", Status.LOGIN_TRUE);

        when(request.getSession(true))
            .thenReturn(session);
        when(session.getAttribute("isLogin"))
            .thenReturn(Status.LOGIN_TRUE);

        boolean result = authServiceImpl.isAuthenticated(request);

        verify(request).getSession(true);
        verify(session).setAttribute("isLogin", Status.LOGIN_TRUE);
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

        verify(request).getSession(true);
        verify(session).setAttribute("isLogin", Status.LOGIN_FALSE);
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
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(roleAccessService);
        verifyNoMoreInteractions(encoder);
        verifyNoMoreInteractions(request);
        verifyNoMoreInteractions(response);
        verifyNoMoreInteractions(session);
    }

//    private Cookie catchCookie() {
//        ArgumentCaptor<Cookie> captor = ArgumentCaptor.forClass(Cookie.class);
//
//    }

}
