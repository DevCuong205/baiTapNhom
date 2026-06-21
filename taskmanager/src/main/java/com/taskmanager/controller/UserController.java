package com.taskmanager.controller;

import com.taskmanager.entity.User;
import com.taskmanager.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("totalUsers", userRepository.count());
        return "users";
    }

    @GetMapping("/users/new")
    public String showForm(Model model) {

        User user = new User();
        user.setAvatar("default.jpg");

        model.addAttribute("user", user);

        return "user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                           HttpSession session) {

        try {

            String uploadDir =
                    System.getProperty("user.dir")
                            + "/uploads/avatars/";

            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Có upload ảnh mới
            if (avatarFile != null && !avatarFile.isEmpty()) {

                String originalFilename = avatarFile.getOriginalFilename();

                String extension = "";

                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(
                            originalFilename.lastIndexOf(".")
                    );
                }

                String fileName =
                        user.getUsername()
                                + "_"
                                + System.currentTimeMillis()
                                + extension;

                // Xóa avatar cũ nếu đang sửa user
                if (user.getId() != null) {

                    User oldUser =
                            userRepository.findById(user.getId())
                                    .orElse(null);

                    if (oldUser != null
                            && oldUser.getAvatar() != null
                            && !oldUser.getAvatar().equals("default.jpg")) {

                        File oldFile =
                                new File(uploadDir + oldUser.getAvatar());

                        if (oldFile.exists()) {
                            oldFile.delete();
                        }
                    }
                }

                File dest = new File(uploadDir + fileName);

                avatarFile.transferTo(dest);

                user.setAvatar(fileName);

            } else {

                // Không upload ảnh mới
                if (user.getId() != null) {

                    User oldUser =
                            userRepository.findById(user.getId())
                                    .orElse(null);

                    if (oldUser != null) {
                        user.setAvatar(oldUser.getAvatar());
                    }

                } else {

                    user.setAvatar("default.jpg");
                }
            }

            User savedUser = userRepository.save(user);

            // cập nhật session nếu user đang đăng nhập sửa chính mình
            User sessionUser = (User) session.getAttribute("user");

            if (sessionUser != null
                    && sessionUser.getId().equals(savedUser.getId())) {

                session.setAttribute("user", savedUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id,
                           Model model) {

        User user =
                userRepository.findById(id)
                        .orElse(null);

        if (user != null &&
                (user.getAvatar() == null
                        || user.getAvatar().isEmpty())) {

            user.setAvatar("default.jpg");
        }

        model.addAttribute("user", user);

        return "user-form";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        User user =
                userRepository.findById(id)
                        .orElse(null);

        if (user != null
                && user.getAvatar() != null
                && !user.getAvatar().equals("default.jpg")) {

            String uploadDir =
                    System.getProperty("user.dir")
                            + "/uploads/avatars/";

            File avatarFile =
                    new File(uploadDir + user.getAvatar());

            if (avatarFile.exists()) {
                avatarFile.delete();
            }
        }

        userRepository.deleteById(id);

        return "redirect:/users";
    }
}