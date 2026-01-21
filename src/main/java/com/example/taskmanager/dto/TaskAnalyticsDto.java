package com.example.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAnalyticsDto {

    private  long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long overdueTasks;
}
