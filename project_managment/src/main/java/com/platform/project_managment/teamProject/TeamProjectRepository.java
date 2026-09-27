package com.platform.project_managment.teamProject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamProjectRepository extends JpaRepository<TeamProject, Integer> {

    // Note: JpaRepository already provides findById that returns Optional<TeamProject>
    // We don't need to redefine it to return List<TeamProject>

    @Query("SELECT tp FROM TeamProject tp JOIN tp.team t JOIN t.members m WHERE m.email = :userEmail")
    List<TeamProject> findAllByUserEmail(String userEmail);


    List<TeamProject> findAllByIdIn(List<Integer> teamProjectIds);
}