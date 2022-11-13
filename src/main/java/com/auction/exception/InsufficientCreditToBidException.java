package com.auction.exception;

public class InsufficientCreditToBidException extends IllegalArgumentException {

    public InsufficientCreditToBidException() {
        super("Credit Is Insufficient To Bid");
    }

}
