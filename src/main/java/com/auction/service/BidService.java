package com.auction.service;

import com.auction.dto.AuctionResponseDTO;
import com.auction.dto.BidDTO;
import com.auction.dto.BidResponseDTO;
import com.auction.dto.PaginatedBidResponseDTO;
import com.auction.entity.Bid;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BidService {

    // Only Admins
    List<BidResponseDTO> getAllBids(HttpServletRequest request);
    PaginatedBidResponseDTO getAllBidsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request);
    List<BidResponseDTO> getAllBidsByUser(Long userId, HttpServletRequest request);
    PaginatedBidResponseDTO getAllBidsByUserWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, Long userId, HttpServletRequest request);

    // Only Admins or Auction's Owner
    List<BidResponseDTO> getAllBidsFromAuction(Long auctionId, HttpServletRequest request);
    PaginatedBidResponseDTO getAllBidsFromAuctionWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, Long auctionId, HttpServletRequest request);

    // Only Admins or Bid's Owner
    BidResponseDTO getBidByBidId(Long bidId, HttpServletRequest request);

    // Only Users
    List<BidResponseDTO> getAllMyBids(HttpServletRequest request);
    PaginatedBidResponseDTO getAllMyBidsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request);
    Bid makeBid(Long auctionId, BidDTO bidDTO, HttpServletRequest request);

}
