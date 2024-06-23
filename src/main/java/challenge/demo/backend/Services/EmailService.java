package challenge.demo.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("exesilveysre@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject("New Password");
        mailSender.send(message);
        System.out.println("Mail Send...");

    }

}