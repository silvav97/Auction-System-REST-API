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
    @GetMapping("/paginationandsorting")
    public PaginatedBidResponseDTO getAllBidsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                      @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                      @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                      HttpServletRequest request) {
        return bidService.getAllBidsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public List<BidResponseDTO> getAllBidsByUser(@PathVariable("userId") Long userId , HttpServletRequest request) {
        return bidService.getAllBidsByUser(userId, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationandsorting/{userId}")
    public PaginatedBidResponseDTO getAllBidsByUserWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                            @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                            @PathVariable("userId") Long userId , HttpServletRequest request) {
        return bidService.getAllBidsByUserWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, userId, request);
    }


    @GetMapping("/mine")
    public List<BidResponseDTO> getAllMyBids(HttpServletRequest request) {
        return bidService.getAllMyBids(request);
    }

    @GetMapping("/paginationandsorting/mine")
    public PaginatedBidResponseDTO getAllMyBidsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                            @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                            HttpServletRequest request) {
        return bidService.getAllMyBidsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }



}
