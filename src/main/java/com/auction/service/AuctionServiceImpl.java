package com.auction.service;

import com.auction.dto.AuctionDTO;
import com.auction.dto.AuctionResponseDTO;
import com.auction.entity.Auction;
import com.auction.entity.User;
import com.auction.exception.AuctionSystemException;
import com.auction.repository.AuctionRepository;
import com.auction.repository.UserRepository;
import com.auction.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
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


    @Override
    public Auction createAuction(AuctionDTO auctionDTO, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Auction auction = new Auction();
        auction.setName(auctionDTO.getName());
        auction.setUser(user);
        return auctionRepository.save(auction);
    }

    @Override
    public List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request) {
        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction :auctionRepository.findAll()){
            AuctionResponseDTO auctionResponseDTO = new AuctionResponseDTO();
            auctionResponseDTO.setId(auction.getId());
            auctionResponseDTO.setName(auction.getName());
            auctionResponseDTO.setOwner(auction.getUser().getName());
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;
    }

    @Override
    public List<AuctionResponseDTO> getAllAuctionsByUser(Long userId, HttpServletRequest request) {
        //User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuctionSystemException(HttpStatus.BAD_REQUEST,"User Not Found"));

        List<Auction> auctions = auctionRepository.findByUser(user);

        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction : auctions){
            AuctionResponseDTO auctionResponseDTO = new AuctionResponseDTO();
            auctionResponseDTO.setId(auction.getId());
            auctionResponseDTO.setName(auction.getName());
            auctionResponseDTO.setOwner(auction.getUser().getName());
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;

    }
}
