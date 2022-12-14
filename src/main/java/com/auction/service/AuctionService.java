package com.auction.service;

import com.auction.dto.*;
import com.auction.entity.Auction;
import com.auction.entity.WinnerBid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AuctionService {

    // Only Users
    Auction createAuction(AuctionDTO auctionDTO, HttpServletRequest request);
    WinnerBid endAuction(Long auctionId, HttpServletRequest request);
    List<AuctionResponseDTO> getAllMyAuctions(HttpServletRequest request);
    PaginatedAuctionResponseDTO getAllMyAuctionsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request);

    // Only Admins
    List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request);
    PaginatedAuctionResponseDTO getAllAuctionsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request);
    List<AuctionResponseDTO> getAllAuctionsByUser(Long userId, HttpServletRequest request);
    PaginatedAuctionResponseDTO getAllAuctionsByUserWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, Long userId, HttpServletRequest request);

    // Any Authenticated User
    List<AuctionResponseDTO> getAllActiveAuctions(HttpServletRequest request);
    PaginatedAuctionResponseDTO getAllActiveAuctionsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request);

    // Only Admins or Auction's Owner
    AuctionResponseDTO getAuctionByAuctionId(Long auctionId, HttpServletRequest request);
}