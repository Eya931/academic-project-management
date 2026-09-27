package com.platform.project_managment.task;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Component
public class TaskMapper {




    public Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setCompleted(taskDTO.isCompleted());

        // Convert status string to LocalDate if present
        if (taskDTO.getStatus() != null && !taskDTO.getStatus().isEmpty()) {
            try {
                task.setStatus(LocalDate.parse(taskDTO.getStatus()));
            } catch (Exception e) {
                // If not parseable as date, set to current date
                task.setStatus(LocalDate.now());
            }
        }

        return task;
    }

    public TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setCompleted(task.isCompleted());

        // Set status as formatted string
        if (task.getStatus() != null) {
            dto.setStatus(task.getStatus().toString());
        }

        // Set project ID if available
        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
        }

        // Convert deliverables to string if needed
        dto.setDeliverables(task.getDeliverables());

        return dto;
    }
}


