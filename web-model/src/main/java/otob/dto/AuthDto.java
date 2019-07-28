package otob.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDto {

    private String userId;
    private boolean isLogin;
    private String role;

}