package com.auction.exception;

public class AuctionAlreadyEndedException extends IllegalArgumentException {

    public AuctionAlreadyEndedException() {
        super("Auction Already Ended");
    }

}
