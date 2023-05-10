package com.dipl.emailmodule.task;

import com.dipl.emailmodule.domain.EmailStatusEntity;
import com.dipl.emailmodule.repository.EmailStatusRepository;
import org.springframework.mail.javamail.JavaMailSender;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
public class SendEmailTask implements Runnable {

    private final JavaMailSender mailSender;
    private final MimeMessage message;
    private final EmailStatusRepository emailStatusRepository;
    private final EmailStatusEntity emailStatus;

    public SendEmailTask(JavaMailSender mailSender, MimeMessage message, EmailStatusRepository emailStatusRepository, EmailStatusEntity emailStatus) {
        this.mailSender = mailSender;
        this.message = message;
        this.emailStatusRepository = emailStatusRepository;
        this.emailStatus = emailStatus;
    }

    @Override
    public void run() {
        try {
            mailSender.send(message);
            System.out.println("Email sent successfully at " + LocalDateTime.now());
            emailStatus.setStatus("Successfully sent");
            emailStatusRepository.save(emailStatus);
        } catch (Exception e) {
            System.out.println("Error sending email at " + LocalDateTime.now() + " - " + e.getMessage());
            emailStatus.setStatus("Failed - " + e.getMessage());
            emailStatusRepository.save(emailStatus);
        }
    }
}