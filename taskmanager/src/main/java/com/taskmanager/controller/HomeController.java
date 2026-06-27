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

        long totalTasks = taskRepository.count();
        long totalUsers = userRepository.count();

        long completedTasks = taskRepository.countByStatus("Hoàn thành");
        long doingTasks = taskRepository.countByStatus("Đang làm");
        long todoTasks = taskRepository.countByStatus("Chưa làm");

        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("doingTasks", doingTasks);
        model.addAttribute("todoTasks", todoTasks);

        return "index";
    }

}