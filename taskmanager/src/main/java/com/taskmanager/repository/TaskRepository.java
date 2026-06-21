package com.taskmanager.repository;

import com.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByStatus(String status);

    Collection<Object> findByTitleContainingIgnoreCase(String keyword);
}