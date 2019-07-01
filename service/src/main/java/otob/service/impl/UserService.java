package otob.service.impl;

import otob.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();
    User registerNewUser(User user, String role);
    Boolean checkUser(String email);
    Boolean removeUser(String email);
    String forgetPassword(String email);

}
