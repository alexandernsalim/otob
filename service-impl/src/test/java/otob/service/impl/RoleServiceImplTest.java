package otob.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import otob.dto.RoleDto;
import otob.entity.Role;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.generator.IdGenerator;
import otob.repository.RoleRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RoleServiceImplTest {

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    private String countersId;
    private String roleNameExists;
    private String roleNameNotExists;
    private Role role;
    private RoleDto roleRequest;

    @Before
    public void setUp() {
        initMocks(this);

        countersId = "roleid";
        roleNameExists = "ROLE_CUSTOMER";
        roleNameNotExists = "ROLE_USER";

        role = Role.builder()
                .roleId(3L)
                .name("ROLE_CUSTOMER")
                .build();
        roleRequest = RoleDto.builder()
                .name(roleNameExists)
                .build();

        when(roleRepository.save(role))
                .thenReturn(role);
    }

    @Test
    public void getRoleByNameTest() {
        when(roleRepository.findByName(roleNameExists))
                .thenReturn(role);

        Role result = roleServiceImpl.getRoleByName(roleNameExists);

        verify(roleRepository).findByName(roleNameExists);
        assertTrue(result.getRoleId().equals(3L));
        assertEquals(roleNameExists, result.getName());
    }

    @Test
    public void getRoleByNameNotFoundTest() {
        when(roleRepository.findByName(roleNameNotExists))
                .thenReturn(null);

        Role result = roleServiceImpl.getRoleByName(roleNameNotExists);

        verify(roleRepository).findByName(roleNameNotExists);
        assertTrue(result == null);
    }

    @Test
    public void addRoleTest() throws Exception {
        when(idGenerator.getNextId(countersId)).
                thenReturn(3L);

        Role result = roleServiceImpl.addRole(roleRequest);

        verify(idGenerator).getNextId(countersId);
        verify(roleRepository).save(role);
        assertTrue(result.getRoleId().equals(3L));
        assertEquals(roleNameExists, result.getName());
    }

    @Test
    public void addRoleFailTest() throws Exception {
        when(idGenerator.getNextId(countersId)).thenThrow(new Exception());

        try {
            roleServiceImpl.addRole(roleRequest);
        } catch (CustomException ex) {
            verify(idGenerator).getNextId(countersId);
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ex.getErrorCode());
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ex.getMessage());
        }
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(roleRepository);
        verifyNoMoreInteractions(idGenerator);
    }

}
