package com.auction.exception;

public class BidDoesNotBelongToUserException extends IllegalArgumentException {

    public BidDoesNotBelongToUserException() {
        super("Bid Does Not Belong To User");
    }

}
