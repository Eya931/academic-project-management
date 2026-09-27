
package com.platform.project_managment.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Integer id;
    private String name;
    private String description;
    private String deliverables; // Single string for front-end
    private LocalDate dueDate;
    private String status; // String representation of status for front-end
    private boolean isCompleted;
    private Integer projectId;

}