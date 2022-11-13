package com.auction.controller;

import com.auction.common.ApiResponse;
import com.auction.dto.AuctionDTO;
import com.auction.dto.AuctionResponseDTO;
import com.auction.dto.DepositMoneyDTO;
import com.auction.entity.Auction;
import com.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping
    public ResponseEntity<ApiResponse> createAuction(@RequestBody AuctionDTO auctionDTO, HttpServletRequest request) {
        auctionService.createAuction(auctionDTO, request);
        return new ResponseEntity<>(new ApiResponse(true, "Auction created successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/{auctionId}")
    public ResponseEntity<ApiResponse> finishAuction(@PathVariable("auctionId") Long auctionId, HttpServletRequest request) {
        auctionService.finishAuction(auctionId, request);
        return new ResponseEntity<>(new ApiResponse(true, "Auction finished successfully"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request) {
        return auctionService.getAllAuctions(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public List<AuctionResponseDTO> getAllAuctionsByUser(@PathVariable("userId") Long userId , HttpServletRequest request) {
        return auctionService.getAllAuctionsByUser(userId, request);
    }

    @GetMapping("/active")
    public List<AuctionResponseDTO> getAllActiveAuctions(HttpServletRequest request) {
        return auctionService.getAllActiveAuctions(request);
    }


    @GetMapping("/mine")
    public List<AuctionResponseDTO> getAllMyAuctions(HttpServletRequest request) {
        return auctionService.getAllMyAuctions(request);
    }





}
