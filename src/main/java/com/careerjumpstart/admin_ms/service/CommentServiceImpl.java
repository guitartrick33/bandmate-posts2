package com.careerjumpstart.admin_ms.service;

import com.careerjumpstart.admin_ms.models.Comment;
import com.careerjumpstart.admin_ms.models.Post;
import com.careerjumpstart.admin_ms.repository.CommentsRepo;
import com.careerjumpstart.admin_ms.repository.PostsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private final CommentsRepo commentsRepo;

    public CommentServiceImpl(CommentsRepo commentsRepo) {
        this.commentsRepo = commentsRepo;
    }

    @Override
    public List<Comment> findAll() {
        return commentsRepo.findAll();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentsRepo.findById(id);
    }


    @Override
    public List<Comment> findCommentsByUsername(String username) {
        return commentsRepo.findCommentsByUsername(username);
    }

    @Override
    public List<Comment> findCommentsByPost_Id(Long id){
        return commentsRepo.findCommentsByPost_Id(id);
    }

    @Override
    public Comment createComment(Comment c) {
        return commentsRepo.save(c);
    }

    @Override
    public Comment updateComment(Long id, Comment c) {
        Optional<Comment> comment = findById(id);
        if(comment.isPresent()){
            comment.get().setContent(c.getContent());
            return commentsRepo.save(comment.get());
        }
        else
        {
            return null;
        }
    }

    @Override
    public void deleteComment(Long id) {
        commentsRepo.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return commentsRepo.existsById(id);
    }
}
