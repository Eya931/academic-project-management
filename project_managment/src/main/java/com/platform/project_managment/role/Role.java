package com.platform.project_managment.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platform.project_managment.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;
    private String description;
     @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    // Getters and Setters
}

