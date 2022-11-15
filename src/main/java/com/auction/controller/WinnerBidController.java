package com.auction.controller;


import com.auction.service.WinnerBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/winnerbid")
public class WinnerBidController {

    @Autowired
    private WinnerBidService winnerBidService;



}
