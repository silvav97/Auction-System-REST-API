package com.auction.service;

import com.auction.dto.BidDTO;
import com.auction.entity.Bid;

import javax.servlet.http.HttpServletRequest;

public interface BidService {

    Bid makeBid(Long auctionId, BidDTO bidDTO, HttpServletRequest request);
}
