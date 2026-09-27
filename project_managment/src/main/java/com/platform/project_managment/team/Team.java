package com.platform.project_managment.team;

import com.platform.project_managment.group.Group;
import com.platform.project_managment.teamProject.TeamProject;
import com.platform.project_managment.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
public class Team {
    @Id
    @GeneratedValue
    private Integer id;



    @ManyToMany
    @JoinTable(
            name = "team_user",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members;

    @ManyToOne
    private Group group;

    @OneToMany(mappedBy = "team")
    private List<TeamProject> teamProjects;
}

