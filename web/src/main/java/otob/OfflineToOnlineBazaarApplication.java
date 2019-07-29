package otob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "otob")
public class OfflineToOnlineBazaarApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfflineToOnlineBazaarApplication.class, args);
	}

}
