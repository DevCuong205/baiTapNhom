package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/tasks")
    public String listTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            Model model,
            HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Task> taskPage;

        if ("ADMIN".equals(loginUser.getRole())) {

            if (keyword != null && !keyword.isBlank()) {
                taskPage = taskRepository.findByTitleContainingIgnoreCase(keyword, pageable);
            } else {
                taskPage = taskRepository.findAll(pageable);
            }

        } else {

            if (keyword != null && !keyword.isBlank()) {
                taskPage = taskRepository.findByUserAndTitleContainingIgnoreCase(loginUser, keyword, pageable);
            } else {
                taskPage = taskRepository.findByUser(loginUser, pageable);
            }
        }

        List<Task> tasks = taskPage.getContent();

        if (status != null && !status.isBlank()) {
            tasks = tasks.stream()
                    .filter(t -> status.equalsIgnoreCase(t.getStatus()))
                    .toList();
        }

        if (priority != null && !priority.isBlank()) {
            tasks = tasks.stream()
                    .filter(t -> priority.equalsIgnoreCase(t.getPriority()))
                    .toList();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTasks", taskPage.getTotalElements());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("size", size);

        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("priority", priority);

        model.addAttribute("todoTasks",
                taskRepository.countByStatus("Chưa làm"));

        model.addAttribute("doingTasks",
                taskRepository.countByStatus("Đang làm"));

        model.addAttribute("completedTasks",
                taskRepository.countByStatus("Hoàn thành"));

        return "tasks";
    }
    @GetMapping("/tasks/new")
    public String showForm(Model model, HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());

        return "task-form";
    }

    @PostMapping("/tasks/save")
    public String saveTask(@ModelAttribute Task task,
                           @RequestParam(required = false) Long userId,
                           HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            task.setUser(user);
        }

        // Tạo mới
        if (task.getId() == null) {
            task.setCreatedAt(LocalDateTime.now());

            if (task.getProgress() == null) {
                task.setProgress(0);
            }

            if (task.getPriority() == null || task.getPriority().isBlank()) {
                task.setPriority("MEDIUM");
            }
        }

        // Tự cập nhật trạng thái theo progress
        if (task.getProgress() != null) {

            if (task.getProgress() == 0) {
                task.setStatus("Chưa làm");
            } else if (task.getProgress() == 100) {
                task.setStatus("Hoàn thành");
            } else {
                task.setStatus("Đang làm");
            }

        }

        task.setUpdatedAt(LocalDateTime.now());

        taskRepository.save(task);

        return "redirect:/tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTask(@PathVariable Long id,
                           Model model,
                           HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            return "redirect:/tasks";
        }

        // USER chỉ được sửa task của mình
        if ("USER".equals(loginUser.getRole())) {

            if (task.getUser() == null ||
                    !task.getUser().getId().equals(loginUser.getId())) {

                return "redirect:/tasks";
            }
        }

        model.addAttribute("task", task);
        model.addAttribute("users", userRepository.findAll());

        return "task-form";
    }

    @PostMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id,
                             HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            return "redirect:/tasks";
        }

        // USER chỉ được xóa task của mình
        if ("USER".equals(loginUser.getRole())) {

            if (task.getUser() == null ||
                    !task.getUser().getId().equals(loginUser.getId())) {

                return "redirect:/tasks";
            }
        }

        taskRepository.delete(task);

        return "redirect:/tasks";
    }
}