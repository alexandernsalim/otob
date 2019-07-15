package otob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "otob")
public class OfflineToOnlineBazaarApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfflineToOnlineBazaarApplication.class, args);
	}

}
