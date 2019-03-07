package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.dto.UserDto;

public interface UserService {

    UserDto registerNewUser(UserDto user);
    UserDto registerNewAdmin(UserDto user);

}
