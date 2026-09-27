package com.platform.project_managment.pendingProfessor;



import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingProfessorRepository extends JpaRepository<PendingProfessor, Long> {
    Optional<PendingProfessor> findByInstitutionalEmail(String email);
    boolean existsByInstitutionalEmail(String email);
}

