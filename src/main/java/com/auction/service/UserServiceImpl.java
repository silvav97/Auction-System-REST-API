package com.auction.service;

import com.auction.dto.DepositMoneyDTO;
import com.auction.dto.SignupDTO;
import com.auction.dto.UpdateUserInformationDTO;
import com.auction.entity.Role;
import com.auction.entity.User;
import com.auction.exception.EmailAlreadyExistException;
import com.auction.exception.UsernameAlreadyExistException;
import com.auction.repository.RoleRepository;
import com.auction.repository.UserRepository;
import com.auction.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static com.auction.util.MyMappers.mapFromSignDTOToUser;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public void save(SignupDTO signupDTO) {
        if(userRepository.existsByUsername(signupDTO.getUsername())) throw new UsernameAlreadyExistException(signupDTO.getUsername());
        if(userRepository.existsByEmail(signupDTO.getEmail())) throw new EmailAlreadyExistException(signupDTO.getEmail());
        Role role = roleRepository.findByName("ROLE_USER").get();
        User user = mapFromSignDTOToUser(signupDTO, role);
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void saveAdmin(SignupDTO signupDTO) {
        if(userRepository.existsByUsername(signupDTO.getUsername())) throw new UsernameAlreadyExistException(signupDTO.getUsername());
        if(userRepository.existsByEmail(signupDTO.getEmail()))  throw new EmailAlreadyExistException(signupDTO.getEmail());
        Role role = roleRepository.findByName("ROLE_ADMIN").get();
        User user = mapFromSignDTOToUser(signupDTO, role);
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        userRepository.save(user);
    }



    @Override
    public void depositeMoneyToMyAcount(DepositMoneyDTO depositMoneyDTO, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        user.setCredit(depositMoneyDTO.getMoney());
        userRepository.save(user);
    }

    @Override
    public void updateUserInformation(UpdateUserInformationDTO updateUserInformationDTO, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        if(userRepository.existsByEmail(updateUserInformationDTO.getEmail()) && !updateUserInformationDTO.getEmail().equals(user.getEmail())) {
            throw new EmailAlreadyExistException(updateUserInformationDTO.getEmail());
        }
        user.setAddress(updateUserInformationDTO.getAddress());
        user.setCellPhone(updateUserInformationDTO.getCellPhone());
        user.setCity(updateUserInformationDTO.getCity());
        user.setDocumentNumber(updateUserInformationDTO.getDocumentNumber());
        user.setEmail(updateUserInformationDTO.getEmail());
        userRepository.save(user);
    }




}
