package com.marvin.apiuserregisteremailsender.exceptions;

public class TokenNotFoundException extends Exception{
    public TokenNotFoundException(String token) {
        super(String.format("The token informed was not founded. Token: %s", token));
    }
}
