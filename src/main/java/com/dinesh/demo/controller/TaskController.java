package com.dinesh.demo.controller;

import com.dinesh.demo.model.Task;
import com.dinesh.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @PostMapping("/save")
    public String save(@ModelAttribute Task task,
                       @RequestParam(value = "dueDateStr", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate,
                       @RequestParam(value = "reminderTimeStr", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminderTime) {
        if (dueDate != null) task.setDueDate(dueDate);
        if (reminderTime != null) task.setReminderTime(reminderTime);
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        taskService.findById(id).ifPresent(t -> model.addAttribute("task", t));
        return "tasks/form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        taskService.deleteById(id);
        return "redirect:/tasks";
    }

    @PostMapping("/complete/{id}")
    public String markAsCompleted(@PathVariable Long id) {
        taskService.markAsCompleted(id);
        return "redirect:/tasks";
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
