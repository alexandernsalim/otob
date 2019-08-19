package otob.service;

import otob.model.entity.User;
import otob.web.model.PageableUserDto;

import java.util.List;

public interface UserService {

    PageableUserDto getAllUser(Integer page, Integer size);
    User getUserByEmail(String email);
    User registerNewUser(User user, String role);
    Boolean changePassword(String email, String oldPassword, String newPassword);
    Boolean checkUser(String email);
    Boolean removeUser(String email);

}
