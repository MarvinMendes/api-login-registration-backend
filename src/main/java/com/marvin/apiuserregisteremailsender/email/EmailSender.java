package com.marvin.apiuserregisteremailsender.email;

import com.marvin.apiuserregisteremailsender.exceptions.EmailFailedException;

public interface EmailSender {

    public void sendEmail(String to, String email) throws EmailFailedException;
}
