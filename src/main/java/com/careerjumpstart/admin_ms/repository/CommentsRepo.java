package com.careerjumpstart.admin_ms.repository;

import com.careerjumpstart.admin_ms.models.Comment;
import com.careerjumpstart.admin_ms.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentsRepo extends JpaRepository<Comment, Long> {
    List<Comment> findAll();
    Optional<Comment> findById(Long id);
    List<Comment> findCommentsByUsername(String username);
    List<Comment> findCommentsByPost_Id(Long id);

    @Override
    <S extends Comment> S save(S entity);
}
