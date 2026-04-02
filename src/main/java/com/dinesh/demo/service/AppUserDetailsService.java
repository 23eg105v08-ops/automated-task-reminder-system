package com.dinesh.demo.service;

import com.dinesh.demo.model.AppUser;
import com.dinesh.demo.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.auth.username:taskuser}")
    private String defaultUsername;

    @Value("${app.auth.password:Task@123}")
    private String defaultPassword;

    @Value("${app.auth.email:taskuser@example.com}")
    private String defaultEmail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(user.getUsername())
            .password(user.getPassword())
            .roles("USER")
            .disabled(!user.isEnabled())
            .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ensureDefaultUserExists() {
        if (appUserRepository.existsByUsername(defaultUsername)) {
            return;
        }

        AppUser user = new AppUser();
        user.setUsername(defaultUsername);
        user.setEmail(defaultEmail);
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        appUserRepository.save(user);
    }
}
