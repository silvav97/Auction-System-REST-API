package com.auction.service;

import com.auction.dto.AuctionResponseDTO;
import com.auction.dto.BidDTO;
import com.auction.dto.BidResponseDTO;
import com.auction.entity.Auction;
import com.auction.entity.Bid;
import com.auction.entity.User;
import com.auction.exception.*;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.repository.UserRepository;
import com.auction.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Bid makeBid(Long auctionId, BidDTO bidDTO, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));

        if(auction.getUser().equals(user)) { throw new BidOnOwnAuctionException();}
        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setUser(user);

        if(bidDTO.getBid() != null){
            // bid is not null
            bidConsideringThatBidDTOIsNotNull(bidDTO, user, auction, bid);
        }
        else {
            // bid is null
            bidConsideringThatBidDTOIsNull(auction,bid, user);
        }

        auction.setHighestBid(bid.getBidAmount());
        auction.setHighestBidderId(user.getId());
        return bidRepository.save(bid);

    }

    @Override
    public List<BidResponseDTO> getAllBids(HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<BidResponseDTO> listBids = new ArrayList<>();
        for(Bid bid :bidRepository.findAll()){
            BidResponseDTO bidResponseDTO = mapFromBidToBidResponseDTO(bid);
            listBids.add(bidResponseDTO);
        }
        return listBids;
    }

    @Override
    public List<BidResponseDTO> getAllBidsByUser(Long userId, HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","UserId",String.valueOf(userId)));

        List<BidResponseDTO> listBids = new ArrayList<>();
        for(Bid bid : bidRepository.findByUser(user)){
            BidResponseDTO bidResponseDTO = mapFromBidToBidResponseDTO(bid);
            listBids.add(bidResponseDTO);
        }
        return listBids;
    }

    @Override
    public List<BidResponseDTO> getAllMyBids(HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<BidResponseDTO> listBids = new ArrayList<>();
        for(Bid bid : bidRepository.findByUser(user)){
            BidResponseDTO bidResponseDTO = mapFromBidToBidResponseDTO(bid);
            listBids.add(bidResponseDTO);
        }
        return listBids;
    }

    /*private Long id;
    private Float bidAmount;
    private Long auctionId;
    private String product;
    private String bidderId;
    private String bidderName;*/



    // Map From Bid To BidResponseDTO
    public BidResponseDTO mapFromBidToBidResponseDTO(Bid bid) {
        BidResponseDTO bidResponseDTO = new BidResponseDTO();
        bidResponseDTO.setId(bid.getId());
        bidResponseDTO.setBidAmount(bid.getBidAmount());
        bidResponseDTO.setAuctionId(bid.getAuction().getId());
        bidResponseDTO.setProduct(bid.getAuction().getProduct());
        bidResponseDTO.setBidderId(bid.getUser().getId());
        bidResponseDTO.setBidderName(bid.getUser().getName());
        return bidResponseDTO;
    }


    private void bidConsideringThatBidDTOIsNotNull(BidDTO bidDTO, User user, Auction auction, Bid bid) {
        if(bidDTO.getBid() > user.getCredit()) {
            throw new InsufficientCreditToBidException();
        }

        if(auction.getHighestBid() != null) {
            // This bid won't be the first one, so take into account the highest bid
            if(auction.getHighestBid() >= bidDTO.getBid()) {
                throw new BidLessThanOrEqualToHighestBidException();
            }
            bid.setBidAmount(bidDTO.getBid());
        }
        else {
            // This bid will be the first one because there is not highest bid
            if(auction.getInitialValue() > bidDTO.getBid()) {
                throw new BidLessThanInitialValueException();
            }
            // the bid is more than initial value
            bid.setBidAmount(bidDTO.getBid());
        }



    }


    private void bidConsideringThatBidDTOIsNull(Auction auction, Bid bid, User user) {
        if(auction.getHighestBid() == null) {
            // The bid is the first one
            if(auction.getInitialValue() > user.getCredit()) {
                throw new InsufficientCreditToBidException();
            }
            bid.setBidAmount(auction.getInitialValue());
        }
        else {
            // The bid is not the first one
            Float amountToBeBidden = auction.getHighestBid()+1000;
            if(amountToBeBidden > user.getCredit()) {
                throw new InsufficientCreditToBidException();
            }
            bid.setBidAmount(amountToBeBidden);
        }
    }



}
