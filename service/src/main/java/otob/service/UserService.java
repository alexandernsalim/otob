package otob.service;

import otob.model.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();
    User getUserByEmail(String email);
    User registerNewUser(User user, String role);
    Boolean changePassword(String email, String oldPassword, String newPassword);
    Boolean checkUser(String email);
    Boolean removeUser(String email);

}
