package com.platform.project_managment.teamProject;

import com.platform.project_managment.project.Project;
import com.platform.project_managment.team.Team;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TeamProject extends Project {

    @ManyToOne
    private Team team;
    @Column(unique = true)
    private String teamProjectName;
    private Integer voteCount;
    private Double averageRating;



}