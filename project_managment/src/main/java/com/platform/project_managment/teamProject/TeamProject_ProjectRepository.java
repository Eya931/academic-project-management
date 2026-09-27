package com.platform.project_managment.teamProject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamProject_ProjectRepository extends JpaRepository<TeamProject_Project, Integer> {

    List<TeamProject_Project> findAllByProjectId(Integer    projectId);

}
