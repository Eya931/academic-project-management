package com.platform.project_managment.project;

import com.platform.project_managment.comment.Comment;
import com.platform.project_managment.documentation.Documentation;
import com.platform.project_managment.group.Group;
import com.platform.project_managment.task.Task;
import com.platform.project_managment.user.User;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Project {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Float grade;
    private Integer maxStudents;

    @OneToMany(
        mappedBy = "project",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(
            name = "project_supervisors",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> supervisors;

    private String category;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Documentation> documentations;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @ManyToMany
    private List<Group> groups;
    // add task
}