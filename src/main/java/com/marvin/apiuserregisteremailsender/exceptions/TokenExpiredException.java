package com.marvin.apiuserregisteremailsender.exceptions;

public class TokenExpiredException extends Exception {
    public TokenExpiredException(String token) {
        super(String.format("This token %s has been expired already", token));
    }
}
