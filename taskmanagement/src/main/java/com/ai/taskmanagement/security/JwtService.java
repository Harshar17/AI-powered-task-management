package com.ai.taskmanagement.security;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.ai.taskmanagement.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final String secretKey = "mySuperSecretKeyForTaskManagementApplication123456";

	public String generateToken(User user) {
		
		return Jwts.builder()
				.subject(user.getEmail())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+1000*60*60))
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
				.compact();
	}
	

	public String extractEmail(String token) {

		 return Jwts.parser()
	            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
	            .build()
	            .parseSignedClaims(token)
	            .getPayload()
	            .getSubject();
	}
}
