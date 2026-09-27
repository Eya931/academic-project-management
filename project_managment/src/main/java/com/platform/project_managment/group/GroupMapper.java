package com.platform.project_managment.group;

import com.platform.project_managment.project.Project;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GroupMapper {

    public Group toGroup(GroupDto groupDto) {
        Group group = new Group();
        group.setName(groupDto.getName());
        group.setPendingStudentEmails(groupDto.getPendingStudentEmails());
        group.setUniversityYear(groupDto.getUniversityYear());
        group.setNumberstudents(groupDto.getPendingStudentEmails().size());
        group.setSector(groupDto.getSector());
        return group;

    }

    public GroupDto toGroupDto(Group group) {
        GroupDto groupDto = new GroupDto();

        groupDto.setId(group.getId());
        groupDto.setName(group.getName());
        groupDto.setPendingStudentEmails(group.getPendingStudentEmails());
        groupDto.setNumberstudents(group.getPendingStudentEmails().size());
        groupDto.setUniversityYear(group.getUniversityYear());
        groupDto.setSector(group.getSector());


        return groupDto;

    }

    public List<GroupDto> toGroupDtoList(List<Group> groups) {
        return groups.stream()
                .map(this::toGroupDto)
                .collect(Collectors.toList());
    }

}
