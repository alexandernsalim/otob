package otob.service.api;

import otob.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();
    User getUserByEmail(String email);
    User registerNewUser(User user, String role);
    Boolean checkUser(String email);
    Boolean removeUser(String email);

}
