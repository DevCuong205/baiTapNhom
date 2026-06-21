package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/tasks")
    public String listTasks(Model model) {

        model.addAttribute("tasks", taskRepository.findAll());
        model.addAttribute("totalTasks", taskRepository.count());

        return "tasks";
    }

    @GetMapping("/tasks/new")
    public String showForm(Model model) {

        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());

        return "task-form";
    }

    @PostMapping("/tasks/save")
    public String saveTask(@ModelAttribute Task task,
                           @RequestParam(required = false) Long userId) {

        if (userId != null) {

            User user = userRepository.findById(userId).orElse(null);

            task.setUser(user);

        } else {

            task.setUser(null);
        }

        taskRepository.save(task);

        return "redirect:/tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {

        Task task = taskRepository.findById(id).orElse(new Task());

        model.addAttribute("task", task);
        model.addAttribute("users", userRepository.findAll());

        return "task-form";
    }

    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id) {

        taskRepository.deleteById(id);

        return "redirect:/tasks";
    }

    @GetMapping("/tasks/search")
    public String searchTask(@RequestParam("keyword") String keyword,
                             Model model) {

        model.addAttribute(
                "tasks",
                taskRepository.findByTitleContainingIgnoreCase(keyword)
        );

        model.addAttribute(
                "totalTasks",
                taskRepository.findByTitleContainingIgnoreCase(keyword).size()
        );

        return "tasks";
    }
}