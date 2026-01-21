package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    long countByUserUsername(String username);
    long countByUserUsernameAndCompleted(String username,Boolean completed);

    @Query(""" 
                Select Count(t)
                From Task t
                where t.user.username= :username
                and t.completed=false
                and t.dueDate< :today
                    """)
    long countOverdueTask(@Param("username") String username, @Param("today") LocalDate today);


}
