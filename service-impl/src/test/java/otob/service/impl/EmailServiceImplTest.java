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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class EmailServiceImplTest {



    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailServiceImpl;

    private GreenMail greenMail;
    private String from;
    private String to;
    private String subject;
    private String text;

    @Before
    public void setUp(){
        initMocks(this);
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        to = "alexandernsalim@gmail.com";
        subject = "Test";
        text = "Test success";
    }

    @Test
    public void sendSimpleMessageTest() throws MessagingException {
        emailServiceImpl.sendSimpleMessage(to, subject, text);

        Message[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("Test", messages[0].getSubject());

        String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");

        assertEquals("Test success", body);
    }

    @After
    public void teardown(){
        greenMail.stop();
        verifyNoMoreInteractions(mailSender);
    }

}
