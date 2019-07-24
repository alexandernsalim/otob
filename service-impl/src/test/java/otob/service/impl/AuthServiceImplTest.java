package otob.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import otob.service.api.RoleAccessService;
import otob.service.api.UserService;

import static org.mockito.MockitoAnnotations.initMocks;

public class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleAccessService roleAccessService;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private MockHttpServletRequest request;

    @Before
    public void setUp(){
        initMocks(this);

        request = new MockHttpServletRequest();
    }

    @Test
    public void isAuthorizedTest(){

    }


    @After
    public void teardown(){

    }

}
