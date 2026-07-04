package com.taskmanager.repository;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByStatus(String status);

    List<Task> findByTitleContainingIgnoreCase(String keyword);

    List<Task> findByUser(User user);

    List<Task> findByStatus(String status);

    List<Task> findByPriority(String priority);

    List<Task> findByStatusAndPriority(String status, String priority);

    List<Task> findAllByOrderByDeadlineAsc();

    List<Task> findAllByOrderByDeadlineDesc();

}