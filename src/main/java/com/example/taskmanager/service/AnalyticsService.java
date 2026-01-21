package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskAnalyticsDto;
import com.example.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TaskRepository taskRepository;

    public TaskAnalyticsDto getAnalytics(String username){
        long total=taskRepository.countByUserUsername(username);
        long completed = taskRepository.countByUserUsernameAndCompleted(username, true);
        long pending = taskRepository.countByUserUsernameAndCompleted(username, false);
        long overdue = taskRepository.countOverdueTask(username, LocalDate.now());
        return  new TaskAnalyticsDto(total,completed,pending,overdue);

    }
}
