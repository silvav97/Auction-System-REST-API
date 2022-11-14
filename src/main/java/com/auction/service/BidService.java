package com.auction.service;

import com.auction.dto.BidDTO;
import com.auction.dto.BidResponseDTO;
import com.auction.entity.Bid;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BidService {

    Bid makeBid(Long auctionId, BidDTO bidDTO, HttpServletRequest request);

    List<BidResponseDTO> getAllBids(HttpServletRequest request);

    List<BidResponseDTO> getAllBidsByUser(Long userId, HttpServletRequest request);

    List<BidResponseDTO> getAllMyBids(HttpServletRequest request);
}
