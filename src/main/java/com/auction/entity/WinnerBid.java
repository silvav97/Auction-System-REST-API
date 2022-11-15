package com.auction.entity;


import org.hibernate.mapping.ToOne;

import javax.persistence.*;

@Entity
@Table(name = "winner_bid")
public class WinnerBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @OneToOne
    @JoinColumn(name = "bidder_id")
    private User user;

    public WinnerBid() {
    }

    public WinnerBid(Auction auction, User user) {
        this.auction = auction;
        this.user = user;
    }

    public WinnerBid(Long id, Auction auction, User user) {
        this.id = id;
        this.auction = auction;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
