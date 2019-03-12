package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUser();
    UserDto registerNewUser(UserDto user, String role);

}
