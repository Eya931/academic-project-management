package com.platform.project_managment.teamProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team-projects")
public class TeamProjectController {

    private final TeamProjectService teamProjectService;

    @Autowired
    public TeamProjectController(TeamProjectService teamProjectService) {
        this.teamProjectService = teamProjectService;
    }

    @PostMapping
    public ResponseEntity<TeamProjectResponseDTO> createTeamProject(@RequestBody CreateTeamProjectRequest request, @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");
        TeamProjectResponseDTO  response = teamProjectService.createTeamProject(request,token).getBody();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TeamProjectResponseDTO>> getAllTeamProjects( @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");
        List<TeamProjectResponseDTO> teamProjects = teamProjectService.getAllTeamProjects(token);
        return ResponseEntity.ok(teamProjects);
    }

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<TeamProjectResponseDTO>> getTeamProjectsByProjectId(@PathVariable Integer projectId) {
        String userEmail = getCurrentUserEmail();
        List<TeamProjectResponseDTO> teamProjects = teamProjectService.getTeamProjectsByProjectId(projectId);
        return ResponseEntity.ok(teamProjects);
    }

    // Helper method to get the current authenticated user's email
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // In Spring Security, getName() returns the username, which is email in this case
    }
}

// 6. Let's also update the Angular service to call these new endpoints
// This would go in the An