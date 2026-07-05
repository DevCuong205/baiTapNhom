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
import java.util.Comparator;
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
            Model model,
            HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Task> taskPage;

        if ("ADMIN".equals(loginUser.getRole())) {

            taskPage = taskRepository.findAll(pageable);

        } else {

            taskPage = taskRepository.findByUser(loginUser, pageable);
        }

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("totalTasks", taskPage.getTotalElements());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("size", size);

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

    @GetMapping("/tasks/search")
    public String searchTask(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Task> taskPage;

        if ("ADMIN".equals(loginUser.getRole())) {

            taskPage = taskRepository.findByTitleContainingIgnoreCase(keyword, pageable);

        } else {

            taskPage = taskRepository.findByUser(loginUser, pageable);
        }

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("totalTasks", taskPage.getTotalElements());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        return "tasks";
    }

    @GetMapping("/tasks/filter")
    public String filterTasks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String sort,
            Model model,
            HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        List<Task> tasks = taskRepository.findAll();

        // Tìm kiếm
        if (keyword != null && !keyword.isBlank()) {

            tasks = tasks.stream()
                    .filter(t -> t.getTitle().toLowerCase()
                            .contains(keyword.toLowerCase()))
                    .toList();
        }

        // Trạng thái
        if (status != null && !status.isBlank()) {

            tasks = tasks.stream()
                    .filter(t -> status.equals(t.getStatus()))
                    .toList();
        }

        // Ưu tiên
        if (priority != null && !priority.isBlank()) {

            tasks = tasks.stream()
                    .filter(t -> priority.equals(t.getPriority()))
                    .toList();
        }

        // Sắp xếp deadline
        if ("asc".equals(sort)) {

            tasks = tasks.stream()
                    .sorted(Comparator.comparing(Task::getDeadline))
                    .toList();

        } else if ("desc".equals(sort)) {

            tasks = tasks.stream()
                    .sorted(Comparator.comparing(Task::getDeadline).reversed())
                    .toList();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTasks", tasks.size());

        return "tasks";
    }

}