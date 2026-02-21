package com.dinesh.demo.controller;

import com.dinesh.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StudentService studentService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalStudents", studentService.findAll().size());
        model.addAttribute("recentEnrollments", 0); // You can add logic to count recent enrollments
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("totalStudents", studentService.findAll().size());
        model.addAttribute("recentEnrollments", 0);
        return "index";
    }
}
