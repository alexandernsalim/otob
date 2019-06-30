package otob.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import otob.service.EmailService;

public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender mailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
        }catch(MailException exception){
            exception.printStackTrace();
        }
    }

}
