package com.platform.project_managment.group;

import com.platform.project_managment.project.Project;
import com.platform.project_managment.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
@Table(
        name = "groups",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "universityYear"})
        }
)
public class Group {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false ,unique = true)
    private String name;

    private String sector;


    private String universityYear;

    @ManyToMany(mappedBy = "groups")
    private List<User> students;
    private Integer Numberstudents;

    @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER)
    private List<Project> projects;

    @ElementCollection
    @CollectionTable(name = "group_pending_emails", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "email")
    private List<String> pendingStudentEmails;

    public void addStudent(User user) {
        this.students.add(user);
    }
}
