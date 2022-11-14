package com.auction.dto;

import com.auction.entity.Auction;
import com.auction.entity.User;


public class BidResponseDTO {


    private Long id;
    private Float bidAmount;
    private Long auctionId;
    private String product;
    private Long bidderId;
    private String bidderName;

    public BidResponseDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Float bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public void setBidderId(Long bidderId) {
        this.bidderId = bidderId;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }
}
