package otob.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import otob.constant.path.RoleApiPath;
import otob.entity.Role;
import otob.exception.GlobalExceptionHandler;
import otob.mapper.BeanMapper;
import otob.service.api.RoleService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @Mock
    private BeanMapper mapper;

    @InjectMocks
    private RoleController roleController;

    private MockMvc mockMvc;
    private Role role;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        role = Role.builder()
                .roleId(1L)
                .name(otob.constant.Role.GUEST)
                .build();

    }

    @Test
    public void getRoleByName() throws Exception {
        when(roleService.getRoleByName(otob.constant.Role.GUEST))
                .thenReturn(role);

        mockMvc.perform(
            post(RoleApiPath.BASE_PATH + RoleApiPath.GET_ROLE_BY_NAME)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("roleName", "ROLE_GUEST")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(role));

        verify(roleService).getRoleByName(otob.constant.Role.GUEST);
    }

    @After
    public void tearDown() {

    }

}
