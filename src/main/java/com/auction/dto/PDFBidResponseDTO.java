package com.auction.dto;

public class PDFBidResponseDTO {

    private Long id;
    private String bidderName;
    private String bidderEmail;
    private Float bidAmount;

    public PDFBidResponseDTO() {

    }

    public PDFBidResponseDTO(Long id, String bidderName, String bidderEmail, Float bidAmount) {
        this.id = id;
        this.bidderName = bidderName;
        this.bidderEmail = bidderEmail;
        this.bidAmount = bidAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public String getBidderEmail() {
        return bidderEmail;
    }

    public void setBidderEmail(String bidderEmail) {
        this.bidderEmail = bidderEmail;
    }

    public Float getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Float bidAmount) {
        this.bidAmount = bidAmount;
    }

}
