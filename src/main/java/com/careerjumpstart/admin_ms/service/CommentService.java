package com.careerjumpstart.admin_ms.service;

import com.careerjumpstart.admin_ms.models.Comment;
import com.careerjumpstart.admin_ms.models.Post;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> findAll();
    Optional<Comment> findById(Long id);
    List<Comment> findCommentsByUsername(String username);
    List<Comment> findCommentsByPost_Id(Long id);
    Comment createComment(Comment c);
    Comment updateComment(Long id, Comment c);
    void deleteComment(Long id);
    boolean exists(Long id);
}
