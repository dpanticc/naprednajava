package com.dipl.emailmodule.service.impl;

import com.dipl.emailmodule.domain.EmailStatusEntity;
import com.dipl.emailmodule.repository.EmailStatusRepository;
import com.dipl.emailmodule.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Date;




@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;
    private final TaskScheduler taskScheduler;
    private final EmailStatusRepository emailStatusRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender mailSender, TaskScheduler taskScheduler,
                                  EmailStatusRepository emailStatusRepository) {
        this.mailSender = mailSender;
        this.taskScheduler = taskScheduler;
        this.emailStatusRepository = emailStatusRepository;

    }

    @Override
    public void sendEmail(String to, String subject, String message, String attachmentFilename) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        EmailStatusEntity emailStatus = new EmailStatusEntity(senderEmail, to, subject, message, attachmentFilename, "Pending", LocalDateTime.now());

        try{
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(message);

            if (attachmentFilename != null && !attachmentFilename.isEmpty()) {
                // add attachment
            File attachmentFile = new File(attachmentFilename);
                if (attachmentFile.exists()) {
                    FileSystemResource fileSystemResource = new FileSystemResource(attachmentFile);
                    messageHelper.addAttachment(attachmentFile.getName(), fileSystemResource);
                }else{
                    throw new FileNotFoundException(attachmentFilename);
                }
            }
            // send email
            taskScheduler.schedule(new SendEmailTask(mailSender, mimeMessage, emailStatusRepository, emailStatus), new Date(System.currentTimeMillis() + 5000));
            emailStatusRepository.save(emailStatus);

        } catch (FileNotFoundException e) {
            emailStatus.setStatus("Failed - attachment file not found");
            emailStatusRepository.save(emailStatus);
            System.out.println("Error sending email at " + LocalDateTime.now() + " - Attachment file not found");
        } catch (Exception e) {
            emailStatus.setStatus("Failed - " + e.getMessage());
            emailStatusRepository.save(emailStatus);
            System.out.println("Error sending email at " + LocalDateTime.now() + " - " + e.getMessage());
    }

    }
    private static class SendEmailTask implements Runnable {

        private final JavaMailSender mailSender;
        private final MimeMessage message;
        private final EmailStatusRepository emailStatusRepository;
        private final EmailStatusEntity emailStatus;
        public SendEmailTask(JavaMailSender mailSender, MimeMessage message,
                             EmailStatusRepository emailStatusRepository, EmailStatusEntity emailStatus) {
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
}
