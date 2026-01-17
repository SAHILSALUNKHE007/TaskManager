package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TaskService {



    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public Task createTask(Task task,String username){
        User user=userRepository.findByUsername(username).orElseThrow();
        task.setUser(user);
        return taskRepository.save(task);

    }
    public void updateStatus(Long id,String username,Boolean status){
        Task task=taskRepository.findByIdAndUserUsername(id,username).orElseThrow(() ->
                new RuntimeException("Task not found or not authorized"));
        task.setCompleted(status);

    }

    public List<Task> getTask(String username){
        String cachekey="tasks:"+username;
        String cachedData=redisTemplate.opsForValue().get(cachekey);
        if(cachedData==null){

        }
        return taskRepository.findByUserUsername(username);
    }



    public List<Task> getTodayTasks(String username) {
        return taskRepository.findByUserUsernameAndDueDate(
                username,
                LocalDate.now()
        );
    }


    public List<Task> getTasksBetweenDates(
            String username,
            LocalDate start,
            LocalDate end) {

        return taskRepository
                .findByUserUsernameAndDueDateBetween(username, start, end);
    }

}
