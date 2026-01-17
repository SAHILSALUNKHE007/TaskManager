package com.example.taskmanager.controller;


import com.example.taskmanager.entity.Task;
import com.example.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication authentication){
        String username=authentication.getName();
        return taskService.createTask(task, username);

    }

    @GetMapping
    public List<Task> getTasks(Authentication authentication){
        return taskService.getTask(authentication.getName());
    }

    @GetMapping("/updatestatus/{id}")
    public ResponseEntity<Object> updateStatus(@PathVariable long id,@RequestBody Boolean status,Authentication authentication){
        taskService.updateStatus(id, authentication.getName(), status);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/today")
    public List<Task> getTodayTasks(Authentication authentication) {
        return taskService.getTodayTasks(authentication.getName());
    }

    @GetMapping("/range")
    public List<Task> getTasksBetweenDates(
            @RequestParam String startDate,
            @RequestParam String endDate,
            Authentication authentication) {

        return taskService.getTasksBetweenDates(
                authentication.getName(),
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
    }


}
