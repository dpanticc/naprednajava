package com.dipl.emailmodule.service.impl;

import com.dipl.emailmodule.domain.EmailStatusEntity;
import com.dipl.emailmodule.repository.EmailStatusRepository;
import com.dipl.emailmodule.service.EmailSenderService;
import com.dipl.emailmodule.task.SendEmailTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private EmailStatusRepository emailStatusRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;
    @Autowired
    public EmailSenderServiceImpl(JavaMailSender mailSender, TaskScheduler taskScheduler, EmailStatusRepository emailStatusRepository) {
        this.mailSender = mailSender;
        this.taskScheduler = taskScheduler;
        this.emailStatusRepository = emailStatusRepository;
    }
    @Override
    public void sendEmail(List<String> toList, String subject, String message, String attachmentFilename) {

        for (String to : toList) {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            EmailStatusEntity emailStatus = new EmailStatusEntity(senderEmail, to, subject, message, attachmentFilename,
                    "Pending", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                messageHelper.setFrom(senderEmail);
                messageHelper.setSubject(subject);
                messageHelper.setText(message);

                //email recipient validation
                InternetAddress emailAddress = new InternetAddress(to);
                emailAddress.validate();
                messageHelper.setTo(to);

                //attachment validation and adding
                if (attachmentFilename != null && !attachmentFilename.isEmpty()) {
                    File attachmentFile = new File(attachmentFilename);
                    if (attachmentFile.exists()) {
                        FileSystemResource fileSystemResource = new FileSystemResource(attachmentFile);
                        messageHelper.addAttachment(attachmentFile.getName(), fileSystemResource);
                    } else {
                        throw new FileNotFoundException(attachmentFilename);
                    }
                }
                // send email
                SendEmailTask sendEmailTask = new SendEmailTask(mailSender, mimeMessage, emailStatusRepository, emailStatus);
                taskScheduler.schedule(sendEmailTask, new Date(System.currentTimeMillis() + 5000));
                emailStatusRepository.save(emailStatus);

            } catch (AddressException ex) {
                emailStatus.setStatus("Failed - the recipient's email is not valid");
                emailStatusRepository.save(emailStatus);
                System.out.println("Invalid email address: " + to);
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
    }
}
