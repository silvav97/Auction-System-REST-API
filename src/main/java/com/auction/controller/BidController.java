package com.auction.controller;


import com.auction.common.ApiResponse;
import com.auction.dto.AuctionResponseDTO;
import com.auction.dto.BidDTO;
import com.auction.dto.BidResponseDTO;
import com.auction.dto.PaginatedBidResponseDTO;
import com.auction.entity.Bid;
import com.auction.service.BidService;
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
@RequestMapping("/api/bid")
public class BidController {

    @Autowired
    private BidService bidService;


    // Only Admins
    // Get All Bids
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<BidResponseDTO> getAllBids(HttpServletRequest request) {
        return bidService.getAllBids(request);
    }

    // Only Admins
    // Get All Bids With Pagination And Sorting
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationandsorting")
    public PaginatedBidResponseDTO getAllBidsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                      @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                      @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                      HttpServletRequest request) {
        return bidService.getAllBidsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }

    // Only Admins
    // Get All Bids By User
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userid/{userId}")
    public List<BidResponseDTO> getAllBidsByUser(@PathVariable("userId") Long userId , HttpServletRequest request) {
        return bidService.getAllBidsByUser(userId, request);
    }

    // Only Admins
    // Get All Bids By User With Pagination And Sorting
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationandsorting/userid/{userId}")
    public PaginatedBidResponseDTO getAllBidsByUserWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                            @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                            @PathVariable("userId") Long userId , HttpServletRequest request) {
        return bidService.getAllBidsByUserWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, userId, request);
    }

    // Only Admins or Auction's Owner
    // Get Bids From Auction
    @GetMapping("/auctionid/{auctionId}")
    public List<BidResponseDTO> getAllBidsFromOneOfMyAuctions(@PathVariable("auctionId") Long auctionId, HttpServletRequest request) {
        return bidService.getAllBidsFromAuction(auctionId, request);
    }

    // Only Admins or Auction's Owner
    // Get Bids From Auction With Pagination And Sorting
    @GetMapping("/paginationandsorting/auctionid/{auctionId}")
    public PaginatedBidResponseDTO getAllBidsFromOneOfMyAuctionsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                         @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                                         @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                                         @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                                         @PathVariable("auctionId") Long auctionId, HttpServletRequest request) {
        return bidService.getAllBidsFromAuctionWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, auctionId, request);
    }

    // Only Admins or Bid's Owner
    // Get Bid By BidId
    @GetMapping("/{bidId}")
    public BidResponseDTO getAllMyBids(@PathVariable("bidId") Long bidId,HttpServletRequest request) {
        return bidService.getBidByBidId(bidId, request);
    }

    // Only Users
    // Get All My Bids
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mine")
    public List<BidResponseDTO> getAllMyBids(HttpServletRequest request) {
        return bidService.getAllMyBids(request);
    }

    // Only Users
    // Get All My Bids With Pagination And Sorting
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/paginationandsorting/mine")
    public PaginatedBidResponseDTO getAllMyBidsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                        @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                        @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                        @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                        HttpServletRequest request) {
        return bidService.getAllMyBidsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }

    // Only Users
    // Make A Bid
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/auctionid/{auctionId}")
    public ResponseEntity<ApiResponse> makeBid(@Valid @RequestBody BidDTO bidDTO, @PathVariable("auctionId") Long auctionId, HttpServletRequest request) {
        Bid bid = bidService.makeBid(auctionId, bidDTO, request);
        return new ResponseEntity<>(new ApiResponse(true,"Bid made successfully"), HttpStatus.CREATED);
    }


}
