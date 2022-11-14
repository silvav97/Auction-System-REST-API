package com.auction.controller;


import com.auction.common.ApiResponse;
import com.auction.dto.AuctionResponseDTO;
import com.auction.dto.BidDTO;
import com.auction.dto.BidResponseDTO;
import com.auction.entity.Bid;
import com.auction.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/bid")
public class BidController {

    @Autowired
    private BidService bidService;

    @PostMapping("/{auctionId}")
    public ResponseEntity<ApiResponse> makeBid(@PathVariable("auctionId") Long auctionId, @RequestBody BidDTO bidDTO, HttpServletRequest request) {
        Bid bid = bidService.makeBid(auctionId, bidDTO, request);
        return new ResponseEntity<>(new ApiResponse(true,"Bid made successfully"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<BidResponseDTO> getAllBids(HttpServletRequest request) {
        return bidService.getAllBids(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public List<BidResponseDTO> getAllBidsByUser(@PathVariable("userId") Long userId , HttpServletRequest request) {
        return bidService.getAllBidsByUser(userId, request);
    }

    @GetMapping("/mine")
    public List<BidResponseDTO> getAllMyBids(HttpServletRequest request) {
        return bidService.getAllMyBids(request);
    }



}
