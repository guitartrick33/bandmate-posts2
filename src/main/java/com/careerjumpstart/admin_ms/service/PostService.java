package com.careerjumpstart.admin_ms.service;

import com.careerjumpstart.admin_ms.models.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<Post> findAll();
    Optional<Post> findById(Long id);
    List<Post> findByUsername(String username);
    Post createPost(Post p);
    Post updatePost(Long id, Post p);
    void deletePost(Long id);
    boolean exists(Long id);
}
