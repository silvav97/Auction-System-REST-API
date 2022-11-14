package com.auction.repository;

import com.auction.entity.Auction;
import com.auction.entity.Bid;
import com.auction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByUser(User user);
    List<Bid> findByAuctionId(Long auctionId);

}
