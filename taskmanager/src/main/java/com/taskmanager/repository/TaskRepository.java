package com.taskmanager.repository;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {


    long countByStatus(String status);
    List<Task> findTop5ByDeadlineAfterOrderByDeadlineAsc(LocalDate date);
    List<Task> findByDeadlineBeforeAndStatusNot(
            LocalDate date,
            String status
    );

    @Query("""
       SELECT t FROM Task t
       WHERE t.status <> 'Hoàn thành'
       AND t.deadline BETWEEN :today AND :limitDate
       ORDER BY t.deadline ASC
       """)
    List<Task> findUpcomingTasks(
            @Param("today") LocalDate today,
            @Param("limitDate") LocalDate limitDate
    );


    // ADMIN tìm kiếm
    Page<Task> findByTitleContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );


    // ADMIN lọc
    Page<Task> findByStatus(
            String status,
            Pageable pageable
    );


    Page<Task> findByPriority(
            String priority,
            Pageable pageable
    );


    Page<Task> findByStatusAndPriority(
            String status,
            String priority,
            Pageable pageable
    );


    // ADMIN tìm kiếm + lọc
    Page<Task> findByTitleContainingIgnoreCaseAndStatus(
            String keyword,
            String status,
            Pageable pageable
    );


    Page<Task> findByTitleContainingIgnoreCaseAndPriority(
            String keyword,
            String priority,
            Pageable pageable
    );


    Page<Task> findByTitleContainingIgnoreCaseAndStatusAndPriority(
            String keyword,
            String status,
            String priority,
            Pageable pageable
    );



    // USER
    Page<Task> findByUser(
            User user,
            Pageable pageable
    );


    Page<Task> findByUserAndTitleContainingIgnoreCase(
            User user,
            String keyword,
            Pageable pageable
    );


    Page<Task> findByUserAndStatus(
            User user,
            String status,
            Pageable pageable
    );


    Page<Task> findByUserAndPriority(
            User user,
            String priority,
            Pageable pageable
    );


    Page<Task> findByUserAndStatusAndPriority(
            User user,
            String status,
            String priority,
            Pageable pageable
    );



    long countByUser(User user);


    long countByUserAndStatus(
            User user,
            String status
    );

    // USER FILTER + SEARCH

    Page<Task> findByUserAndTitleContainingIgnoreCaseAndStatusAndPriority(
            User user,
            String keyword,
            String status,
            String priority,
            Pageable pageable
    );


    Page<Task> findByUserAndTitleContainingIgnoreCaseAndStatus(
            User user,
            String keyword,
            String status,
            Pageable pageable
    );


    Page<Task> findByUserAndTitleContainingIgnoreCaseAndPriority(
            User user,
            String keyword,
            String priority,
            Pageable pageable
    );

    List<Task> findByDeadlineBetweenAndStatusNot(
            LocalDate start,
            LocalDate end,
            String status
    );

    long countByStatusNot(String status);

}