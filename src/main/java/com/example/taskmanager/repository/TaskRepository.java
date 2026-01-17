package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByUserUsername(String username);
    Optional<Task> findByIdAndUserUsername(Long id, String username);
    List<Task> findByUserUsernameAndDueDate(String username, LocalDate dueDate);
    List<Task> findByUserUsernameAndDueDateBetween(
            String username,
            LocalDate startDate,
            LocalDate endDate
    );
    List<Task> findByDueDateAndCompleted(LocalDate date, Boolean completed);


}
