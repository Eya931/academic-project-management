package com.platform.project_managment.task;

import com.platform.project_managment.project.Project;
import com.platform.project_managment.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;


    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));
    }

    public TaskDTO createTask(TaskDTO taskDto, Integer projectId) throws ChangeSetPersister.NotFoundException {
        // Find the project by ID
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        Task task = new Task();
        taskMapper.convertToEntity(taskDto);
        // Associate the task with the project
        task.setProject(project);

        // Set default values if not provided
        if (task.getDueDate() == null) {
            task.setDueDate(LocalDate.now().plusDays(7)); // Default due date is 1 week from now
        }

        if (task.getStatus() == null) {
            task.setStatus(LocalDate.now()); // Set status date to current date
        }

        // Save and return the task
        return taskMapper.convertToDTO(taskRepository.save(task));
    }

    public Task updateTask(Integer id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));

        task.setName(taskDetails.getName());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());
        task.setProject(taskDetails.getProject());
        task.setDeliverables(taskDetails.getDeliverables());

        return taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));
        taskRepository.delete(task);
    }


}