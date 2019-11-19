package otob.web.config.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class JwtConfig {

    @Value("${security.jwt.uri:/api/auth/**}")
    private String Uri;

    @Value("${security.jwt.header:authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:BlibliBazaarSecret}")
    private String secret;

}
