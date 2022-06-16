package com.careerjumpstart.admin_ms.repository;

import com.careerjumpstart.admin_ms.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostsRepo extends JpaRepository<Post, Long> {
    List<Post> findAll();
    Optional<Post> findPostById(Long id);
    List<Post> findPostsByUsername(String username);

    @Override
    <S extends Post> S save(S entity);
}
