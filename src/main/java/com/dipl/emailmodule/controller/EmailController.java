package com.dipl.emailmodule.controller;

import com.dipl.emailmodule.resource.EmailMessage;
import com.dipl.emailmodule.service.EmailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

//    @PostMapping("/send-email")
//    public ResponseEntity<String> sendEmail(@RequestParam(required = false) MultipartFile attachment,
//                                            @RequestParam String to,
//                                            @RequestParam String subject,
//                                            @RequestParam String message) {
//
//        if (attachment != null && !attachment.isEmpty()) {
//            String attachmentFilename = attachment.getOriginalFilename();
//            this.emailSenderService.sendEmail(to, subject, message, attachmentFilename, attachment);
//        } else {
//            this.emailSenderService.sendEmail(to, subject, message);
//        }
//        return ResponseEntity.ok("Email sent successfully");
//    }
    @PostMapping("/send-feedback")
    public ResponseEntity sendFeedback(@RequestBody EmailMessage emailMessage){
        String from = emailMessage.getFrom();
        String subject = "Feedback from " + from + ": " + emailMessage.getSubject();
        String message = "From: " + from + "\n\n" + emailMessage.getMessage();
        this.emailSenderService.sendEmail("myapp.notifications7@gmail.com", subject, message, emailMessage.getAttachmentFilename());
        return ResponseEntity.ok("Success");
    }
}
