package com.auction.service;

import com.auction.dto.BidDTO;
import com.auction.dto.BidResponseDTO;
import com.auction.dto.PaginatedBidResponseDTO;
import com.auction.entity.Auction;
import com.auction.entity.Bid;
import com.auction.entity.Role;
import com.auction.entity.User;
import com.auction.exception.*;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.repository.RoleRepository;
import com.auction.repository.UserRepository;
import com.auction.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.auction.util.MyMappers.*;

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

    @Autowired
    private RoleRepository roleRepository;


    // Only Admins
    @Override
    public List<BidResponseDTO> getAllBids(HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<Bid> bids = bidRepository.findAll();
        List<BidResponseDTO> listBids =  bids.stream().map(bid -> mapFromBidToBidResponseDTO(bid)).collect(Collectors.toList());
        return listBids;
    }

    // Only Admins
    @Override
    public PaginatedBidResponseDTO getAllBidsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Bid> bids = bidRepository.findAll(pageable);
        return returnPaginatedBidResponseDTO(bids);
    }

    // Only Admins
    @Override
    public List<BidResponseDTO> getAllBidsByUser(Long userId, HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","UserId",String.valueOf(userId)));
        List<Bid> bids = bidRepository.findByUser(user);
        List<BidResponseDTO> listBids =  bids.stream().map(bid -> mapFromBidToBidResponseDTO(bid)).collect(Collectors.toList());
        return listBids;
    }

    // Only Admins
    @Override
    public PaginatedBidResponseDTO getAllBidsByUserWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, Long userId, HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","UserId",String.valueOf(userId)));
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Bid> bids = bidRepository.findAllByUser(user, pageable);
        return returnPaginatedBidResponseDTO(bids);
    }

    // Only Admins or Auction's Owner
    @Override
    public List<BidResponseDTO> getAllBidsFromAuction(Long auctionId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role","Name","ROLE_ADMIN"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));

        if(!(user.getRoles().contains(role) || auction.getUser().getUsername().equals(user.getUsername()))) {
            throw new AuctionDoesNotBelongToUserException();
        }

        List<Bid> bids = bidRepository.findByAuctionId(auctionId);
        List<BidResponseDTO> listBids =  bids.stream().map(bid -> mapFromBidToBidResponseDTO(bid)).collect(Collectors.toList());
        return listBids;
    }

    // Only Admins or Auction's Owner
    @Override
    public PaginatedBidResponseDTO getAllBidsFromAuctionWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, Long auctionId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role","Name","ROLE_ADMIN"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));

        if(!(user.getRoles().contains(role) || auction.getUser().getUsername().equals(user.getUsername()))) {
            throw new AuctionDoesNotBelongToUserException();
        }

        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Bid> bids = bidRepository.findAllByAuctionId(auctionId,pageable);
        return returnPaginatedBidResponseDTO(bids);

    }

    // Only Admins or Bid's Owner
    @Override
    public BidResponseDTO getBidByBidId(Long bidId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role","Name","ROLE_ADMIN"));
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new ResourceNotFoundException("Bid","BidId",String.valueOf(bidId)));

        if(!(user.getRoles().contains(role) || bid.getUser().getUsername().equals(user.getUsername()))) {
            throw new BidDoesNotBelongToUserException();
        }
        return mapFromBidToBidResponseDTO(bid);
    }

    // Only Users
    @Override
    public List<BidResponseDTO> getAllMyBids(HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<Bid> bids = bidRepository.findByUser(user);
        List<BidResponseDTO> listBids =  bids.stream().map(bid -> mapFromBidToBidResponseDTO(bid)).collect(Collectors.toList());
        return listBids;
    }

    // Only Users
    @Override
    public PaginatedBidResponseDTO getAllMyBidsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Bid> bids = bidRepository.findAllByUser(user, pageable);
        return returnPaginatedBidResponseDTO(bids);
    }

    // Only Users
    @Override
    public Bid makeBid(Long auctionId, BidDTO bidDTO, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));
        if(auction.getUser().equals(user)) {throw new BidOnOwnAuctionException();}
        if(auction.isActive() == false) {throw new AuctionIsNotActiveException();}

        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setUser(user);

        if(bidDTO.getBid() != null) {bidConsideringThatBidDTOIsNotNull(bidDTO, user, auction, bid);}// bid is not null
        else {bidConsideringThatBidDTOIsNull(auction,bid, user);} // bid is null

        auction.setHighestBid(bid.getBidAmount());
        auction.setHighestBidderId(user.getId());
        return bidRepository.save(bid);

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
