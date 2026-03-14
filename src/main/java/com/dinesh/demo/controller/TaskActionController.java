package com.dinesh.demo.controller;

import com.dinesh.demo.model.Task;
import com.dinesh.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskActionController {

    private final TaskService taskService;

    @PostMapping("/save")
    public String save(@ModelAttribute Task task,
                       @RequestParam(value = "dueDateStr", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate,
                       @RequestParam(value = "reminderTimeStr", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminderTime) {
        if (dueDate != null) {
            task.setDueDate(dueDate);
        }
        if (reminderTime != null) {
            task.setReminderTime(reminderTime);
        }
        taskService.save(task);
        return "redirect:/tasks";
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
}
