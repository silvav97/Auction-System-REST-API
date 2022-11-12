package com.auction.service;

import com.auction.dto.AuctionDTO;
import com.auction.dto.AuctionResponseDTO;
import com.auction.entity.Auction;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AuctionService {

    Auction createAuction(AuctionDTO auctionDTO, HttpServletRequest request);

    List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request);

    List<AuctionResponseDTO> getAllAuctionsByUser(Long userId, HttpServletRequest request);
}
