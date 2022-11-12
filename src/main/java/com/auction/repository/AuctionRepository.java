package com.auction.repository;


import com.auction.entity.Auction;
import com.auction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findByUser(User user);
    List<Auction> findByActiveTrue();


}
