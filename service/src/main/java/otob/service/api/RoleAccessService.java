package otob.service.api;

import java.util.List;

public interface RoleAccessService {

    List<String> getAccessByRole(String role);

}
