package otob.service.impl;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailServiceImpl;

    private GreenMail greenMail;
    private String to;
    private String subject;
    private String text;
    private SimpleMailMessage message;

    @Before
    public void setUp(){
        initMocks(this);
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        to = "alexandernsalim@gmail.com";
        subject = "Test";
        text = "Test success";
        message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
    }


    @Test
    public void sendSimpleMessageTest() {
        emailServiceImpl.sendSimpleMessage(to, subject, text);

        verify(mailSender).send(message);
    }

    @After
    public void tearDown(){
        greenMail.stop();
        verifyNoMoreInteractions(mailSender);
    }

}
