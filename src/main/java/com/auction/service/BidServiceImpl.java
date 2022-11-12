package com.auction.service;

import com.auction.dto.BidDTO;
import com.auction.entity.Bid;
import com.auction.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Override
    public Bid makeBid(Long auctionId, BidDTO bidDTO, HttpServletRequest request) {

        return null;
    }
}
