package com.auction.service;

import com.auction.dto.AuctionDTO;
import com.auction.dto.AuctionResponseDTO;
import com.auction.dto.BidResponseDTO;
import com.auction.entity.*;
import com.auction.exception.AuctionAlreadyEndedException;
import com.auction.exception.AuctionDoesNotBelongToUserException;
import com.auction.exception.ResourceNotFoundException;
import com.auction.exception.ThereWasNoWinnerException;
import com.auction.repository.*;
import com.auction.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private BidServiceImpl bidService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WinnerBidRepository winnerBidRepository;


    @Override
    public Auction createAuction(AuctionDTO auctionDTO, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Auction auction = new Auction();
        auction.setProduct(auctionDTO.getProduct());
        auction.setDescription(auctionDTO.getDescription());
        auction.setInitialValue(auctionDTO.getInitialValue());
        auction.setActive(true);
        auction.setUser(user);
        return auctionRepository.save(auction);
    }


    @Override
    public WinnerBid endAuction(Long auctionId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));
        if(!auction.getUser().equals(user)) {
            throw new AuctionDoesNotBelongToUserException();
        }
        if(auction.isActive() == false) {
            throw new AuctionAlreadyEndedException();
        }
        auction.setActive(false);
        auctionRepository.save(auction);

        if(auction.getHighestBidderId() == null) {
            throw new ThereWasNoWinnerException();
        }

        WinnerBid winnerBid = registerWinnerAndTransferMoneyFromWinnerToAuctioneer(auction);

        return winnerBid;
    }

    @Transactional
    private WinnerBid registerWinnerAndTransferMoneyFromWinnerToAuctioneer(Auction auction) {
        User winner = userRepository.findById(auction.getHighestBidderId())
                .orElseThrow(() -> new ResourceNotFoundException("User","UserID",String.valueOf(auction.getHighestBidderId())));
        User auctioneer = auction.getUser();
        winner.setCredit(winner.getCredit() - auction.getHighestBid());   // Take money from bidder
        auction.getUser().setCredit(auction.getUser().getCredit() + auction.getHighestBid());  // Deposite Money To Auctioneer

        userRepository.saveAll(List.of(winner,auctioneer));
        WinnerBid winnerBid = new WinnerBid(auction,winner);
        return winnerBidRepository.save(winnerBid);
    }


    // Only Admin role users
    @Override
    public List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction :auctionRepository.findAll()){
            AuctionResponseDTO auctionResponseDTO = mapFromAuctionToAuctionResponseDTO(auction);
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;
    }

    // Only Admin role users
    @Override
    public List<AuctionResponseDTO> getAllAuctionsByUser(Long userId, HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","UserId",String.valueOf(userId)));

        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction : auctionRepository.findByUser(user)){
            AuctionResponseDTO auctionResponseDTO = mapFromAuctionToAuctionResponseDTO(auction);
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;

    }

    @Override
    public List<AuctionResponseDTO> getAllActiveAuctions(HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction : auctionRepository.findByActiveTrue()){
            AuctionResponseDTO auctionResponseDTO = mapFromAuctionToAuctionResponseDTO(auction);
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;
    }

    @Override
    public List<AuctionResponseDTO> getAllMyAuctions(HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction : auctionRepository.findByUser(user)){
            AuctionResponseDTO auctionResponseDTO = mapFromAuctionToAuctionResponseDTO(auction);
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;
    }

    @Override
    public List<BidResponseDTO> getAllBidsFromAuction(Long auctionId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role","Name","ROLE_ADMIN"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));

        if(!(user.getRoles().contains(role) || auction.getUser().getUsername().equals(user.getUsername()))) {
            throw new AuctionDoesNotBelongToUserException();
        }
        List<BidResponseDTO> listBids = new ArrayList<>();
        for(Bid bid : bidRepository.findByAuctionId(auctionId)){
            BidResponseDTO bidResponseDTO = bidService.mapFromBidToBidResponseDTO(bid);
            listBids.add(bidResponseDTO);
        }
        return listBids;
    }


    private AuctionResponseDTO mapFromAuctionToAuctionResponseDTO(Auction auction) {
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

}
