package com.marvin.apiuserregisteremailsender.exceptions;

public class UserWrongTokenException extends Exception {
    public UserWrongTokenException(String token) {
        super(String.format("The token informed could be wrong, check the correct token and try again. Token informed: %s", token));
    }
}
