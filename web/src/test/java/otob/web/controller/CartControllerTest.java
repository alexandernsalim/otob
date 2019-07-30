package otob.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import otob.model.constant.path.CartApiPath;
import otob.model.exception.GlobalExceptionHandler;
import otob.service.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartController cartController;

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private String userId;

    @Before
    public void setUp() {
        initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        userId = "user@mail.com";

    }

    @Test
    public void getCartItemsTest() throws Exception {
        when(request.getSession())
            .thenReturn(session);
        when(session.getAttribute("userId"))
            .thenReturn(userId);

        mvc.perform(
            get(CartApiPath.BASE_PATH)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value());

        verify(request).getSession();
        verify(session).getAttribute("userid");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(cartService);
    }

}
