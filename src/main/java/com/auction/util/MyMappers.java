package com.auction.util;

import com.auction.dto.AuctionResponseDTO;
import com.auction.dto.BidResponseDTO;
import com.auction.dto.PaginatedAuctionResponseDTO;
import com.auction.dto.PaginatedBidResponseDTO;
import com.auction.entity.Auction;
import com.auction.entity.Bid;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class MyMappers {


    // Map From Bid To BidResponseDTO
    public static BidResponseDTO mapFromBidToBidResponseDTO(Bid bid) {
        BidResponseDTO bidResponseDTO = new BidResponseDTO();
        bidResponseDTO.setId(bid.getId());
        bidResponseDTO.setBidAmount(bid.getBidAmount());
        bidResponseDTO.setAuctionId(bid.getAuction().getId());
        bidResponseDTO.setProduct(bid.getAuction().getProduct());
        bidResponseDTO.setBidderId(bid.getUser().getId());
        bidResponseDTO.setBidderName(bid.getUser().getName());
        return bidResponseDTO;
    }

    // Map From Auction To AuctionResponseDTO
    public static AuctionResponseDTO mapFromAuctionToAuctionResponseDTO(Auction auction) {
        AuctionResponseDTO auctionResponseDTO = new AuctionResponseDTO();
        auctionResponseDTO.setId(auction.getId());
        auctionResponseDTO.setProduct(auction.getProduct());
        auctionResponseDTO.setDescription(auction.getDescription());
        auctionResponseDTO.setInitialValue(auction.getInitialValue());
        auctionResponseDTO.setActive(auction.isActive());
        auctionResponseDTO.setHighestBid(auction.getHighestBid());
        auctionResponseDTO.setAuctioneer(auction.getUser().getName());
        return auctionResponseDTO;
    }

    // Map From Page<Auction> To PaginatedAuctionResponseDTO
    public static PaginatedAuctionResponseDTO returnPaginatedAuctionResponseDTO(Page<Auction> auctions) {
        List<Auction> auctionList = auctions.getContent();
        List<AuctionResponseDTO> content = auctionList.stream().map(auction -> mapFromAuctionToAuctionResponseDTO(auction)).collect(Collectors.toList());

        PaginatedAuctionResponseDTO response = new PaginatedAuctionResponseDTO();
        response.setContent(content);
        response.setPageNumber(auctions.getNumber());
        response.setPageSize(auctions.getSize());
        response.setTotalElements(auctions.getTotalElements());
        response.setTotalPages(auctions.getTotalPages());
        response.setLastPage(auctions.isLast());
        return response;
    }

    // Map From Page<Bid> To PaginatedBidResponseDTO
    public static PaginatedBidResponseDTO returnPaginatedBidResponseDTO(Page<Bid> bids) {
        List<Bid> bidList = bids.getContent();
        List<BidResponseDTO> content = bidList.stream().map(bid -> mapFromBidToBidResponseDTO(bid)).collect(Collectors.toList());

        PaginatedBidResponseDTO response = new PaginatedBidResponseDTO();
        response.setContent(content);
        response.setPageNumber(bids.getNumber());
        response.setPageSize(bids.getSize());
        response.setTotalElements(bids.getTotalElements());
        response.setTotalPages(bids.getTotalPages());
        response.setLastPage(bids.isLast());
        return response;
    }

}
