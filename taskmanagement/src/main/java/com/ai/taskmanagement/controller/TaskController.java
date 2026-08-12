package com.ai.taskmanagement.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.taskmanagement.dto.TaskResponse;
import com.ai.taskmanagement.model.Task;
import com.ai.taskmanagement.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody Task task){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return taskService.createTask(task, email);
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return taskService.getAllTasks(email);
    }
    
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable int id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return taskService.getTaskById(id, email);
    }
    
    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable int id,
            @Valid @RequestBody Task task) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return taskService.updateTask(id, task, email);
    }
    
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable int id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        taskService.deleteTask(id, email);

        return "Task deleted successfully";
    }
    
    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(
            @PathVariable int id,
            @RequestParam String status,
            Authentication authentication) {

        return taskService.updateStatus(
                id,
                status,
                authentication.getName()
        );
    }
 
}