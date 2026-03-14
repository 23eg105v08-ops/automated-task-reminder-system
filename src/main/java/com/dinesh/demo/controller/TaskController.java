package com.dinesh.demo.controller;

import com.dinesh.demo.model.Task;
import com.dinesh.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("pendingCount", taskService.getPendingTasksCount());
        model.addAttribute("completedCount", taskService.getCompletedTasksCount());
        model.addAttribute("overdueCount", taskService.getOverdueTasksCount());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        taskService.findById(id).ifPresent(t -> model.addAttribute("task", t));
        return "tasks/form";
    }

    @GetMapping("/filter/status/{status}")
    public String filterByStatus(@PathVariable String status, Model model) {
        model.addAttribute("tasks", taskService.findByStatus(status));
        model.addAttribute("currentFilter", status);
        return "tasks/list";
    }

    @GetMapping("/filter/priority/{priority}")
    public String filterByPriority(@PathVariable String priority, Model model) {
        model.addAttribute("tasks", taskService.findByPriority(priority));
        model.addAttribute("currentFilter", priority);
        return "tasks/list";
    }
}
