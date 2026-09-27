package com.platform.project_managment.group;

import com.platform.project_managment.project.ProjectRepository;
import com.platform.project_managment.user.User;
import com.platform.project_managment.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository, ProjectRepository projectRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.groupMapper = groupMapper;
    }

    public GroupDto createGroup(GroupDto groupDto) {
        Group group = groupMapper.toGroup(groupDto);

        return groupMapper.toGroupDto(groupRepository.save(group));
    }

    public GroupDto addPendingStudentEmail(Integer groupId, String email) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + groupId));

        group.getPendingStudentEmails().add(email);
        group = groupRepository.save(group);

        return groupMapper.toGroupDto(group);
    }

    public GroupDto deletePendingStudentEmail(Integer groupId, String email) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + groupId));

        group.getPendingStudentEmails().remove(email);
        group = groupRepository.save(group);

        return groupMapper.toGroupDto(group);
    }

    public GroupDto addPendingStudentEmails(Integer groupId, List<String> emails) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + groupId));

        group.getPendingStudentEmails().addAll(emails);
        group = groupRepository.save(group);

        return groupMapper.toGroupDto(group);
    }


    public List<GroupDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        System.out.println(groups.get(0).getName());
        return groupMapper.toGroupDtoList(groups); // mapper pour convertir en Dto
    }

    // Nouvelle méthode pour récupérer les étudiants d'un groupe par son ID
    public List<User> getGroupStudents(Integer groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + groupId));

        return group.getStudents();
    }
}
