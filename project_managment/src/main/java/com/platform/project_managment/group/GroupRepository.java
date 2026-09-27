package com.platform.project_managment.group;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    Group getByName(String name);

    Optional<Group> findByName(String assignedTo);
}
