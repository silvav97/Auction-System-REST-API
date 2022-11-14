package com.auction.exception;

public class BidLessThanOrEqualToHighestBidException extends IllegalArgumentException {

    public BidLessThanOrEqualToHighestBidException() {
        super("Bid Can Not Be Less Than Or Equal To The Highest Bid");
    }

}
