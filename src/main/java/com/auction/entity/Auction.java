package com.auction.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auction")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;

    private String description;

    private Float initialValue;

    private boolean active;

    private Float highestBid;

    @ManyToOne
    @JoinColumn(name = "auctioneer_id")
    private User user;

    //@OneToMany
    //private Set<Bid> bids = new HashSet<>();

    public Auction() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }*/

}
