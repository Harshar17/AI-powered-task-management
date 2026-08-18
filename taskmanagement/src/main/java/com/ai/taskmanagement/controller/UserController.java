package com.ai.taskmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.taskmanagement.dto.LoginRequest;
import com.ai.taskmanagement.dto.LoginResponse;
import com.ai.taskmanagement.dto.RegisterRequest;
import com.ai.taskmanagement.dto.UserResponse;
import com.ai.taskmanagement.model.User;
import com.ai.taskmanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	private final UserService userservice;
	
	public UserController(UserService userService) {
		this.userservice=userService;
	}
	
	
	@PostMapping("/register")
	public UserResponse register(@Valid @RequestBody RegisterRequest request ) {
		
		User user=new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		
		User savedUser=userservice.registerUser(user);
		
		UserResponse response=new UserResponse();
		response.setId(savedUser.getId());
		response.setName(savedUser.getName());
		response.setEmail(savedUser.getEmail());
		response.setCreatedAt(savedUser.getCreatedAt());
		
		return response;
	}
	
	@PostMapping("/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {
		
		LoginResponse response=userservice.loginUser(request.getEmail(),request.getPassword());
		
		return response;
	}
	
//	@GetMapping("/test")
//	public String test() {
//	    return "JWT authentication successful";
//	}

}
