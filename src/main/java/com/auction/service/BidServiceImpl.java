package com.auction.service;

import com.auction.dto.BidDTO;
import com.auction.entity.Auction;
import com.auction.entity.Bid;
import com.auction.entity.User;
import com.auction.exception.*;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Override
    public Bid makeBid(Long auctionId, BidDTO bidDTO, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));

        if(auction.getUser().equals(user)) { throw new BidOnOwnAuctionException();}
        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setUser(user);

        if(bidDTO.getBid() != null){
            // bid not null
            bidConsideringThatBidDTOIsNotNull(bidDTO, user, auction, bid);
        }

        // bid is null
        bidConsideringThatBidDTOIsNull(auction,bid, user);
        return bidRepository.save(bid);

    }

    private void bidConsideringThatBidDTOIsNotNull(BidDTO bidDTO, User user, Auction auction, Bid bid) {
        if(bidDTO.getBid() > user.getCredit()) {
            throw new InsufficientCreditToBidException();
        }
        if(auction.getHighestBid() != null) {
            // This bid won't be the first one, so take into account the highest bid
            if(auction.getHighestBid() > bidDTO.getBid()) {
                throw new BidLessThanHighestBidException();
            }
            bid.setBidAmount(bidDTO.getBid());
        }

        // This bid will be the first one because there is not highest bid
        if(auction.getInitialValue() > bidDTO.getBid()) {
            throw new BidLessThanInitialValueException();
        }
        // the bid is more than initial value
        bid.setBidAmount(auction.getInitialValue());

    }


    private void bidConsideringThatBidDTOIsNull(Auction auction, Bid bid, User user) {
        if(auction.getHighestBid() == null) {
            // The bid is the first one
            if(auction.getInitialValue() > user.getCredit()) {
                throw new InsufficientCreditToBidException();
            }
            bid.setBidAmount(auction.getInitialValue());
        }
        // The bid is not the first one
        Float amountToBeBidden = auction.getHighestBid()+1000;
        if(amountToBeBidden > user.getCredit()) {
            throw new InsufficientCreditToBidException();
        }
        bid.setBidAmount(amountToBeBidden);

    }
}
