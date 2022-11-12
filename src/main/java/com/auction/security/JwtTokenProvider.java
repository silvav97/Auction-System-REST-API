package com.auction.security;

import java.util.Date;

import com.auction.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auction.exception.AuctionSystemException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-milliseconds}")
	private String jwtExpirationInMs;
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expirationDate = new Date(currentDate.getTime() + Long.parseLong(jwtExpirationInMs));
		
		String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		
		return token;	
	}
	
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			throw new AuctionSystemException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
		} catch (MalformedJwtException e) {
			throw new AuctionSystemException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
		} catch (ExpiredJwtException e) {
			throw new AuctionSystemException(HttpStatus.BAD_REQUEST, "Expired JWT token");
		} catch (UnsupportedJwtException e) {
			throw new AuctionSystemException(HttpStatus.BAD_REQUEST, "JWT token not supported");
		} catch (IllegalArgumentException e) {
			throw new AuctionSystemException(HttpStatus.BAD_REQUEST, "claims JWT string is empty");
		}
	}
	

}
