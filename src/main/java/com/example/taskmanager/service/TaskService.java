package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor

public class TaskService {



    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public Task createTask(Task task,String username){
        User user=userRepository.findByUsername(username).orElseThrow();
        task.setUser(user);
        String cacheKey="tasks:"+username.toLowerCase();
        redisTemplate.delete("tasks:" + username.toLowerCase());
        redisTemplate.delete("tasks:today:" + username.toLowerCase());
        return taskRepository.save(task);

    }
    public void updateStatus(Long id,String username,Boolean status){
        Task task=taskRepository.findByIdAndUserUsername(id,username).orElseThrow(() ->
                new RuntimeException("Task not found or not authorized"));
        task.setCompleted(status);
        System.out.println(task.isCompleted());
        redisTemplate.delete("tasks:" + username.toLowerCase());
        redisTemplate.delete("tasks:today:" + username.toLowerCase());
        taskRepository.save(task);

    }

    public List<Task> getTask(String username){
        String cacheKey="tasks:"+username.toLowerCase();
        System.out.println(cacheKey);
        String cachedData = (String)redisTemplate.opsForValue().get(cacheKey);

        if (cachedData != null) {
            System.out.println("Fetching from Redis");
            try {
                return objectMapper.readValue(
                        cachedData,
                        new TypeReference<List<Task>>() {}
                );
            } catch (Exception e) {
                e.printStackTrace(); // never swallow exception
            }
        }
        List<Task> tasks= taskRepository.findByUserUsername(username);
        try{
            redisTemplate.opsForValue().set(
                    cacheKey,
                    objectMapper.writeValueAsString(tasks),
                    10, TimeUnit.MINUTES
            );
        }catch (Exception e){

        }
        return  tasks;
    }



    public List<Task> getTodayTasks(String username) {
        String today=LocalDate.now().toString();
        String cacheKey = "tasks:today:" + username.toLowerCase() + ":" + today;
        String cacheData=(String) redisTemplate.opsForValue().get(cacheKey);
        if(cacheData!=null){
            try{
                return  objectMapper.readValue(
                        cacheData,
                        new TypeReference<List<Task>>() {}
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Task> tasks= taskRepository.findByUserUsernameAndDueDate(
                username,
                LocalDate.now()
        );
        try {
            redisTemplate.opsForValue().set(cacheKey,
                    objectMapper.writeValueAsString(tasks),
                    24,TimeUnit.HOURS);
        }finally {
            return tasks;
        }

    }


    public List<Task> getTasksBetweenDates(
            String username,
            LocalDate start,
            LocalDate end) {

        String cachedKey="task:range:"+username.toLowerCase()+":"+start.toString()+":"+end.toString();
        String cachedData=redisTemplate.opsForValue().get(cachedKey).toString();
        if(cachedData!=null){
            try{
                return objectMapper.readValue(
                        cachedData,
                        new TypeReference<List<Task>>() {}
                );
            }catch (Exception e){

            }
        }
        List<Task> tasks= taskRepository
                .findByUserUsernameAndDueDateBetween(username, start, end);
        try{
            redisTemplate.opsForValue().set(
                    cachedKey,
                    objectMapper.writeValueAsString(tasks),
                    24,
                    TimeUnit.HOURS
            );
        }catch (Exception e){

        }
        return  tasks;
    }

}
