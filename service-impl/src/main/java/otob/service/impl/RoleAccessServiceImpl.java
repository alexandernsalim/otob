package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otob.model.entity.RoleAccess;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.service.RoleAccessService;
import otob.repository.RoleAccessRepository;

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
