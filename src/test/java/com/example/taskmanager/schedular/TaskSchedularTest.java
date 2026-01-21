package com.example.taskmanager.schedular;

import com.example.taskmanager.dto.EmailDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.EmailEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskSchedularTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EmailEventProducer emailEventProducer;

    @Mock
    private RedisTemplate<String, Boolean> redisTemplate;

    @Mock
    private ValueOperations<String, Boolean> valueOperations;

    @InjectMocks
    private TaskSchedular taskSchedular;

    @Test
    void shouldSendKafkaEventForTodayRemainingTasks() {

        // ---------- GIVEN ----------

        User user = new User();
        user.setId(1L);
        user.setUsername("sahil");
        user.setEmail("sahil@gmail.com");

        Task task = new Task();
        task.setId(1L);
        task.setName("Finish Assignment");
        task.setCompleted(false);
        task.setDueDate(LocalDate.now());
        task.setUser(user);

        when(taskRepository.findByDueDateAndCompleted(
                LocalDate.now(), false))
                .thenReturn(List.of(task));

        when(redisTemplate.hasKey(any())).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // ---------- WHEN ----------
        taskSchedular.todayRemaningTask();

        // ---------- THEN ----------
        verify(emailEventProducer, times(1))
                .sendEmailEvent(any(EmailDTO.class));

        verify(valueOperations, times(1))
                .set(any(), eq(true), eq(24L), any());
    }
}
