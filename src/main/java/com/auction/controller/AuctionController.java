package com.auction.controller;

import com.auction.dto.AuctionDTO;
import com.auction.dto.AuctionResponseDTO;
import com.auction.entity.Auction;
import com.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping
    public Auction createAuction(@RequestBody AuctionDTO auctionDTO, HttpServletRequest request) {
        return auctionService.createAuction(auctionDTO, request);
    }

    @GetMapping
    public List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request) {
        return auctionService.getAllAuctions(request);
    }

    @GetMapping("/{userId}")
    public List<AuctionResponseDTO> getAllAuctionsByUser(@PathVariable("userId") Long userId , HttpServletRequest request) {
        return auctionService.getAllAuctionsByUser(userId, request);
    }
}
