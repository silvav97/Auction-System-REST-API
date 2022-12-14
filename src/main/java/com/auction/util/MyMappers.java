package com.auction.util;

import com.auction.dto.*;
import com.auction.entity.Auction;
import com.auction.entity.Bid;
import com.auction.entity.Role;
import com.auction.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
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

    public static PDFBidResponseDTO mapFromBidToPDFBidResponseDTO(Bid bid) {
        PDFBidResponseDTO pdfBidResponseDTO = new PDFBidResponseDTO();
        pdfBidResponseDTO.setId(bid.getId());
        pdfBidResponseDTO.setBidderName(bid.getUser().getName());
        pdfBidResponseDTO.setBidderEmail(bid.getUser().getEmail());
        pdfBidResponseDTO.setBidAmount(bid.getBidAmount());
        return pdfBidResponseDTO;
    }

    public static PDFAuctionResponseDTO mapFromAuctionToPDFAuctionResponseDTO(Auction auction) {
        PDFAuctionResponseDTO pdfAuctionResponseDTO = new PDFAuctionResponseDTO();
        pdfAuctionResponseDTO.setId(auction.getId());
        pdfAuctionResponseDTO.setProduct(auction.getProduct());
        pdfAuctionResponseDTO.setAuctioneerName(auction.getUser().getName());
        pdfAuctionResponseDTO.setAuctioneerEmail(auction.getUser().getEmail());
        pdfAuctionResponseDTO.setInitialValue(auction.getInitialValue());
        pdfAuctionResponseDTO.setActive(auction.isActive());
        return pdfAuctionResponseDTO;
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

    // Map From SignDTO to User
    public static User mapFromSignDTOToUser(SignupDTO signupDTO, Role role) {
        User user = new User();
        user.setAddress(signupDTO.getAddress());
        user.setCellPhone(signupDTO.getCellPhone());
        user.setCity(signupDTO.getCity());
        user.setDocumentNumber(signupDTO.getDocumentNumber());
        user.setEmail(signupDTO.getEmail());
        user.setName(signupDTO.getName());
        user.setUsername(signupDTO.getUsername());
        user.setCredit(0F);
        user.setRoles(Collections.singleton(role));
        return user;
    }

}
