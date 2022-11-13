package com.auction.exception;

public class BidOnOwnAuctionException extends IllegalArgumentException {

    public BidOnOwnAuctionException() {
        super("Can Not Bid On Own Auction");
    }

}
