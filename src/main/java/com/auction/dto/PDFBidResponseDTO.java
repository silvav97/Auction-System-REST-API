package com.auction.dto;

public class PDFBidResponseDTO {


    private static Long auctionId;
    private static String product;
    private static String auctioneerName;
    private static String auctioneerEmail;

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

    public static Long getAuctionId() {
        return auctionId;
    }

    public static void setAuctionId(Long auctionId) {
        PDFBidResponseDTO.auctionId = auctionId;
    }

    public static String getProduct() {
        return product;
    }

    public static void setProduct(String product) {
        PDFBidResponseDTO.product = product;
    }

    public static String getAuctioneerName() {
        return auctioneerName;
    }

    public static void setAuctioneerName(String auctioneerName) {
        PDFBidResponseDTO.auctioneerName = auctioneerName;
    }

    public static String getAuctioneerEmail() {
        return auctioneerEmail;
    }

    public static void setAuctioneerEmail(String auctioneerEmail) {
        PDFBidResponseDTO.auctioneerEmail = auctioneerEmail;
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
