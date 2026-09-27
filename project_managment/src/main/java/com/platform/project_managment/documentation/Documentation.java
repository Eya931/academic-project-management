package com.platform.project_managment.documentation;

import com.platform.project_managment.project.Project;
import com.platform.project_managment.user.User;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
@Entity
public class Documentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters
}

