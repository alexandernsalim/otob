package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.request.UserRequest;
import future.phase2.offlinetoonlinebazaar.model.response.UserResponse;

public interface UserService {

    UserResponse registerNewUser(UserRequest user);

}
