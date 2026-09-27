package com.platform.project_managment.teamProject;

import com.platform.project_managment.task.TaskDTO;
import lombok.Data;

import java.util.List;

@Data
public class TeamProjectResponseDTO {
    private Integer teamProjectId;
    private String  teamProjectName;
    private List<String> supervisors;
    private List<TaskDTO>tasks;
    private String category;
    private String description;
    private String dueDate;
    private String status;
    private boolean votingEnabled;
    private List<String> members;
    private Integer voteCount;
    private Double averageRating;
}
