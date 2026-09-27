package com.platform.project_managment.pendingProfessor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PendingProfessorService {

    private final PendingProfessorRepository repository;

    public void addProfessor(String email) {
        if (!repository.existsByInstitutionalEmail(email)) {
            repository.save(PendingProfessor.builder()
                    .institutionalEmail(email)
                    .build());
        }
    }

    public boolean isPendingProfessor(String email) {
        return repository.existsByInstitutionalEmail(email);
    }

    public List<PendingProfessor> getAllPending() {
        return repository.findAll();
    }

    public void remove(String email) {
        repository.findByInstitutionalEmail(email)
                .ifPresent(repository::delete);
    }

    public void addProfessors(List<String> emails) {
        for (String email : emails) {
            addProfessor(email);  // Réutilisation de la méthode addProfessor pour chaque email
        }
    }

    public List<PendingProfessor> addListOfPendingProfessor(List<String> emails) {
        // Filter out emails that already exist
        List<String> newEmails = emails.stream()
                .filter(email -> !repository.existsByInstitutionalEmail(email))
                .collect(Collectors.toList());

        // Create and save new PendingProfessor entities
        List<PendingProfessor> savedProfessors = newEmails.stream()
                .map(email -> PendingProfessor.builder().institutionalEmail(email).build())
                .map(repository::save)
                .collect(Collectors.toList());

        return savedProfessors;
    }
}