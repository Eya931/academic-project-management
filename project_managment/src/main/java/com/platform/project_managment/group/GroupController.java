package com.platform.project_managment.group;

import com.platform.project_managment.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/create")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto groupDto) {
        return ResponseEntity.ok(groupService.createGroup(groupDto));
    }

    @PutMapping("/{groupId}/pending-students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupDto> addPendingStudentEmail(
            @PathVariable Integer groupId,
            @RequestParam String email) {

        GroupDto updatedGroup = groupService.addPendingStudentEmail(groupId, email);
        return ResponseEntity.ok(updatedGroup);
    }

    @PutMapping("/{groupId}/pending-students/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupDto> addPendingStudentEmails(
            @PathVariable Integer groupId,
            @RequestBody List<String> emails) {

        GroupDto updatedGroup = groupService.addPendingStudentEmails(groupId, emails);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{groupId}/pending-students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupDto> deletePendingStudentEmail(
            @PathVariable Integer groupId,
            @RequestParam String email) {

        GroupDto updatedGroup = groupService.deletePendingStudentEmail(groupId, email);
        return ResponseEntity.ok(updatedGroup);
    }

    // Nouvelle route pour obtenir tous les groupes
    @GetMapping("/all")
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<GroupDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    // Nouvelle route pour obtenir les étudiants d'un groupe par ID
    @GetMapping("/{groupId}/students")
    public ResponseEntity<List<User>> getGroupStudents(@PathVariable Integer groupId) {
        List<User> students = groupService.getGroupStudents(groupId);
        return ResponseEntity.ok(students);
    }


}
