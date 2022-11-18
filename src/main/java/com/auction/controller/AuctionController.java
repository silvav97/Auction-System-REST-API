package com.auction.controller;

import com.auction.common.ApiResponse;
import com.auction.dto.*;
import com.auction.service.AuctionService;
import com.auction.util.AppConstants;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
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
    public ResponseEntity<ApiResponse> endAuction(@PathVariable("auctionId") Long auctionId, HttpServletRequest request) {
        auctionService.endAuction(auctionId, request);
        return new ResponseEntity<>(new ApiResponse(true, "Auction Ended successfully"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request) {
        return auctionService.getAllAuctions(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationandsorting")
    public PaginatedAuctionResponseDTO getAllAuctionsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                              @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                              HttpServletRequest request) {
        return auctionService.getAllAuctionsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export/{format}")
    public String getAllAuctionsPDFReport(@PathVariable("format") String format, HttpServletRequest request) throws JRException, FileNotFoundException {
        return auctionService.exportAuctionsReport(format, request);
    }

    // Get Bids From My Auctions
    @GetMapping("/{auctionId}/bids/export/{format}")
    public String getAllBidsFromOneOfMyAuctionsPDFReport(@PathVariable("format") String format, @PathVariable("auctionId") Long auctionId, HttpServletRequest request) throws JRException, FileNotFoundException {
        return auctionService.exportBidsReport(format, auctionId, request);
    }










    // Get All The Auctions By User
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public List<AuctionResponseDTO> getAllAuctionsByUser(@PathVariable("userId") Long userId, HttpServletRequest request) {
        return auctionService.getAllAuctionsByUser(userId, request);
    }

    // Get All The Auctions By User With Pagination And Sorting
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationandsorting/{userId}")
    public PaginatedAuctionResponseDTO getAllAuctionsByUserWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                                    @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                                    @PathVariable("userId") Long userId, HttpServletRequest request) {
        return auctionService.getAllAuctionsByUserWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, userId, request);
    }

    // Get All The Active Auctions
    @GetMapping("/active")
    public List<AuctionResponseDTO> getAllActiveAuctions(HttpServletRequest request) {
        return auctionService.getAllActiveAuctions(request);
    }

    // Get All The Active Auctions With Pagination And Sorting
    @GetMapping("/paginationandsorting/active")
    public PaginatedAuctionResponseDTO getAllActiveAuctionsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                                    @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                                    HttpServletRequest request) {
        return auctionService.getAllActiveAuctionsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }

    // Get All My Auctions
    @GetMapping("/mine")
    public List<AuctionResponseDTO> getAllMyAuctions(HttpServletRequest request) {
        return auctionService.getAllMyAuctions(request);
    }

    // Get All My Auctions With Pagination And Sorting
    @GetMapping("/paginationandsorting/mine")
    public PaginatedAuctionResponseDTO getAllMyAuctionsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                                    @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                                    HttpServletRequest request) {
        return auctionService.getAllMyAuctionsWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, request);
    }


    // Get Bids From My Auctions
    @GetMapping("/{auctionId}/bids")
    public List<BidResponseDTO> getAllBidsFromOneOfMyAuctions(@PathVariable("auctionId") Long auctionId, HttpServletRequest request) {
        return auctionService.getAllBidsFromAuction(auctionId, request);
    }

    // Get Bids From My Auctions With Pagination And Sorting
    @GetMapping("/paginationandsorting/{auctionId}/bids")
    public PaginatedBidResponseDTO getAllBidsFromOneOfMyAuctionsWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                         @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                                         @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                                         @RequestParam(value = "sortDireccion", defaultValue = AppConstants.DEFAULT_SORT_DIRECCION, required = false) String sortDireccion,
                                                                                         @PathVariable("auctionId") Long auctionId, HttpServletRequest request) {
        return auctionService.getAllBidsFromAuctionWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDireccion, auctionId, request);
    }







}

