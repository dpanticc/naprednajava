package com.dipl.emailmodule.domain;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_status")
public class EmailStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_email")
    private String from;

    @Column(name = "to_email")
    private String to;

    @Column(name = "subject")
    private String subject;

    @Column(name = "message")
    private String message;

    @Column(name = "attachment_filename")
    private String attachmentFilename;

    @Column(name = "status")
    private String status;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    public EmailStatusEntity() {

    }

    public EmailStatusEntity(String from, String to, String subject, String message, String attachmentFilename, String status, LocalDateTime requestTime) {

        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.attachmentFilename = attachmentFilename;
        this.status = status;
        this.requestTime = requestTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    @Override
    public String toString() {
        return "EmailStatusEntity{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", attachmentFilename='" + attachmentFilename + '\'' +
                ", status='" + status + '\'' +
                ", requestTime=" + requestTime +
                '}';
    }
}