package com.platform.project_managment.deliverable;


import com.platform.project_managment.task.Task;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Deliverable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private LocalDateTime uploadDate;

    @ManyToOne(fetch = FetchType.LAZY) // Préférez LAZY pour les relations ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}