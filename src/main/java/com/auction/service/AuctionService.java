package com.auction.service;

import com.auction.dto.AuctionDTO;
import com.auction.dto.AuctionResponseDTO;
import com.auction.dto.BidResponseDTO;
import com.auction.entity.Auction;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AuctionService {

    // Any Authenticated User
    Auction createAuction(AuctionDTO auctionDTO, HttpServletRequest request);
    Auction finishAuction(Long auctionId, HttpServletRequest request);

    // Only Admin
    List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request);
    List<AuctionResponseDTO> getAllAuctionsByUser(Long userId, HttpServletRequest request);

    // Any Authenticated User
    List<AuctionResponseDTO> getAllActiveAuctions(HttpServletRequest request);
    List<AuctionResponseDTO> getAllMyAuctions(HttpServletRequest request);

    // Only Admin
    List<BidResponseDTO> getAllBidsFromAuction(Long auctionId, HttpServletRequest request);
}