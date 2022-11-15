package com.auction.exception;

public class ThereWasNoWinnerException extends IllegalArgumentException {

    public ThereWasNoWinnerException() {
        super("Auction Ended and Nobody Won");
    }

}