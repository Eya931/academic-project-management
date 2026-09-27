package com.platform.project_managment.comment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    public Comment addComment(Comment comment) {

        return commentRepository.save(comment);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Comment getUserComment(Integer userId) {
        return commentRepository.findCommentsByUserId(userId).stream().findFirst().orElse(null);
    }

    public List<Comment> getProjectComments(Integer projectId) {
        return commentRepository.findCommentsByProjectId(projectId);
    }


}

