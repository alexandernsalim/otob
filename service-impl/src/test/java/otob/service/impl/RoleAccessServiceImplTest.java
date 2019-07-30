package otob.service.impl;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import otob.model.entity.RoleAccess;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.repository.RoleAccessRepository;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RoleAccessServiceImplTest {

    @Mock
    private RoleAccessRepository roleAccessRepository;

    @InjectMocks
    private RoleAccessServiceImpl roleAccessServiceImpl;

    private String roleExists;
    private String roleNotExists;
    private List<String> paths;
    private RoleAccess eligibleAccess;

    @Before
    public void setUp() {
        initMocks(this);

        roleExists = "ROLE_GUEST";
        roleNotExists = "ROLE_USER";
        paths = Arrays.asList(
                "/api/auth/login",
                "/api/products",
                "/api/products/id",
                "/api/products/name"
        );
        eligibleAccess = RoleAccess.builder()
                .eligibleAccess(paths)
                .build();
    }

    @Test
    public void getAccessByRoleTest() {
        when(roleAccessRepository.existsById(roleExists))
                .thenReturn(true);
        when(roleAccessRepository.findByIdEquals(roleExists))
                .thenReturn(eligibleAccess);

        List<String> result = roleAccessServiceImpl.getAccessByRole(roleExists);

        verify(roleAccessRepository).existsById(roleExists);
        verify(roleAccessRepository).findByIdEquals(roleExists);
        assertTrue(result.contains("/api/auth/login"));
        assertTrue(result.contains("/api/products"));
        assertTrue(result.contains("/api/products/id"));
        assertTrue(result.contains("/api/products/name"));
        assertTrue(result.size() == 4);
    }

    @Test
    public void getAccessByRoleNotFoundTest() {
        when(roleAccessRepository.existsById(roleNotExists))
                .thenReturn(false);

        try {
            roleAccessServiceImpl.getAccessByRole(roleNotExists);
        } catch (CustomException ex) {
            verify(roleAccessRepository).existsById(roleNotExists);
            TestCase.assertEquals(ErrorCode.BAD_REQUEST.getCode(), ex.getErrorCode());
            assertEquals(ErrorCode.BAD_REQUEST.getMessage(), ex.getMessage());
        }
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(roleAccessRepository);
    }

}
