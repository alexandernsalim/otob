package otob.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import otob.model.constant.path.UserApiPath;
import otob.model.entity.Role;
import otob.model.entity.User;
import otob.model.exception.GlobalExceptionHandler;
import otob.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private Role roleAdmin;
    private Role roleCashier;
    private Role roleCustomer;
    private List<Role> roles;
    private User userReq;
    private User userAdmin;
    private User userCashier;
    private User userCustomer;
    private List<User> users;

    @Before
    public void setUp() {
        initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(userController)
                  .setControllerAdvice(new GlobalExceptionHandler())
                  .build();

        objectMapper = new ObjectMapper();

        roleAdmin= Role.builder()
            .roleId(1L)
            .name(otob.model.constant.Role.ADMIN)
            .build();

        roleCashier= Role.builder()
            .roleId(2L)
            .name(otob.model.constant.Role.CASHIER)
            .build();

        roleCustomer = Role.builder()
            .roleId(3L)
            .name(otob.model.constant.Role.CUSTOMER)
            .build();

        roles = new ArrayList<>();

        userReq = User.builder()
            .build();

        userAdmin = User.builder()
            .email("admin@mail.com")
            .password("passwd")
            .roles(roles)
            .build();

        userCashier = User.builder()
            .email("cashier@mail.com")
            .password("passwd")
            .roles(roles)
            .build();

        userCustomer = User.builder()
            .email("customer@mail.com")
            .password("passwd")
            .roles(roles)
            .build();

        users = new ArrayList<>();
        users.add(userAdmin);
        users.add(userCashier);
        users.add(userCustomer);

    }

    @Test
    public void getAllUserTest() throws Exception {
        when(userService.getAllUser())
            .thenReturn(users);

        mvc.perform(
            get(UserApiPath.BASE_PATH)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(3)));

        verify(userService).getAllUser();
    }

    @Test
    public void registerNewAdminTest() throws Exception {
        userReq.setEmail("admin@mail.com");
        userAdmin.getRoles().add(roleAdmin);

        when(userService.registerNewUser(userReq, otob.model.constant.Role.ADMIN))
            .thenReturn(userAdmin);

        mvc.perform(
            post(UserApiPath.BASE_PATH + UserApiPath.REGISTER_ADMIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userReq))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(userReq));

        verify(userService).registerNewUser(userReq, otob.model.constant.Role.ADMIN);
    }

    @Test
    public void registerNewCashierTest() throws Exception {
        userReq.setEmail("cashier@mail.com");
        userCashier.getRoles().add(roleCashier);

        when(userService.registerNewUser(userReq, otob.model.constant.Role.CASHIER))
            .thenReturn(userCashier);

        mvc.perform(
            post(UserApiPath.BASE_PATH + UserApiPath.REGISTER_CASHIER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userReq))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(userReq));

        verify(userService).registerNewUser(userReq, otob.model.constant.Role.CASHIER);
    }

    @Test
    public void registerNewCustomerTest() throws Exception {
        userReq.setEmail("customer@mail.com");
        userCustomer.getRoles().add(roleCustomer);

        when(userService.registerNewUser(userReq, otob.model.constant.Role.CUSTOMER))
            .thenReturn(userCustomer);

        mvc.perform(
            post(UserApiPath.BASE_PATH + UserApiPath.REGISTER_CUSTOMER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userReq))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(userReq));

        verify(userService).registerNewUser(userReq, otob.model.constant.Role.CUSTOMER);
    }

    @Test
    public void removeUserTest() throws Exception {
        when(userService.removeUser(userCustomer.getEmail()))
            .thenReturn(true);

        mvc.perform(
            delete(UserApiPath.BASE_PATH + UserApiPath.DELETE_USER, userCustomer.getEmail())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(true));

        verify(userService).removeUser(userCustomer.getEmail());
    }

    @After
    public void tearDown() {
      verifyNoMoreInteractions(userService);
    }

}
