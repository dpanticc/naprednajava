package com.dipl.emailmodule.controller;

import com.dipl.emailmodule.domain.EmailMessage;
import com.dipl.emailmodule.service.EmailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailSenderService emailSenderService;

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
        this.emailSenderService.sendEmail("myapp.notifications7@gmail.com", subject, message, emailMessage.getAttachmentFilename());
        return ResponseEntity.ok("Success");
    }
}
