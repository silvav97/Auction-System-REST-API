package com.auction.controller;

import com.auction.common.ApiResponse;
import com.auction.dto.*;
import com.auction.service.AuctionService;
import com.auction.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    // Only Users
    // Create Auction
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ApiResponse> createAuction(@Valid @RequestBody AuctionDTO auctionDTO, HttpServletRequest request) {
        auctionService.createAuction(auctionDTO, request);
        return new ResponseEntity<>(new ApiResponse(true, "Auction created successfully"), HttpStatus.CREATED);
    }

    // Only Users
    // End One Of My Auctions
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{auctionId}")
    public ResponseEntity<ApiResponse> endAuction(@PathVariable("auctionId") Long auctionId, HttpServletRequest request) {
        auctionService.endAuction(auctionId, request);
        return new ResponseEntity<>(new ApiResponse(true, "Auction Ended successfully"), HttpStatus.OK);
    }

    // Only Users
    // Get All My Auctions
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mine")
    public List<AuctionResponseDTO> getAllMyAuctions(HttpServletRequest request) {
        return auctionService.getAllMyAuctions(request);
    }

    // Only Users
    // Get All My Auctions With Pagination And Sorting
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/paginationandsorting/mine")
    public PaginatedAuctionResponseDTO getAllMyAuctionsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                                @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                                HttpServletRequest request) {
        return auctionService.getAllMyAuctionsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }

    // Only Admins
    // Get All The Auctions
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request) {
        return auctionService.getAllAuctions(request);
    }

    // Only Admins
    // Get All The Auctions With Pagination And Sorting
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationandsorting")
    public PaginatedAuctionResponseDTO getAllAuctionsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                              @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                              HttpServletRequest request) {
        return auctionService.getAllAuctionsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }

    // Only Admins
    // Get All The Auctions By UserId
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userid/{userId}")
    public List<AuctionResponseDTO> getAllAuctionsByUser(@PathVariable("userId") Long userId, HttpServletRequest request) {
        return auctionService.getAllAuctionsByUser(userId, request);
    }

    // Only Admins
    // Get All The Auctions By UserId With Pagination And Sorting
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationandsorting/userid/{userId}")
    public PaginatedAuctionResponseDTO getAllAuctionsByUserWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                                    @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                                    @PathVariable("userId") Long userId, HttpServletRequest request) {
        return auctionService.getAllAuctionsByUserWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, userId, request);
    }

    // Any Authenticated User
    // Get All The Active Auctions
    @GetMapping("/active")
    public List<AuctionResponseDTO> getAllActiveAuctions(HttpServletRequest request) {
        return auctionService.getAllActiveAuctions(request);
    }

    // Any Authenticated User
    // Get All The Active Auctions With Pagination And Sorting
    @GetMapping("/paginationandsorting/active")
    public PaginatedAuctionResponseDTO getAllActiveAuctionsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                                    @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                                    HttpServletRequest request) {
        return auctionService.getAllActiveAuctionsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }

    // Only Admins or Auction's Owner
    // Get Auction By AuctionId
    @GetMapping("/{auctionId}")
    public AuctionResponseDTO getAuctionByAuctionId(@PathVariable("auctionId") Long auctionId, HttpServletRequest request) {
        return auctionService.getAuctionByAuctionId(auctionId, request);
    }











}

