package com.ai.taskmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.taskmanagement.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	List<Task> findByUserEmail(String email);
	
	Optional<Task> findByIdAndUser_Email(int id, String email);
	
}