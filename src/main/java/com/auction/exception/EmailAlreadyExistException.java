package com.auction.exception;

public class EmailAlreadyExistException extends IllegalArgumentException {

    public EmailAlreadyExistException(String email) {
        super("Email '" + email + "' Already exists");
    }

}
