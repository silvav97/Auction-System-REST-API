package com.auction.entity;


import javax.persistence.*;

@Entity
@Table(name = "bid")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bidder_id")
    private User user;

    private Float bidAmount;

    @ManyToOne
    private Auction auction;

    public Bid() {

    }

    public Bid(Long id, User user, Float bidAmount, Auction auction) {
        this.id = id;
        this.user = user;
        this.bidAmount = bidAmount;
        this.auction = auction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Float getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Float bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

}
