package com.taskmanager.controller;

import com.taskmanager.entity.ActivityLog;
import com.taskmanager.entity.User;
import com.taskmanager.repository.ActivityLogRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActivityLogController {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @GetMapping("/activity")
    public String activity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session,
            Model model) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(loginUser.getRole())) {
            return "redirect:/";
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<ActivityLog> logPage =
                activityLogRepository.findAllByOrderByCreatedAtDesc(pageable);

        model.addAttribute("logs", logPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", logPage.getTotalPages());

        return "activity";
    }
}