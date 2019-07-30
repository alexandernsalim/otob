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
import otob.model.constant.path.RoleApiPath;
import otob.model.entity.Role;
import otob.model.exception.GlobalExceptionHandler;
import otob.service.RoleService;
import otob.web.model.RoleDto;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private Role role;
    private RoleDto roleRequest;
    private RoleDto roleResponse;

    @Before
    public void setUp() {
        initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(roleController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        role = otob.model.entity.Role.builder()
                .roleId(1L)
                .name(otob.model.constant.Role.GUEST)
                .build();

        roleRequest = RoleDto.builder()
                .name(otob.model.constant.Role.GUEST)
                .build();

        roleResponse = RoleDto.builder()
                .roleId(1L)
                .name(otob.model.constant.Role.GUEST)
                .build();

    }

    @Test
    public void getRoleByName() throws Exception {
        when(roleService.getRoleByName(otob.model.constant.Role.GUEST))
            .thenReturn(role);

        mvc.perform(
            get(RoleApiPath.BASE_PATH + RoleApiPath.GET_ROLE_BY_NAME, "ROLE_GUEST")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(role));

        verify(roleService).getRoleByName(otob.model.constant.Role.GUEST);
    }

    @Test
    public void addRoleTest() throws Exception {
        when(roleService.addRole(roleRequest))
            .thenReturn(role);

        mvc.perform(
            post(RoleApiPath.BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleRequest))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(roleResponse));

        verify(roleService).addRole(roleRequest);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(roleService);
    }

}
