package com.ai.taskmanagement.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ai.taskmanagement.dto.LoginResponse;
import com.ai.taskmanagement.exception.EmailAlreadyExistsException;
import com.ai.taskmanagement.exception.InvalidCredentialsException;
import com.ai.taskmanagement.model.User;
import com.ai.taskmanagement.repository.UserRepository;
import com.ai.taskmanagement.security.JwtService;

@Service
public class UserServiceImpl implements UserService{

	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder, JwtService jwtService ) {
		this.userRepository=userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}
	
	@Override
	public User registerUser(User user) {
		
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new EmailAlreadyExistsException("Email already registered");
		}
		
		user.setCreatedAt(LocalDateTime.now());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public LoginResponse loginUser(String email, String password) {

	    System.out.println("========== LOGIN DEBUG ==========");
	    System.out.println("Login email: " + email);

	    Optional<User> optional = userRepository.findByEmail(email);

	    if (optional.isEmpty()) {

	        System.out.println("USER NOT FOUND");

	        throw new InvalidCredentialsException(
	                "Invalid email or password"
	        );
	    }

	    User user = optional.get();

	    System.out.println("USER FOUND: " + user.getEmail());
	    System.out.println("Stored password starts with: "
	            + user.getPassword().substring(0,
	                    Math.min(10, user.getPassword().length())));

	    boolean passwordMatches =
	            passwordEncoder.matches(password, user.getPassword());

	    System.out.println("PASSWORD MATCH: " + passwordMatches);

	    if (!passwordMatches) {

	        System.out.println("PASSWORD DOES NOT MATCH");

	        throw new InvalidCredentialsException(
	                "Invalid email or password"
	        );
	    }

	    System.out.println("PASSWORD MATCHED");
	    System.out.println("GENERATING JWT...");

	    String token = jwtService.generateToken(user);

	    System.out.println("JWT GENERATED");

	    LoginResponse response = new LoginResponse();
	    response.setToken(token);

	    return response;
	}
}
