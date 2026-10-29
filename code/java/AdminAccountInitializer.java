package com.cleangoa.cleangoa;

import com.cleangoa.cleangoa.entity.User;
import com.cleangoa.cleangoa.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminAccountInitializer {

@Bean
CommandLineRunner createAdmin(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder) {

    return args -> {

        String adminEmail = System.getenv("ADMIN_EMAIL");
        String adminPassword = System.getenv("ADMIN_PASSWORD");

        if (adminEmail == null || adminPassword == null
                || adminEmail.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException(
                    "Set ADMIN_EMAIL and ADMIN_PASSWORD before starting the application.");
        }

        adminEmail = adminEmail.trim().toLowerCase();

        if (userRepository.existsByEmail(adminEmail)) {
            User existingUser = userRepository.findByEmail(adminEmail)
                    .orElseThrow();

            if (!"ADMIN".equals(existingUser.getRole())) {
                throw new IllegalStateException(
                        "The configured admin email belongs to a non-admin account.");
            }

            System.out.println("Admin account already exists.");
            return;
        }

        if (adminPassword.length() < 12 || adminPassword.length() > 72) {
            throw new IllegalStateException(
                    "Admin password must be between 12 and 72 characters.");
        }

        User admin = new User();
        admin.setName("CleanGoa Administrator");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole("ADMIN");

        userRepository.save(admin);

        System.out.println("Admin account created successfully.");
    };
}

}
