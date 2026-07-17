package com.taskmanager.service;

import com.taskmanager.entity.ActivityLog;
import com.taskmanager.entity.User;
import com.taskmanager.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    public void save(User user,
                     String action,
                     String description){

        ActivityLog log = new ActivityLog();

        log.setUser(user);
        log.setAction(action);
        log.setDescription(description);
        log.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(log);

    }

}