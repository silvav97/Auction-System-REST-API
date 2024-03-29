package com.auction.service;

import com.auction.dto.*;
import com.auction.entity.*;
import com.auction.exception.*;
import com.auction.repository.*;
import com.auction.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.auction.util.MyMappers.*;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WinnerBidRepository winnerBidRepository;

    // Only Users
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

    // Only Users
    @Override
    public WinnerBid endAuction(Long auctionId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));
        if(!auction.getUser().equals(user))   throw new AuctionDoesNotBelongToUserException();
        if(!auction.isActive())   throw new AuctionAlreadyEndedException();

        auction.setActive(false);
        auctionRepository.save(auction);

        if(auction.getHighestBidderId() == null)   throw new ThereWasNoWinnerException();

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

    // Only Users
    @Override
    public List<AuctionResponseDTO> getAllMyAuctions(HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<Auction> auctions = auctionRepository.findByUser(user);
        List<AuctionResponseDTO> listAuctions =  auctions.stream().map(auction -> mapFromAuctionToAuctionResponseDTO(auction)).collect(Collectors.toList());
        return listAuctions;
    }

    // Only Users
    @Override
    public PaginatedAuctionResponseDTO getAllMyAuctionsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Auction> auctions = auctionRepository.findAllByUser(user, pageable);
        return returnPaginatedAuctionResponseDTO(auctions);
    }

    // Only Admins
    @Override
    public List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<Auction> auctions = auctionRepository.findAll();
        List<AuctionResponseDTO> listAuctions =  auctions.stream().map(auction -> mapFromAuctionToAuctionResponseDTO(auction)).collect(Collectors.toList());
        return listAuctions;

    }

    // Only Admins
    @Override
    public PaginatedAuctionResponseDTO getAllAuctionsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Auction> auctions = auctionRepository.findAll(pageable);
        return returnPaginatedAuctionResponseDTO(auctions);
    }

    // Only Admins
    @Override
    public List<AuctionResponseDTO> getAllAuctionsByUser(Long userId, HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","UserId",String.valueOf(userId)));
        List<Auction> auctions = auctionRepository.findByUser(user);
        List<AuctionResponseDTO> listAuctions =  auctions.stream().map(auction -> mapFromAuctionToAuctionResponseDTO(auction)).collect(Collectors.toList());
        return listAuctions;
    }

    // Only Admins
    @Override
    public PaginatedAuctionResponseDTO getAllAuctionsByUserWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, Long userId, HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","UserId",String.valueOf(userId)));
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Auction> auctions = auctionRepository.findAllByUser(user, pageable);
        return returnPaginatedAuctionResponseDTO(auctions);
    }

    // Any Authenticated User
    @Override
    public List<AuctionResponseDTO> getAllActiveAuctions(HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<Auction> auctions = auctionRepository.findByActiveTrue();
        List<AuctionResponseDTO> listAuctions =  auctions.stream().map(auction -> mapFromAuctionToAuctionResponseDTO(auction)).collect(Collectors.toList());
        return listAuctions;
    }

    // Any Authenticated User
    @Override
    public PaginatedAuctionResponseDTO getAllActiveAuctionsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request) {
        //jwtAuthenticationFilter.getTheUserFromRequest(request);
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Auction> auctions = auctionRepository.findAllByActiveTrue(pageable);
        return returnPaginatedAuctionResponseDTO(auctions);
    }

    // Only Admins or Auction's Owner
    @Override
    public AuctionResponseDTO getAuctionByAuctionId(Long auctionId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role","Name","ROLE_ADMIN"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));

        if(!(user.getRoles().contains(role) || auction.getUser().getUsername().equals(user.getUsername()))) {
            throw new AuctionDoesNotBelongToUserException();
        }
        return mapFromAuctionToAuctionResponseDTO(auction);
    }


}
