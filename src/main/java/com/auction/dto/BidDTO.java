package com.auction.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class BidDTO {

    @Positive(message = "Bid may be positive")
    private Float bid;

    public BidDTO() {

    }

    public Float getBid() {
        return bid;
    }

    public void setBid(Float bid) {
        this.bid = bid;
    }
}
