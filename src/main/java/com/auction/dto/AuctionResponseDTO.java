package com.auction.dto;

public class AuctionResponseDTO {

    private Long id;
    private String product;
    private String description;
    private Float initialValue;
    private boolean active;
    private String auctioneer;
    private Float highestBid;


    public AuctionResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Float initialValue) {
        this.initialValue = initialValue;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Float getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(Float highestBid) {
        this.highestBid = highestBid;
    }

    public String getAuctioneer() {
        return auctioneer;
    }

    public void setAuctioneer(String auctioneer) {
        this.auctioneer = auctioneer;
    }
}
