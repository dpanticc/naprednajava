package com.dipl.emailmodule.service.impl;

import com.dipl.emailmodule.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;
    private final TaskScheduler taskScheduler;

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender mailSender, TaskScheduler taskScheduler) {
        this.mailSender = mailSender;
        this.taskScheduler = taskScheduler;

    }

    @Override
    public void sendEmail(String to, String subject, String message, String attachmentFilename) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try{
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("myapp.notifications7@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(message);

            // add attachment
            File attachmentFile = new File(attachmentFilename);
            FileSystemResource fileSystemResource = new FileSystemResource(attachmentFile);
            messageHelper.addAttachment(attachmentFile.getName(), fileSystemResource);

            // send email
            taskScheduler.schedule(new SendEmailTask(mailSender, mimeMessage), new Date(System.currentTimeMillis() + 5000));


        }catch (Exception e){

        }

        /* sending emails without attachment:

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("myapp.notifications7@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        //this.mailSender.send(simpleMailMessage);

        taskScheduler.schedule(new SendEmailTask(mailSender, simpleMailMessage), new Date(System.currentTimeMillis() + 5000));
*/
    }
    private static class SendEmailTask implements Runnable {

        private final JavaMailSender mailSender;
        private final MimeMessage message;

        public SendEmailTask(JavaMailSender mailSender, MimeMessage message) {
            this.mailSender = mailSender;
            this.message = message;
        }

        @Override
        public void run() {
            try {
                mailSender.send(message);
                System.out.println("Email sent successfully at " + LocalDateTime.now());
            } catch (Exception e) {
                System.out.println("Error sending email at " + LocalDateTime.now() + " - " + e.getMessage());
            }
        }
    }
}
