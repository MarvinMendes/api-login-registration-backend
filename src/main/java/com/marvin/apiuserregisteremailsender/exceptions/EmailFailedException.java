package com.marvin.apiuserregisteremailsender.exceptions;

public class EmailFailedException extends Exception {
    public EmailFailedException(String failedToSendEmail) {
        super(failedToSendEmail);
    }
}
