package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otob.entity.RoleAccess;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.repository.RoleAccessRepository;
import otob.service.api.RoleAccessService;

import java.util.List;

@Service
public class RoleAccessServiceImpl implements RoleAccessService {

    @Autowired
    private RoleAccessRepository roleAccessRepository;

    @Override
    public List<String> getAccessByRole(String role) {
        if (!roleAccessRepository.existsById(role)) {
            throw new CustomException(
                    ErrorCode.BAD_REQUEST.getCode(),
                    ErrorCode.BAD_REQUEST.getMessage()
            );
        }

        RoleAccess roleAccess = roleAccessRepository.findByIdEquals(role);

        return roleAccess.getEligibleAccess();
    }

}
