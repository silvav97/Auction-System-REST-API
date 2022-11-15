package com.auction.repository;

import com.auction.entity.WinnerBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WinnerBidRepository extends JpaRepository<WinnerBid, Long> {

}
