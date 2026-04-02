package com.dinesh.demo.controller;

import com.dinesh.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null
            && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/signin")
    public String signin(Authentication authentication) {
        return login(authentication);
    }

    @GetMapping("/signup")
    public String signup(Authentication authentication) {
        if (authentication != null
            && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@RequestParam String username,
                           @RequestParam(required = false) String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        AuthService.RegistrationResult result = authService.register(username, email, password, confirmPassword);
        if (result.success()) {
            return "redirect:/login?registered";
        }

        model.addAttribute("registerError", result.message());
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        return "signup";
    }
}
