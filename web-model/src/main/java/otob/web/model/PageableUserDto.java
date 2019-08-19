package otob.web.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageableUserDto {

    int totalPage;
    List<UserDto> users;

}
