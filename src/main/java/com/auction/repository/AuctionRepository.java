package com.auction.repository;


import com.auction.entity.Auction;
import com.auction.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findByUser(User user);
    List<Auction> findByActiveTrue();

    Page<Auction> findAllByUser(User user, Pageable pageable);
    Page<Auction> findAllByActiveTrue(Pageable pageable);



}
