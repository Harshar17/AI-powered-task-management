package com.ai.taskmanagement.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ai.taskmanagement.model.User;
import com.ai.taskmanagement.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final UserRepository userRepository;
	private final JwtService jwtService;

	public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {

		 String path = request.getServletPath();
			
		System.out.println("JWT FILTER PATH: " + path);

		return path.equals("/api/auth/login") || path.equals("/api/auth/register");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);

		String email;
		
		try {
		    email = jwtService.extractEmail(token);

		    System.out.println("========== JWT DEBUG ==========");
		    System.out.println("JWT EMAIL: " + email);

		} catch (Exception e) {

		    System.out.println("========== JWT ERROR ==========");
		    e.printStackTrace();

		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return;
		}

//		try {
//			email = jwtService.extractEmail(token);
//		} catch (Exception e) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			return;
//		}

//		Optional<User> optionalUser = userRepository.findByEmail(email);
//
//		if (optionalUser.isEmpty()) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			return;
//		}
		
		Optional<User> optionalUser = userRepository.findByEmail(email);

		System.out.println("USER FOUND: " + optionalUser.isPresent());

		if (optionalUser.isEmpty()) {

		    System.out.println("USER NOT FOUND FOR EMAIL: " + email);

		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return;
		}

		User user = optionalUser.get();

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),
				null, new ArrayList<>());

		SecurityContextHolder.getContext().setAuthentication(authentication);
		System.out.println("JWT FILTER PASSED: " + request.getRequestURI());
		filterChain.doFilter(request, response);
	}

}
