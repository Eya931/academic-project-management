package com.platform.project_managment;

import com.platform.project_managment.role.Role;
import com.platform.project_managment.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync


public class ProjectManagmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagmentApplication.class, args);
    }

    @Bean
    @Order(1)
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
        return args -> {
            // Initialisation des rôles si non existants
            if (roleRepository.findByName("student").isEmpty()) {
                roleRepository.save(Role.builder().name("student").description("Utilisateur étudiant").build());
            }
            if (roleRepository.findByName("professor").isEmpty()) {
                roleRepository.save(Role.builder().name("professor").description("Utilisateur enseignant ").build());
            }
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(Role.builder().name("ADMIN").description("Utilisateur administrateur").build());
            }
        };
    }


}
