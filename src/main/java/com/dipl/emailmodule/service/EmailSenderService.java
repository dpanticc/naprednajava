package com.dipl.emailmodule.service;

import java.util.List;
public interface EmailSenderService {
    void sendEmail(List<String> to, String subject, String message, String attachmentFilename);

}
