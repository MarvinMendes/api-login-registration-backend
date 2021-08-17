package com.marvin.apiuserregisteremailsender.email;

import com.marvin.apiuserregisteremailsender.exceptions.EmailFailedException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements  EmailSender{

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;


    @Async
    @Override
    public void sendEmail(String to, String email) throws EmailFailedException {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email.");
            helper.setFrom("marvin.lopes@sga.pucminas.br");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email", e);
            throw new EmailFailedException("Failed to send email");
        }

    }
}
