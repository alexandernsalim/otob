package otob.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import otob.model.constant.path.UserApiPath;
import otob.model.entity.Role;
import otob.model.entity.User;
import otob.model.exception.GlobalExceptionHandler;
import otob.service.UserService;
import otob.util.mapper.BeanMapper;
import otob.web.model.PageableUserDto;
import otob.web.model.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private MockHttpServletRequest request;
    private MockHttpSession session;

    private Integer page;
    private Integer size;
    private Role roleAdmin;
    private Role roleCashier;
    private Role roleCustomer;
    private List<Role> roles;
    private User userReq;
    private User userAdmin;
    private User userCashier;
    private User userCustomer;
    private User userCustomerUpdated;
    private List<User> users;
    private PageableUserDto pageableUserDto;

    @Before
    public void setUp() {
        initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(userController)
                  .setControllerAdvice(new GlobalExceptionHandler())
                  .build();

        objectMapper = new ObjectMapper();

        request = new MockHttpServletRequest();
        session = new MockHttpSession();
        request.setSession(session);

        page = 0;
        size = 5;

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

        userCustomerUpdated = User.builder()
            .email("customer@mail.com")
            .password("newPasswd")
            .roles(roles)
            .build();

        users = new ArrayList<>();
        users.add(userAdmin);
        users.add(userCashier);
        users.add(userCustomer);

        pageableUserDto = PageableUserDto.builder()
            .totalPage(1)
            .users(BeanMapper.mapAsList(users, UserDto.class))
            .build();

    }

    @Test
    public void getAllUserTest() throws Exception {
        when(userService.getAllUser(page, size))
            .thenReturn(pageableUserDto);

        mvc.perform(
            get(UserApiPath.BASE_PATH)
                .param("page", "1")
                .param("size", "5")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.users").isArray())
        .andExpect(jsonPath("$.data.users", hasSize(3)));

        verify(userService).getAllUser(page, size);
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
    public void changeUserTest() throws Exception {
        when(userService.changePassword(userCustomer.getEmail(), userCustomer.getPassword(), userCustomerUpdated.getPassword()))
            .thenReturn(true);

        mvc.perform(
            put(UserApiPath.BASE_PATH + UserApiPath.CHANGE_PASSWORD)
                .param("oldPassword", userCustomer.getPassword())
                .param("newPassword", userCustomerUpdated.getPassword())
                .sessionAttr("userId", userCustomer.getEmail())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(true));

        verify(userService).changePassword(userCustomer.getEmail(), userCustomer.getPassword(), userCustomerUpdated.getPassword());
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
