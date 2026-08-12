package com.ai.taskmanagement.service;

import com.ai.taskmanagement.dto.LoginResponse;
import com.ai.taskmanagement.model.User;

public interface UserService {
	
	User registerUser(User user);
	
	LoginResponse loginUser(String email,String password);

}
