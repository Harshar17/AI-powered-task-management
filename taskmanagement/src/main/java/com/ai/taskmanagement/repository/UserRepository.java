package com.ai.taskmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.taskmanagement.model.Task;
import com.ai.taskmanagement.model.User;

public interface UserRepository  extends JpaRepository<User, Integer>{

	boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String emial);
}
