package otob.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {

    private String host;
    private String username;
    private String password;
    private String database;
    private int port;

}
