package com.auction.dto;

public class PDFAuctionResponseDTO {

    private Long id;
    private String product;
    private String auctioneerName;
    private String auctioneerEmail;
    private Float initialValue;
    private boolean active;

    public PDFAuctionResponseDTO() {
    }

    public PDFAuctionResponseDTO(Long id, String product, String auctioneerName, String auctioneerEmail, Float initialValue, boolean active) {
        this.id = id;
        this.product = product;
        this.auctioneerName = auctioneerName;
        this.auctioneerEmail = auctioneerEmail;
        this.initialValue = initialValue;
        this.active = active;

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

    public String getAuctioneerName() {
        return auctioneerName;
    }

    public void setAuctioneerName(String auctioneerName) {
        this.auctioneerName = auctioneerName;
    }

    public String getAuctioneerEmail() {
        return auctioneerEmail;
    }

    public void setAuctioneerEmail(String auctioneerEmail) {
        this.auctioneerEmail = auctioneerEmail;
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



}
