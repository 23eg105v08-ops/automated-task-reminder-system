package com.dinesh.demo.controller;

import com.dinesh.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final TaskService taskService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalTasks", taskService.getTotalTasksCount());
        model.addAttribute("pendingTasks", taskService.getPendingTasksCount());
        model.addAttribute("completedTasks", taskService.getCompletedTasksCount());
        model.addAttribute("overdueTasks", taskService.getOverdueTasksCount());
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("totalTasks", taskService.getTotalTasksCount());
        model.addAttribute("pendingTasks", taskService.getPendingTasksCount());
        model.addAttribute("completedTasks", taskService.getCompletedTasksCount());
        model.addAttribute("overdueTasks", taskService.getOverdueTasksCount());
        return "index";
    }
}
