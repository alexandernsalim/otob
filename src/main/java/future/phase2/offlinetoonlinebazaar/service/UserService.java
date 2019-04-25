package future.phase2.offlinetoonlinebazaar.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import future.phase2.offlinetoonlinebazaar.model.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();
    User registerNewUser(User user, String role);
    Boolean checkUser(String email);
    Boolean removeUser(String email);
    String forgetPassword(String email);

}
