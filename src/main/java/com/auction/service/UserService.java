package com.auction.service;

import com.auction.dto.DepositMoneyDTO;
import com.auction.dto.SignupDTO;
import com.auction.dto.UpdateUserInformationDTO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    void save(SignupDTO signupDTO);

    void depositeMoneyToMyAcount(DepositMoneyDTO depositMoneyDTO, HttpServletRequest request);

    void updateUserInformation(UpdateUserInformationDTO updateUserInformationDTO, HttpServletRequest request);

    void saveAdmin(SignupDTO signupDTO);
}
