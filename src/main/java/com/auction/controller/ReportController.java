package com.auction.controller;


import com.auction.common.ApiResponse;
import com.auction.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@RequestMapping("/api/auction/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Get All the Auctions
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getAllAuctionsPDFReport(@RequestParam(value = "format", defaultValue = "pdf", required = false) String format, HttpServletRequest request,
                                                               @RequestParam(value = "path", defaultValue = "C://Users//ASUS//Desktop//PROGRAMACION//JAVA//SpringBoot//auction_system//reports//", required = false) String path) throws JRException, FileNotFoundException {
        reportService.exportAuctionsReport(path, format, request);
        return new ResponseEntity<>(new ApiResponse(true, format+" file generated successfully in path "+path), HttpStatus.OK);
    }

    // Get Bids From My Auctions
    @GetMapping("/{auctionId}/bids")
    public ResponseEntity<ApiResponse> getAllBidsFromOneOfMyAuctionsPDFReport(@RequestParam(value = "format", defaultValue = "pdf", required = false) String format,
                                                                              @RequestParam(value = "path", defaultValue = "C://Users//ASUS//Desktop//PROGRAMACION//JAVA//SpringBoot//auction_system//reports//", required = false) String path,
                                                                              @PathVariable("auctionId") Long auctionId, HttpServletRequest request) throws JRException, FileNotFoundException {
        reportService.exportBidsReport(path, format, auctionId, request);
        return new ResponseEntity<>(new ApiResponse(true, format+" file generated successfully in path "+path), HttpStatus.OK);
    }


}
