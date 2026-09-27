package com.platform.project_managment.task;

import com.platform.project_managment.deliverable.Deliverable;
import com.platform.project_managment.project.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "Title cannot be null")

    private String name;

    @NotNull(message = "Description cannot be null")

    private String description;

    @NotNull(message = "Deadline cannot be null")
    private LocalDate dueDate;

    private LocalDate status;
    private boolean isCompleted;
    private String deliverables;

    @ManyToOne(fetch = FetchType.LAZY)  // FetchType.LAZY is more common for ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Deliverable> submissions;





}
