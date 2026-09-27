package com.platform.project_managment.admin;

import com.platform.project_managment.role.Role;
import com.platform.project_managment.role.RoleRepository;
import com.platform.project_managment.user.User;
import com.platform.project_managment.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Value("${app.first-admin.email}")
    private String firstAdminEmail;

    @Value("${app.first-admin.password}")
    private String firstAdminPassword;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(firstAdminEmail).isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("ADMIN role not initialized"));

            User admin = User.builder()
                    .email(firstAdminEmail)
                    .password(passwordEncoder.encode(firstAdminPassword))
                    .enabled(true)
                    .accountLocked(false)
                    .roles(List.of(adminRole))
                    .build();

            userRepository.save(admin);
            System.out.println("✅ First admin initialized: " + firstAdminEmail);
        }
    }
}