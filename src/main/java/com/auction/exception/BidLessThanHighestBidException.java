package com.auction.exception;

public class BidLessThanHighestBidException extends IllegalArgumentException {

    public BidLessThanHighestBidException() {
        super("Bid Can Not Be Less Than Highest Bid");
    }

}
