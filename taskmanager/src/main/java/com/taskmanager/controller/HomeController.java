package com.taskmanager.controller;

import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("totalTasks",
                taskRepository.count());

        model.addAttribute("totalUsers",
                userRepository.count());

        model.addAttribute("completedTasks",
                taskRepository.countByStatus("Hoàn thành"));

        model.addAttribute("doingTasks",
                taskRepository.countByStatus("Đang làm"));

        model.addAttribute("todoTasks",
                taskRepository.countByStatus("Chưa làm"));

        return "index";
    }
}