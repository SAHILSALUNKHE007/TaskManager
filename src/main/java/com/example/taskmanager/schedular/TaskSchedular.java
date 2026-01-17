package com.example.taskmanager.schedular;


import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskSchedular {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 20 * * *")
    public void todayRemaningTask(){
        List<Task> tasks=taskRepository.findByDueDateAndCompleted(LocalDate.now(),false);

        Map<User,List<Task>> usertasks=tasks.
                stream().collect(
                        Collectors.groupingBy(
                                Task::getUser
                        )
                );

        usertasks.forEach((user,task)->{
                String email=user.getEmail();
                String emailbody=buildEmailBody(task);
                emailService.sendTaskReminder(
                        email,
                        "Today Task Pending",
                        emailbody
                );

        });

    }

    String buildEmailBody(List<Task> tasks){
        StringBuilder body = new StringBuilder();
        body.append("You have pending tasks for today:\n\n");

        for (Task task : tasks) {
            body.append("- ").append(task.getName()).append("\n");
        }

        body.append("\nPlease complete them on time.");

        return body.toString();

    }
}
