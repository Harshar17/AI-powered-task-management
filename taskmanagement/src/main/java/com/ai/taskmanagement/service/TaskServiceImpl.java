package com.ai.taskmanagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ai.taskmanagement.ai.AiService;
import com.ai.taskmanagement.dto.AiResponse;
import com.ai.taskmanagement.dto.AiSubtask;
import com.ai.taskmanagement.dto.TaskResponse;
import com.ai.taskmanagement.exception.TaskNotFoundException;
import com.ai.taskmanagement.model.Task;
import com.ai.taskmanagement.model.User;
import com.ai.taskmanagement.repository.TaskRepository;
import com.ai.taskmanagement.repository.UserRepository;

@Service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	public TaskServiceImpl(
	        TaskRepository taskRepository,
	        UserRepository userRepository) {

	    this.taskRepository = taskRepository;
	    this.userRepository = userRepository;
	}

	@Override
	public TaskResponse createTask(Task task, String email) {

		Optional<User> optionalUser = userRepository.findByEmail(email);

		if (optionalUser.isEmpty()) {
			throw new RuntimeException("User not found");
		}

		User user = optionalUser.get();

		task.setUser(user);
		task.setCreatedAt(LocalDateTime.now());

		Task savedTask = taskRepository.save(task);

		TaskResponse response = new TaskResponse();

		response.setId(savedTask.getId());
		response.setTitle(savedTask.getTitle());
		response.setDescription(savedTask.getDescription());
		response.setStatus(savedTask.getStatus());
		response.setPriority(savedTask.getPriority());
		response.setCreatedAt(savedTask.getCreatedAt());
		response.setDueDate(savedTask.getDueDate());

		response.setUserId(user.getId());
		response.setUserName(user.getName());
		response.setUserEmail(user.getEmail());

		return response;
	}

	@Override
	public List<TaskResponse> getAllTasks(String email) {

		List<Task> tasks = taskRepository.findByUserEmail(email);

		List<TaskResponse> responses = new ArrayList<>();

		for (Task task : tasks) {

			TaskResponse response = new TaskResponse();

			response.setId(task.getId());
			response.setTitle(task.getTitle());
			response.setDescription(task.getDescription());
			response.setStatus(task.getStatus());
			response.setPriority(task.getPriority());
			response.setCreatedAt(task.getCreatedAt());
			response.setDueDate(task.getDueDate());

			response.setUserId(task.getUser().getId());
			response.setUserName(task.getUser().getName());
			response.setUserEmail(task.getUser().getEmail());

			responses.add(response);
		}

		return responses;
	}

	@Override
	public TaskResponse getTaskById(int id, String email) {

		Optional<Task> optionalTask = taskRepository.findByIdAndUser_Email(id, email);

		if (optionalTask.isEmpty()) {
			throw new TaskNotFoundException("Task not found");
		}

		Task task = optionalTask.get();

		TaskResponse response = new TaskResponse();

		response.setId(task.getId());
		response.setTitle(task.getTitle());
		response.setDescription(task.getDescription());
		response.setStatus(task.getStatus());
		response.setPriority(task.getPriority());
		response.setCreatedAt(task.getCreatedAt());
		response.setDueDate(task.getDueDate());

		response.setUserId(task.getUser().getId());
		response.setUserName(task.getUser().getName());
		response.setUserEmail(task.getUser().getEmail());

		return response;
	}

	@Override
	public TaskResponse updateTask(int id, Task task, String email) {

		Optional<Task> optionalTask = taskRepository.findByIdAndUser_Email(id, email);

		if (optionalTask.isEmpty()) {
			throw new TaskNotFoundException("Task not found");
		}

		Task existingTask = optionalTask.get();

		existingTask.setTitle(task.getTitle());
		existingTask.setDescription(task.getDescription());
		existingTask.setStatus(task.getStatus());
		existingTask.setPriority(task.getPriority());
		existingTask.setDueDate(task.getDueDate());

		Task updatedTask = taskRepository.save(existingTask);

		TaskResponse response = new TaskResponse();

		response.setId(updatedTask.getId());
		response.setTitle(updatedTask.getTitle());
		response.setDescription(updatedTask.getDescription());
		response.setStatus(updatedTask.getStatus());
		response.setPriority(updatedTask.getPriority());
		response.setCreatedAt(updatedTask.getCreatedAt());
		response.setDueDate(updatedTask.getDueDate());

		response.setUserId(updatedTask.getUser().getId());
		response.setUserName(updatedTask.getUser().getName());
		response.setUserEmail(updatedTask.getUser().getEmail());

		return response;
	}
	
	@Override
	public TaskResponse updateStatus(int id, String status, String email) {

	    Optional<Task> optionalTask =
	            taskRepository.findByIdAndUser_Email(id, email);

	    if (optionalTask.isEmpty()) {
	        throw new TaskNotFoundException("Task not found");
	    }

	    Task task = optionalTask.get();

	    task.setStatus(status);

	    Task savedTask = taskRepository.save(task);

	    TaskResponse response = new TaskResponse();

	    response.setId(savedTask.getId());
	    response.setTitle(savedTask.getTitle());
	    response.setDescription(savedTask.getDescription());
	    response.setStatus(savedTask.getStatus());
	    response.setPriority(savedTask.getPriority());
	    response.setCreatedAt(savedTask.getCreatedAt());
	    response.setDueDate(savedTask.getDueDate());

	    response.setUserId(savedTask.getUser().getId());
	    response.setUserName(savedTask.getUser().getName());
	    response.setUserEmail(savedTask.getUser().getEmail());

	    return response;
	}

	@Override
	public void deleteTask(int id, String email) {

		Optional<Task> optionalTask = taskRepository.findByIdAndUser_Email(id, email);

		if (optionalTask.isEmpty()) {
			throw new TaskNotFoundException("Task not found");
		}

		taskRepository.deleteById(id);
	}
}