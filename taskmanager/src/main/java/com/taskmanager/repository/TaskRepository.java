package com.taskmanager.repository;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByStatus(String status);

    Page<Task> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Task> findByUser(User user, Pageable pageable);

    Page<Task> findByUserAndTitleContainingIgnoreCase(User user, String keyword, Pageable pageable);

    Page<Task> findByStatus(String status, Pageable pageable);

    Page<Task> findByPriority(String priority, Pageable pageable);

    Page<Task> findByStatusAndPriority(String status,
                                       String priority,
                                       Pageable pageable);

    List<Task> findAllByOrderByDeadlineAsc();

    List<Task> findAllByOrderByDeadlineDesc();

    long countByUser(User user);

    long countByUserAndStatus(User user, String status);
}