package com.platform.project_managment.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/save")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.createProject(projectDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Extract the token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Get the filtered list of projects based on the user's groups
        List<ProjectDTO> projects = null;
        try {
            projects = projectService.getAllProjects(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Integer id, @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDTO));
    }



   /* @PostMapping("/assign")
    public ResponseEntity<String> assignProject(@RequestBody Project project) {
        projectService.assignProject(project);
        return ResponseEntity.ok("Project assigned successfully to the selected groups.");
    }*/



    @PostMapping("create")
    public ResponseEntity<?> createProject(
            @RequestBody @Valid ProjectRequestDto requestDto,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            // Remove "Bearer " prefix if present
            String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;

            ProjectRequestDto savedProject = projectService.saveProject(requestDto, token);
            return ResponseEntity.ok(savedProject);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/university-projects")
    public List< ProjectUniversityDto> getUniversityProjects() {
       return projectService.getUniversityProjects();
    }


}
