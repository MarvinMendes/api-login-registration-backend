package com.marvin.apiuserregisteremailsender.exceptions;

public class UserAlreadyRegisterException extends Exception {
    public UserAlreadyRegisterException(String email) {
        super(String.format("User already exists %s ", email));
    }
}
