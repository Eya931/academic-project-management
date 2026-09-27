package com.platform.project_managment.project;

import com.platform.project_managment.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByNameIn(List<String> projectNames);

    @Query("SELECT p FROM Project p JOIN p.groups g WHERE g.id IN :groupIds")
    List<Project> findProjectsByGroupIds(@Param("groupIds") List<Integer> groupIds);

}
