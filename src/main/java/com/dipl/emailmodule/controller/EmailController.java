package com.dipl.emailmodule.controller;

import com.dipl.emailmodule.domain.EmailMessage;
import com.dipl.emailmodule.service.EmailSenderService;
import com.dipl.emailmodule.templates.WelcomeEmailTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
public class EmailController {

    private final EmailSenderService emailSenderService;

    @Value("${spring.mail.username}")
    private String from;

    public EmailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@RequestBody EmailMessage emailMessage){
        this.emailSenderService.sendEmail(emailMessage.getTo(),emailMessage.getSubject(), emailMessage.getMessage(), emailMessage.getAttachmentFilename());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/send-feedback")
    public ResponseEntity sendFeedback(@RequestBody EmailMessage emailMessage){
        String from = emailMessage.getFrom();
        String subject = "Feedback from " + from + ": " + emailMessage.getSubject();
        String message = "From: " + from + "\n\n" + emailMessage.getMessage();
        List<String> to = Arrays.asList("myapp.notifications7@gmail.com");
        this.emailSenderService.sendEmail(to, subject, message, emailMessage.getAttachmentFilename());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/send-welcome-email")
    public ResponseEntity sendWelcomeEmail(@RequestBody EmailMessage emailMessage){
        List<String> to = emailMessage.getTo();
        String attachmentFilename = emailMessage.getAttachmentFilename(); // get attachment filename from the request
        WelcomeEmailTemplate welcomeEmailTemplate = new WelcomeEmailTemplate(from, to, attachmentFilename); // pass attachment filename to the constructor
        EmailMessage message = welcomeEmailTemplate.generate();
        this.emailSenderService.sendEmail(message.getTo(), message.getSubject(), message.getMessage(), message.getAttachmentFilename());
        return ResponseEntity.ok("Success");
    }
}
