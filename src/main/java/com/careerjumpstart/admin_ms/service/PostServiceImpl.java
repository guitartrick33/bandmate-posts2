package com.careerjumpstart.admin_ms.service;

import com.careerjumpstart.admin_ms.models.Post;
import com.careerjumpstart.admin_ms.repository.PostsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private final PostsRepo postsRepo;

    public PostServiceImpl(PostsRepo postsRepo) {
        this.postsRepo = postsRepo;
    }

    @Override
    public List<Post> findAll() {
        return postsRepo.findAll();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postsRepo.findById(id);
    }


    @Override
    public List<Post> findByUsername(String username) {
        return postsRepo.findPostsByUsername(username);
    }

    @Override
    public Post createPost(Post p) {
        return postsRepo.save(p);
    }

    @Override
    public Post updatePost(Long id, Post p) {
        Optional<Post> post = findById(id);
        if(post.isPresent()){
            post.get().setContent(p.getContent());
            post.get().setTitle(p.getTitle());
            return postsRepo.save(post.get());
        }
        else
        {
            return null;
        }
    }

    @Override
    public void deletePost(Long id) {
        postsRepo.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return postsRepo.existsById(id);
    }
}
