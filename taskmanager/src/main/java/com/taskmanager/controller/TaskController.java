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

import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/tasks")
    public String listTasks(Model model, HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        List<Task> tasks;

        if ("ADMIN".equals(loginUser.getRole())) {
            tasks = taskRepository.findAll();
        } else {
            tasks = taskRepository.findByUser(loginUser);
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTasks", tasks.size());

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
        } else {
            task.setUser(null);
        }

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
    public String searchTask(@RequestParam String keyword,
                             Model model,
                             HttpSession session) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        List<Task> tasks;

        if ("ADMIN".equals(loginUser.getRole())) {

            tasks = taskRepository.findByTitleContainingIgnoreCase(keyword);

        } else {

            tasks = taskRepository.findByUser(loginUser)
                    .stream()
                    .filter(task -> task.getTitle().toLowerCase()
                            .contains(keyword.toLowerCase()))
                    .toList();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTasks", tasks.size());
        model.addAttribute("keyword", keyword);

        return "tasks";
    }

}