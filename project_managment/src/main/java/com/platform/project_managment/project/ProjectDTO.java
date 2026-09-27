package com.platform.project_managment.project;

import com.platform.project_managment.task.TaskDTO;
import com.platform.project_managment.user.Supervisor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDTO {
    private Integer id;
    private String title;
    private String description;
    private String category;
    private LocalDate dueDate;
    private List<TaskDTO> tasks;
    private List<Supervisor> supervisors;


}

