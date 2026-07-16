package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import java.time.LocalDate;
import java.util.List;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
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
    public String home(Model model, HttpSession session) {


        User loginUser = (User) session.getAttribute("user");


        if(loginUser == null){
            return "redirect:/login";
        }


        long totalTasks;
        long completedTasks;
        long doingTasks;
        long todoTasks;

        LocalDate today = LocalDate.now();

        LocalDate after30Days = today.plusDays(30);

// Công việc sắp hết hạn (30 ngày tới)
        List<Task> upcomingTasks =
                taskRepository.findByDeadlineBetweenAndStatusNot(
                        today,
                        after30Days,
                        "Hoàn thành"
                );

// Công việc quá hạn
        List<Task> overdueTasks =
                taskRepository.findByDeadlineBeforeAndStatusNot(
                        today,
                        "Hoàn thành"
                );

// Tổng số task chưa hoàn thành
        long unCompletedTasks =
                taskRepository.countByStatusNot("Hoàn thành");

        model.addAttribute("upcomingTasks", upcomingTasks);
        model.addAttribute("overdueTasks", overdueTasks);
        model.addAttribute("unCompletedTasks", unCompletedTasks);


        /*
            ADMIN:
            Xem toàn bộ hệ thống
        */
        if("ADMIN".equals(loginUser.getRole())){


            totalTasks = taskRepository.count();

            completedTasks =
                    taskRepository.countByStatus("Hoàn thành");

            doingTasks =
                    taskRepository.countByStatus("Đang làm");

            todoTasks =
                    taskRepository.countByStatus("Chưa làm");


            model.addAttribute(
                    "totalUsers",
                    userRepository.count()
            );


        }


        /*
            USER:
            Chỉ xem task của mình
        */
        else {


            totalTasks =
                    taskRepository.countByUser(loginUser);


            completedTasks =
                    taskRepository.countByUserAndStatus(
                            loginUser,
                            "Hoàn thành"
                    );


            doingTasks =
                    taskRepository.countByUserAndStatus(
                            loginUser,
                            "Đang làm"
                    );


            todoTasks =
                    taskRepository.countByUserAndStatus(
                            loginUser,
                            "Chưa làm"
                    );


        }



        model.addAttribute(
                "totalTasks",
                totalTasks
        );


        model.addAttribute(
                "completedTasks",
                completedTasks
        );


        model.addAttribute(
                "doingTasks",
                doingTasks
        );


        model.addAttribute(
                "todoTasks",
                todoTasks
        );


        model.addAttribute(
                "loginUser",
                loginUser
        );

        return "index";
    }

}