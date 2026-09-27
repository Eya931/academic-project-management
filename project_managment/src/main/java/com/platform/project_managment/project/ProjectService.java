package com.platform.project_managment.project;

import com.platform.project_managment.group.Group;
import com.platform.project_managment.group.GroupRepository;
import com.platform.project_managment.security.JwtService;
import com.platform.project_managment.user.User;
import com.platform.project_managment.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {


    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final GroupRepository groupRepository;
    private final JwtService jwtService;

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        List<Integer> supervisorIds = projectDTO.getSupervisors()
                .stream()
                .map(s -> s.getId())
                .collect(Collectors.toList());

        List<User> users = userRepository.findAllById(supervisorIds);

        Project project = projectMapper.toEntity(projectDTO, users);
        Project saved = projectRepository.save(project);

        return projectMapper.toDTO(saved);
    }

        public List<ProjectDTO> getAllProjects(String token) throws Exception {
            // Step 1: Extract the username from the JWT token
            String username = jwtService.extractUsername(token);
            if (username == null) {
                throw new Exception("Invalid or expired token");
            }

            // Log the username for debugging
            System.out.println("Extracted username from token: " + username);

            // Step 2: Find the user by username (email in your case)
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Log user details
            System.out.println("User found: " + user);

            // Step 3: Get the groups this user belongs to
            List<Group> userGroups = user.getGroups();
            System.out.println("User belongs to groups: " + userGroups);

            // Step 4: Check if the token is expired
            if (jwtService.isTokenExpired(token)) {
                throw new Exception("Token is expired");
            }

            // Step 5: Extract group IDs
            List<Integer> groupIds = userGroups.stream()
                    .map(Group::getId)
                    .collect(Collectors.toList());

            List<Project> projects = projectRepository.findAll();

            System.out.println("Projects found: " + projects.get(0).getGroups());
            System.out.println("Group belongs to groups: " + groupIds);
            // Step 6: Use the repository method to get projects associated with the group IDs
            List<Project> projectsForUser = projectRepository.findProjectsByGroupIds(groupIds);
            System.out.println("Projects found: " + projectsForUser);

            // Step 7: Map to DTOs and return the filtered list
            return projectsForUser.stream()
                    .map(projectMapper::toDTO)
                    .collect(Collectors.toList());
        }



        public ProjectDTO getProjectById(Integer id) {
            return projectRepository.findById(id)
                    .map(projectMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        }



    @Transactional
    public ProjectDTO updateProject(Integer id, ProjectDTO updatedDTO) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        existing.setName(updatedDTO.getTitle());
        existing.setDescription(updatedDTO.getDescription());
        existing.setStartDate(updatedDTO.getDueDate());
        existing.setEndDate(updatedDTO.getDueDate()); // Tu peux adapter s’il y a un champ `endDate` spécifique

        List<Integer> supervisorIds = updatedDTO.getSupervisors()
                .stream()
                .map(s -> s.getId())
                .collect(Collectors.toList());

        List<User> supervisors = userRepository.findAllById(supervisorIds);
        existing.setSupervisors(supervisors);

        Project saved = projectRepository.save(existing);
        return projectMapper.toDTO(saved);
    }


    /*@Transactional
    public Project assignProject(Project project) {
        // Extraire les IDs des groupes (assure-toi qu’ils sont initialisés avec juste l’ID dans le JSON)
        List<Integer> groupIds = project.getGroups()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());

        // Charger les entités Group complètes depuis la BDD
        List<Group> groups = groupRepository.findAllById(groupIds);



        project.setGroups(groups);


        return projectRepository.save(project);
    }*/



    @Transactional
    public ProjectRequestDto saveProject(ProjectRequestDto requestDto, String token) throws Exception {
        String username = jwtService.extractUsername(token);
        if (username == null) {
            throw new Exception("Invalid or expired token");
        }
        Optional<User> supervisor= userRepository.findByEmail(username);

        Project project = projectMapper.requestToEntity(requestDto);
        project.setSupervisors(List.of(supervisor.get()));

        Project savedProject =projectRepository.save(project);

        return  projectMapper.toRequestDto(savedProject);

    }

    public List< ProjectUniversityDto> getUniversityProjects() {
        List<Project> projects = projectRepository.findAll(); // À remplacer si tu veux filtrer
        return projects.stream()
                .map(projectMapper::toUniversityDto)
                .collect(Collectors.toList());
    }



}
