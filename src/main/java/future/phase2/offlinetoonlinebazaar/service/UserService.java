package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();
    User registerNewUser(User user, String role);
    Boolean removeUser(String email);

}
