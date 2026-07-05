package com.taskmanager.controller;

import com.taskmanager.entity.User;
import com.taskmanager.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;

@Controller
public class UserController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    private boolean isAdmin(HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        return loginUser != null && "ADMIN".equals(loginUser.getRole());
    }

    @GetMapping("/users")
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/";
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<User> userPage = userRepository.findAll(pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("totalUsers", userPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("size", size);

        return "users";
    }

    @GetMapping("/users/new")
    public String showForm(Model model, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/";
        }

        User user = new User();
        user.setAvatar("default.jpg");

        model.addAttribute("user", user);

        return "user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                           HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/";
        }

        try {

            String uploadDir = System.getProperty("user.dir") + "/uploads/avatars/";

            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (avatarFile != null && !avatarFile.isEmpty()) {

                String originalFilename = avatarFile.getOriginalFilename();

                String extension = "";

                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                String fileName = user.getUsername() + "_" + System.currentTimeMillis() + extension;

                if (user.getId() != null) {

                    User oldUser = userRepository.findById(user.getId()).orElse(null);

                    if (oldUser != null &&
                            oldUser.getAvatar() != null &&
                            !oldUser.getAvatar().equals("default.jpg")) {

                        File oldFile = new File(uploadDir + oldUser.getAvatar());

                        if (oldFile.exists()) {
                            oldFile.delete();
                        }
                    }
                }

                File dest = new File(uploadDir + fileName);

                avatarFile.transferTo(dest);

                user.setAvatar(fileName);

            } else {

                if (user.getId() != null) {

                    User oldUser = userRepository.findById(user.getId()).orElse(null);

                    if (oldUser != null) {
                        user.setAvatar(oldUser.getAvatar());
                    }

                } else {

                    user.setAvatar("default.png");
                }
            }

            if (user.getId() == null) {

                user.setPassword(passwordEncoder.encode(user.getPassword()));

            } else {

                User oldUser = userRepository.findById(user.getId()).orElse(null);

                if (oldUser != null) {

                    if (!user.getPassword().equals(oldUser.getPassword())) {

                        user.setPassword(passwordEncoder.encode(user.getPassword()));

                    }
                }
            }

            User savedUser = userRepository.save(user);

            User sessionUser = (User) session.getAttribute("user");

            if (sessionUser != null &&
                    sessionUser.getId().equals(savedUser.getId())) {

                session.setAttribute("user", savedUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id,
                           Model model,
                           HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/";
        }

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return "redirect:/users";
        }

        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar("default.jpg");
        }

        model.addAttribute("user", user);

        return "user-form";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/";
        }

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return "redirect:/users";
        }

        // Không cho phép xóa chính mình
        User loginUser = (User) session.getAttribute("user");

        if (loginUser.getId().equals(user.getId())) {
            return "redirect:/users";
        }

        if (user.getAvatar() != null &&
                !user.getAvatar().equals("default.jpg")) {

            String uploadDir = System.getProperty("user.dir") + "/uploads/avatars/";

            File avatarFile = new File(uploadDir + user.getAvatar());

            if (avatarFile.exists()) {
                avatarFile.delete();
            }
        }

        userRepository.delete(user);

        return "redirect:/users";
    }

    @GetMapping("/users/search")
    public String searchUser(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/";
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<User> results =
                userRepository.findByFullnameContainingIgnoreCaseOrUsernameContainingIgnoreCase(
                        keyword,
                        keyword,
                        pageable
                );

        model.addAttribute("users", results.getContent());
        model.addAttribute("totalUsers", results.getTotalElements());
        model.addAttribute("keyword", keyword);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("size", size);

        return "users";
    }
}