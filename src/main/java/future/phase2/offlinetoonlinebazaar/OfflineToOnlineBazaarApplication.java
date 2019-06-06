package future.phase2.offlinetoonlinebazaar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class OfflineToOnlineBazaarApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfflineToOnlineBazaarApplication.class, args);
	}

	@Bean
	public JavaMailSender getJavaMailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("offlinetoonline.bazaar@gmail.com");
		mailSender.setPassword("OffToOnAdmin");

		Properties props = mailSender.getJavaMailProperties();

		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

}
