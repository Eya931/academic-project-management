package com.platform.project_managment.pendingProfessor;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pending_professors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingProfessor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String institutionalEmail;
}
