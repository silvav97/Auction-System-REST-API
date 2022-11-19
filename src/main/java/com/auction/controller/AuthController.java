package com.auction.controller;

import java.util.Collections;

import com.auction.common.ApiResponse;
import com.auction.exception.EmailAlreadyExistException;
import com.auction.exception.UsernameAlreadyExistException;
import com.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dto.LoginDTO;
import com.auction.dto.SignupDTO;
import com.auction.entity.Role;
import com.auction.entity.User;
import com.auction.repository.RoleRepository;
import com.auction.repository.UserRepository;
import com.auction.security.JwtAuthResponseDTO;
import com.auction.security.JwtTokenProvider;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponseDTO> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// Get the token from jwtTokenProvider
		String token = jwtTokenProvider.generateToken(authentication);
		return new ResponseEntity<>(new JwtAuthResponseDTO(token) ,HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> signupUser(@Valid @RequestBody SignupDTO signupDTO) {
		userService.save(signupDTO);
		return new ResponseEntity<>(new ApiResponse(true, "User registered successfully"), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/signupadmin")
	public ResponseEntity<ApiResponse> signupAdmin(@Valid @RequestBody SignupDTO signupDTO) {
		userService.saveAdmin(signupDTO);
		return new ResponseEntity<>(new ApiResponse(true, "Admin registered successfully"), HttpStatus.CREATED);
	}

}
