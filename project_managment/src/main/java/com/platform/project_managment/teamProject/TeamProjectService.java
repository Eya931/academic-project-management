package com.platform.project_managment.teamProject;

import com.platform.project_managment.project.Project;
import com.platform.project_managment.project.ProjectRepository;
import com.platform.project_managment.security.JwtService;
import com.platform.project_managment.task.Task;
import com.platform.project_managment.task.TaskMapper;
import com.platform.project_managment.task.TaskRepository;
import com.platform.project_managment.team.Team;
import com.platform.project_managment.team.TeamRepository;
import com.platform.project_managment.user.User;
import com.platform.project_managment.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamProjectService {

    private final TeamProjectRepository teamProjectRepository;
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TeamProject_ProjectRepository teamProject_ProjectRepository;
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;



    public ResponseEntity<TeamProjectResponseDTO> createTeamProject(CreateTeamProjectRequest request, String token) throws Exception {
        String userEmail = jwtService.extractUsername(token);
        if (userEmail == null) {
            throw new Exception("Invalid or expired token");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Team team;
        if (request.getTeamId() != null) {
            team = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

            if (team.getMembers().stream().noneMatch(member -> member.getEmail().equals(userEmail))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a member of the specified team");
            }
        } else {
            List<User> members = userRepository.findAllByEmailIn(request.getMembers());
            if (members.isEmpty() || !request.getMembers().contains(userEmail)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid member list");
            }
            team = new Team();
            team.setMembers(members);
            team = teamRepository.save(team);
        }

        TeamProject teamProject = new TeamProject();
        teamProject.setName(project.getName());
        teamProject.setDescription(project.getDescription());
        teamProject.setCategory(project.getCategory());
        teamProject.setStartDate(project.getStartDate());
        teamProject.setEndDate(project.getEndDate());
        teamProject.setSupervisors(project.getSupervisors());

        List<Task> tasks = taskRepository.getTasksByProjectId(project.getId());
        teamProject.setTasks(tasks);
        System.out.println("********************************************");
        System.out.println("team prijects tasks "+teamProject.getTasks().size());

        if (project.getSupervisors() != null) {
            teamProject.setSupervisors(new ArrayList<>(project.getSupervisors()));
        }

        teamProject.setTeam(team);
        teamProject.setTeamProjectName(request.getTeamProjectName());
        teamProject.setVoteCount(0);
        teamProject.setAverageRating(0.0);

        TeamProject savedTeamProject = teamProjectRepository.save(teamProject);

        TeamProjectResponseDTO responseDTO = convertToResponseDTO(savedTeamProject);

        TeamProject_Project savedTeamProject_Project = new TeamProject_Project();
        savedTeamProject_Project.setProjectId(request.getProjectId());
        savedTeamProject_Project.setTeamProjectid(teamProject.getId());
        teamProject_ProjectRepository.save(savedTeamProject_Project);

        return ResponseEntity.ok(responseDTO);
    }
    public List<TeamProjectResponseDTO> getAllTeamProjects( String token) throws Exception {
        String userEmail = jwtService.extractUsername(token);
        if (userEmail == null) {
            throw new Exception("Invalid or expired token");
        }
        List<TeamProject> teamProjects = teamProjectRepository.findAllByUserEmail(userEmail);
        return teamProjects.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TeamProjectResponseDTO> getTeamProjectsByProjectId(Integer projectId) {
        List<Integer> teamProjectIds = teamProject_ProjectRepository.findAllByProjectId(projectId).stream().map(TPP->TPP.getTeamProjectid()).toList();


        List<TeamProject> teamProjects = teamProjectRepository.findAllByIdIn(teamProjectIds);


        // Convert each TeamProject to TeamProjectResponseDTO
        return teamProjects.stream()
                .map(this::convertToResponseDTO) // Assuming convertToResponseDTO is implemented
                .collect(Collectors.toList());
    }

    private TeamProjectResponseDTO convertToResponseDTO(TeamProject teamProject) {
        TeamProjectResponseDTO dto = new TeamProjectResponseDTO();
        dto.setTeamProjectId(teamProject.getId());
        dto.setTeamProjectName(teamProject.getName());
        dto.setCategory(teamProject.getCategory());
        dto.setDescription(teamProject.getDescription());
        dto.setSupervisors(teamProject.getSupervisors().stream().map(user -> user.getName()).collect(Collectors.toList()));

        System.out.println("*************************************************************");
        System.out.println("the supervisor for this team is : "+dto.getSupervisors().getFirst());

        dto.setDueDate(teamProject.getEndDate() != null ? teamProject.getEndDate().toString() : null);
        dto.setVotingEnabled(false);
        dto.setTasks(teamProject.getTasks().stream().map(task ->taskMapper.convertToDTO(task) ).collect(Collectors.toList()));
        // ou selon ta logique

        if (teamProject.getSupervisors() != null) {
            dto.setSupervisors(teamProject.getSupervisors().stream()
                    .map(User::getName) // ou firstname + lastname
                    .collect(Collectors.toList()));
        }

        if (teamProject.getTeam() != null && teamProject.getTeam().getMembers() != null) {
            dto.setMembers(teamProject.getTeam().getMembers().stream()
                    .map(User::getName) // ou email ou firstname + lastname
                    .collect(Collectors.toList()));
        }

        dto.setTeamProjectName(teamProject.getTeamProjectName());
        dto.setVoteCount(teamProject.getVoteCount());
        dto.setAverageRating(teamProject.getAverageRating());
        if (teamProject.getEndDate() != null) {
            LocalDate today = LocalDate.now();
            LocalDate endDate = teamProject.getEndDate();
            long daysLeft = ChronoUnit.DAYS.between(today, endDate);

            if (daysLeft > 30) {
                long months = daysLeft / 30;
                dto.setStatus(months + (months == 1 ? " month left" : " months left"));
            } else if (daysLeft > 7) {
                long weeks = daysLeft / 7;
                dto.setStatus(weeks + (weeks == 1 ? " week left" : " weeks left"));
            } else if (daysLeft > 0) {
                dto.setStatus(daysLeft + (daysLeft == 1 ? " day left" : " days left"));
            } else if (daysLeft == 0) {
                dto.setStatus("Due today");
            } else {
                long overdueDays = Math.abs(daysLeft);
                if (overdueDays > 30) {
                    long months = overdueDays / 30;
                    dto.setStatus("Overdue by " + months + (months == 1 ? " month" : " months"));
                } else if (overdueDays > 7) {
                    long weeks = overdueDays / 7;
                    dto.setStatus("Overdue by " + weeks + (weeks == 1 ? " week" : " weeks"));
                } else {
                    dto.setStatus("Overdue by " + overdueDays + (overdueDays == 1 ? " day" : " days"));
                }
            }
        }



        return dto;
    }


}