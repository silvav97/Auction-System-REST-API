package com.auction.exception;

public class AuctionDoesNotBelongToUserException extends IllegalArgumentException {

    public AuctionDoesNotBelongToUserException() {
        super("Auction Does Not Belong To User");
    }

}
