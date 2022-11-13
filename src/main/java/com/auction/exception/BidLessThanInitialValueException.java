package com.auction.exception;

public class BidLessThanInitialValueException extends IllegalArgumentException {

    public BidLessThanInitialValueException() {
        super("Bid Can Not Be Less Than Initial Value");
    }

}
