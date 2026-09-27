package com.platform.project_managment.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
    List<Comment> findCommentsByUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM Comment c WHERE c.project.id = :projectId")
    List<Comment> findCommentsByProjectId(@Param("projectId") Integer projectId);


}
