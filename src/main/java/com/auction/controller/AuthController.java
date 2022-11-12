package com.auction.controller;

import java.util.Collections;

import com.auction.common.ApiResponse;
import com.auction.exception.EmailAlreadyExistException;
import com.auction.exception.UsernameAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Get the token from jwtTokenProvider
		String token = jwtTokenProvider.generateToken(authentication);
		
		return new ResponseEntity<>(new JwtAuthResponseDTO(token) ,HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> signupUser(@RequestBody SignupDTO signupDTO) {
		if(userRepository.existsByUsername(signupDTO.getUsername())) {
			throw new UsernameAlreadyExistException(signupDTO.getUsername());
		}
		if(userRepository.existsByEmail(signupDTO.getEmail())) {
			throw new EmailAlreadyExistException(signupDTO.getEmail());
		}
		User user = new User();
		user.setName(signupDTO.getName());
		user.setUsername(signupDTO.getUsername());
		user.setEmail(signupDTO.getEmail());
		user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
		
		Role role = roleRepository.findByName("ROLE_USER").get();
		user.setRoles(Collections.singleton(role));
		
		userRepository.save(user);
		return new ResponseEntity<>(new ApiResponse(true, "User registered successfully"), HttpStatus.CREATED);
	}
	

}
