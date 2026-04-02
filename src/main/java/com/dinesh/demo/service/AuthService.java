package com.dinesh.demo.service;

import com.dinesh.demo.model.AppUser;
import com.dinesh.demo.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationResult register(String username, String email, String password, String confirmPassword) {
        String normalizedUsername = username == null ? "" : username.trim();
        String normalizedEmail = email == null ? "" : email.trim();

        if (normalizedUsername.length() < 3) {
            return RegistrationResult.failure("Username must be at least 3 characters.");
        }

        if (password == null || password.length() < 6) {
            return RegistrationResult.failure("Password must be at least 6 characters.");
        }

        if (!password.equals(confirmPassword)) {
            return RegistrationResult.failure("Password and confirm password do not match.");
        }

        if (appUserRepository.existsByUsername(normalizedUsername)) {
            return RegistrationResult.failure("Username is already taken.");
        }

        if (!normalizedEmail.isBlank() && appUserRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            return RegistrationResult.failure("Email is already registered.");
        }

        AppUser user = new AppUser();
        user.setUsername(normalizedUsername);
        user.setEmail(normalizedEmail.isBlank() ? null : normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        appUserRepository.save(user);

        return RegistrationResult.successResult();
    }

    public record RegistrationResult(boolean success, String message) {
        public static RegistrationResult successResult() {
            return new RegistrationResult(true, "Registration successful. Please sign in.");
        }

        public static RegistrationResult failure(String message) {
            return new RegistrationResult(false, message);
        }
    }
}
