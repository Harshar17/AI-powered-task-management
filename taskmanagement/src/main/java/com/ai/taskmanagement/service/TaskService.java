package com.ai.taskmanagement.service;

import java.util.List;

import com.ai.taskmanagement.dto.TaskResponse;
import com.ai.taskmanagement.model.Task;

public interface TaskService {

	TaskResponse createTask(Task task, String email);

	List<TaskResponse> getAllTasks(String email);

	TaskResponse getTaskById(int id, String email);

	TaskResponse updateTask(int id, Task task, String email);

	TaskResponse updateStatus(int id, String status, String email);

	void deleteTask(int id, String email);
}