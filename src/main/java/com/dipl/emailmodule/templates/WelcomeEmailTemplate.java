package com.dipl.emailmodule.templates;

import com.dipl.emailmodule.domain.EmailMessage;
import java.util.List;

public class WelcomeEmailTemplate {

    private final String from;
    private final List<String> to;
    private final String attachmentFilename;

    public WelcomeEmailTemplate(String from, List<String> to, String attachmentFilename) {
        this.from = from;
        this.to = to;
        this.attachmentFilename = attachmentFilename;
    }

    public EmailMessage generate() {
        String subject = "Welcome to our site!";
        String message = "Dear user,\n\n"
                + "Thank you for signing up for our site. We're excited to have you join us!\n\n"
                + "Best regards,\n"
                + "The MySite team";
        return new EmailMessage(from, to, subject, message, attachmentFilename);
    }
}