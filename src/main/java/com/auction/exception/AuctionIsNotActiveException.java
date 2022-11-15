package com.auction.exception;

public class AuctionIsNotActiveException extends IllegalArgumentException {

    public AuctionIsNotActiveException() {
        super("Can Not Bid On A Non-active Auction");
    }

}
