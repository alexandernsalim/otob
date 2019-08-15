package otob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import otob.model.properties.MongoProperties;

@SpringBootApplication(scanBasePackages = "otob")
@EnableConfigurationProperties(MongoProperties.class)
public class OfflineToOnlineBazaarApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfflineToOnlineBazaarApplication.class, args);
	}

}
