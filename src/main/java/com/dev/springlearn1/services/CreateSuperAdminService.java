package com.dev.springlearn1.services;

import com.dev.springlearn1.models.User;
import com.dev.springlearn1.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CreateSuperAdminService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CreateSuperAdminService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${superadmin.username:superadmin}")
    private String superAdminUsername;

    @Value("${superadmin.email:superadmin@example.com}")
    private String superAdminEmail;

    @Value("${superadmin.initial-password}")
    private String superAdminInitialPassword;

    @Override
    public void run(String... args) {
        createSuperAdminIfNotExists();
    }

    private void createSuperAdminIfNotExists() {
        if (userRepository.findByRole(User.Role.SUPER_ADMIN).isEmpty()) {
            User superAdmin = new User();
            superAdmin.setUsername(superAdminUsername);
            superAdmin.setEmail(superAdminEmail);
            superAdmin.setPassword(passwordEncoder.encode(superAdminInitialPassword));
            superAdmin.setRole(User.Role.SUPER_ADMIN);
            superAdmin.setCreatedAt(LocalDateTime.now());
            superAdmin.setUpdatedAt(LocalDateTime.now());

            try {
                userRepository.save(superAdmin);
                logger.info("Super Admin user created successfully.");
            } catch (Exception e) {
                logger.error("Failed to create Super Admin user", e);
            }
        } else {
            logger.info("Super Admin user already exists. Skipping creation.");
        }
    }
}
