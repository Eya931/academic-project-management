package com.platform.project_managment.project;

import com.platform.project_managment.group.Group;
import com.platform.project_managment.group.GroupRepository;
import com.platform.project_managment.task.TaskMapper;
import com.platform.project_managment.user.User;
import com.platform.project_managment.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final UserMapper userMapper;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final GroupRepository groupRepository;
    private final TaskMapper taskMapper;


    public ProjectDTO toDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getName()); // Match ProjectDTO title
        dto.setDescription(project.getDescription());
        dto.setDueDate(project.getStartDate()); // Match ProjectDTO dueDate
        dto.setCategory(project.getCategory());
        dto.setTasks(project.getTasks().stream().map(task -> taskMapper.convertToDTO(task)).collect(Collectors.toList()));

        if (project.getSupervisors() != null) {
            dto.setSupervisors(
                    project.getSupervisors()
                            .stream()
                            .map(userMapper::ToSupervisor)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public Project toEntity(ProjectDTO dto, List<User> supervisors) {
        return Project.builder()
                .id(dto.getId())
                .name(dto.getTitle()) // Match Project's name
                .description(dto.getDescription())
                .startDate(dto.getDueDate()) // Match Project's startDate
                .category(dto.getCategory())
                .supervisors(supervisors)
                .build();
    }






    // Pour ProjectRequestDto (version simplifiée, souvent utilisée pour formulaire création)
    public ProjectRequestDto toRequestDto(Project project) {
        ProjectRequestDto dto = new ProjectRequestDto();
        dto.setId(project.getId());
        dto.setTitle(project.getName());
        dto.setCategory(project.getCategory());
        dto.setDescription(project.getDescription());
        dto.setDueDate(project.getEndDate() != null ? project.getEndDate().format(formatter) : null);
        dto.setMaxStudents(project.getMaxStudents());
        if (project.getGroups() != null && !project.getGroups().isEmpty()) {
            dto.setAssignedTo(project.getGroups().get(0).getName());
        }
        if (dto.getAssignedTo() != null) {
            Optional<Group> optionalGroup = groupRepository.findByName(dto.getAssignedTo());
            optionalGroup.ifPresent(group ->
                    project.setGroups(Collections.singletonList(group))
            );
        }
        return dto;
    }

    public Project requestToEntity(ProjectRequestDto dto) {


        Project project = new Project();
        project.setId(dto.getId());
        project.setName(dto.getTitle());
        project.setCategory(dto.getCategory());
        project.setDescription(dto.getDescription());


        if (dto.getDueDate() != null && !dto.getDueDate().isEmpty()) {
            project.setEndDate(LocalDate.parse(dto.getDueDate(), formatter));
        }

        if (dto.getAssignedTo() != null) {
            Optional<Group> optionalGroup = groupRepository.findByName(dto.getAssignedTo());
            optionalGroup.ifPresent(group ->
                    project.setGroups(Collections.singletonList(group))
            );
        }

        project.setMaxStudents(dto.getMaxStudents());




        return project;
    }


    public ProjectUniversityDto toUniversityDto(Project project) {
        ProjectUniversityDto dto = new ProjectUniversityDto();
        dto.setId(project.getId());
        dto.setTitle(project.getName());
        dto.setCategory(project.getCategory());
        dto.setDescription(project.getDescription());
        dto.setDueDate(project.getEndDate() != null ? LocalDate.parse(project.getEndDate().format(formatter)) : null);
        dto.setSupervisors(project.getSupervisors().stream().map(supervisor -> supervisor.getName()).collect(Collectors.toList()));
    return dto;

    }
}
