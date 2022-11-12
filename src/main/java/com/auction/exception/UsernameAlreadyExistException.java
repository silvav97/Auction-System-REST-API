package com.auction.exception;

public class UsernameAlreadyExistException extends IllegalArgumentException {

    public UsernameAlreadyExistException(String username) {
        super("Username '" + username + "' Already exists");
    }

}