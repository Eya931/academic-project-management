package com.platform.project_managment.teamProject;

import lombok.Data;

import java.util.List;

@Data
public class CreateTeamProjectRequest {
    private Integer projectId;
    private String teamProjectName;
    private List<String> members; // List of member emails
    private Integer teamId;
}
