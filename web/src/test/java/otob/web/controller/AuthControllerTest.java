package otob.web.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import otob.model.constant.path.AuthApiPath;
import otob.model.entity.Role;
import otob.model.entity.User;
import otob.model.exception.GlobalExceptionHandler;
import otob.service.AuthService;
import otob.service.UserService;
import otob.util.generator.RandomTextGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private RandomTextGenerator textGenerator;

    @InjectMocks
    private AuthController authController;

    private MockMvc mvc;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;

    private String email;
    private String password;
    private User user;

    @Before
    public void setUp() {
        initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        request = new MockHttpServletRequest();
        session = new MockHttpSession();
        request.setSession(session);
        response = new MockHttpServletResponse();

        email = "user@gmail.com";
        password = "passwd";

        Role role = Role.builder()
                .name(otob.model.constant.Role.CUSTOMER)
                .build();

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        user = User.builder()
                .email(email)
                .password(password)
                .roles(roles)
                .build();

    }

    @Test
    public void loginSuccessTest() throws Exception {
        when(authService.login(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class), eq(email), eq(password)))
            .thenReturn(HttpStatus.ACCEPTED);

        mvc.perform(
            post(AuthApiPath.BASE_PATH + AuthApiPath.LOGIN)
                .param("email", email)
                .param("password", password)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(HttpStatus.ACCEPTED.getReasonPhrase()));

        verify(authService).login(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class), eq(email), eq(password));
    }

    @Test
    public void logoutTest() throws Exception {
        when(authService.logout(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class)))
                .thenReturn(HttpStatus.OK);

        mvc.perform(
            post(AuthApiPath.BASE_PATH + AuthApiPath.LOGOUT)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(HttpStatus.OK.getReasonPhrase()));

        verify(authService).logout(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(authService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(textGenerator);
    }

}
