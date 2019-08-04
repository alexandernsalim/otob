package otob.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(redisNamespace = "${spring.session.redis.namespace}")
public class SessionConfig {

    @Bean
    JedisConnectionFactory connectionFactory(){
        return new JedisConnectionFactory();
    }

}
