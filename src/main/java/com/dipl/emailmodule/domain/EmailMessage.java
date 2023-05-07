package com.dipl.emailmodule.domain;

public class EmailMessage {

    private String from;
    private String to;
    private String subject;
    private String message;
    private String attachmentFilename;


    public EmailMessage(String from, String to, String subject, String message, String attachmentFilename) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.attachmentFilename = attachmentFilename;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachmentFilename() {
        return attachmentFilename;
    }

    public void setAttachmentFilename(String attachmentFilename) {
        this.attachmentFilename = attachmentFilename;
    }
}
