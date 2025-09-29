package com.todoapp.repository;

import com.todoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Find ALL tasks for a specific user
    List<Task> findByUsername(String username);

    // Find tasks for a specific user AND by completion status
    List<Task> findByUsernameAndCompleted(String username, boolean completed); 

}