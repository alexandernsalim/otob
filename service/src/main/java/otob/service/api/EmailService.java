package otob.service.api;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

}
