package com.auction.controller;


import com.auction.common.ApiResponse;
import com.auction.dto.DepositMoneyDTO;
import com.auction.dto.UpdateUserInformationDTO;
import com.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping
    public ResponseEntity<ApiResponse> depositeMoneyToMyAcount(@RequestBody DepositMoneyDTO depositMoneyDTO, HttpServletRequest request) {
        userService.depositeMoneyToMyAcount(depositMoneyDTO, request);
        return new ResponseEntity<>(new ApiResponse(true, "Money deposited successfully"), HttpStatus.OK);
    }

    @PutMapping("/information")
    public ResponseEntity<ApiResponse> updateUserInformation(@RequestBody UpdateUserInformationDTO updateUserInformationDTO, HttpServletRequest request) {
        userService.updateUserInformation(updateUserInformationDTO, request);
        return new ResponseEntity<>(new ApiResponse(true, "User Information updated successfully"), HttpStatus.OK);
    }


}
