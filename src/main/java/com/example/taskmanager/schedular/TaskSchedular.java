package com.example.taskmanager.schedular;


import com.example.taskmanager.dto.EmailDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.EmailEventProducer;
import com.example.taskmanager.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskSchedular {

    private final TaskRepository taskRepository;
    private final EmailEventProducer emailEventProducer;
    private final RedisTemplate redisTemplate;
    private final EmailService emailService;



    @Scheduled(cron = "0 05 14 * * *")
    public void todayRemaningTask(){
        List<Task> tasks=taskRepository.findByDueDateAndCompleted(LocalDate.now(),false);

        Map<User,List<Task>> usertasks=tasks.
                stream().collect(
                        Collectors.groupingBy(
                                Task::getUser
                        )
                );

        usertasks.forEach((user,task)->{
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                return; // do nothing
            }
                String key="email:daily:"+user.getUsername();
                boolean alreadySent=redisTemplate.hasKey(key);
                if (Boolean.FALSE.equals(alreadySent)) {
                    String email=user.getEmail();
                    EmailDTO emailDTO=new EmailDTO(
                            email,
                            "Today Pending Tasks",
                            task.stream().map(Task::getName).toList()

                    );
                    try {
                        emailEventProducer.sendEmailEvent(emailDTO);
                    }catch (Exception e){
                        emailService.sendTaskReminder(emailDTO.getEmail(),emailDTO.getSubject(),emailDTO.getTaskList().toString());
                    }
                    redisTemplate.opsForValue().set(key,"true",24, TimeUnit.HOURS);
                }


        });

    }


}
