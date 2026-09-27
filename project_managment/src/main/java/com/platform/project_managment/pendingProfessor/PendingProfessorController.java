package com.platform.project_managment.pendingProfessor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/pending-professors")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class PendingProfessorController {

    private final PendingProfessorService service;

    @PostMapping
    public ResponseEntity<?> addProfessor(@RequestBody PendingProfessorRequest request) {
        service.addProfessor(request.institutionalEmail());
        return ResponseEntity.ok("Email ajouté avec succès");
    }

    @GetMapping
    public ResponseEntity<List<PendingProfessor>> getAll() {
        return ResponseEntity.ok(service.getAllPending());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProfessor(@RequestParam String email) {
        service.remove(email);
        return ResponseEntity.ok("Email supprimé avec succès");
    }

    @PostMapping("/bulk-add")  // Nouvelle route pour l'ajout de plusieurs emails
    public ResponseEntity<?> addProfessors(@RequestBody List<String> emails) {
        service.addProfessors(emails); // Appel de la méthode dans le service
        return ResponseEntity.ok("Emails ajoutés avec succès");
    }

    @PostMapping("/add-list")
    public ResponseEntity<List<PendingProfessor>> addListOfPendingProfessor(@RequestBody List<String> emails) {
        List<PendingProfessor> addedProfessors = service.addListOfPendingProfessor(emails);
        return ResponseEntity.ok(addedProfessors);
    }

    public record PendingProfessorRequest(String institutionalEmail) {
    }
}