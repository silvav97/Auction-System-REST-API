package com.auction.service;

import com.auction.dto.DepositMoneyDTO;
import com.auction.entity.User;
import com.auction.repository.UserRepository;
import com.auction.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void depositeMoneyToMyAcount(DepositMoneyDTO depositMoneyDTO, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        user.setCredit(depositMoneyDTO.getMoney());
        userRepository.save(user);
    }
}
