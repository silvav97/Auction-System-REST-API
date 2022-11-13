package com.auction.service;

import com.auction.dto.DepositMoneyDTO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    void depositeMoneyToMyAcount(DepositMoneyDTO depositMoneyDTO, HttpServletRequest request);


}
