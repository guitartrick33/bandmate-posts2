package com.careerjumpstart.admin_ms.controllers;

import com.careerjumpstart.admin_ms.Client;
import com.careerjumpstart.admin_ms.models.Comment;
import com.careerjumpstart.admin_ms.models.Post;
import com.careerjumpstart.admin_ms.payload.response.ResponseWithMessage;
import com.careerjumpstart.admin_ms.repository.CommentsRepo;
import com.careerjumpstart.admin_ms.service.CommentService;
import com.careerjumpstart.admin_ms.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor

@RequestMapping("/comments")
public class CommentController {


    @Autowired
    private CommentService commentService;

    @Autowired
    private Client client;


    @GetMapping(path = "testcookie")
    @ResponseStatus(HttpStatus.OK)
    public String getAllWithAdminAcess(@CookieValue(name="bezkoder") String cookie){
        Object response = client.sendMessageAndReceiveResponse(cookie, "roytuts");
        return response.toString() + " testcookie";
    }

    @GetMapping(path = "testyola")
    @ResponseStatus(HttpStatus.OK)
    public String getAllWithAdminAcess2(@CookieValue(name="bezkoder") String cookie){
        Object response = client.sendMessageAndReceiveResponse(cookie, "roytuts");
        return response.toString() + " testyola";
    }

    @GetMapping
    public ResponseEntity<ResponseWithMessage<List<Comment>>> getAll(){
        List<Comment> comments;
        try {
            comments = commentService.findAll();
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(comments.isEmpty()) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "No posts found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ResponseWithMessage<>(comments, null), HttpStatus.OK);
        }
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ResponseWithMessage<Optional<Comment>>> getById(@PathVariable Long id){
        Optional<Comment> result;
        try {
            result = commentService.findById(id);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
            return new ResponseEntity<>(new ResponseWithMessage<>(result, null), HttpStatus.OK);
    }

    @GetMapping(params = "username")
    public ResponseEntity<ResponseWithMessage<List<Comment>>> getByUsername(@RequestParam String username){
        // TODO: Change this request to take the username from the cookie (case: corresponding user) or user has role admin
        List<Comment> results;
        try {
            results = commentService.findCommentsByUsername(username);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Posts repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseWithMessage<>(results, null), HttpStatus.OK);
    }

    @GetMapping(path = "/post/{id}")
    public ResponseEntity<ResponseWithMessage<List<Comment>>> getByPostId(@PathVariable Long id){
        List<Comment> result;
        try {
            result = commentService.findCommentsByPost_Id(id);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseWithMessage<>(result, null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWithMessage<Comment>> postComment(@CookieValue(name="bezkoder") String cookie, @RequestBody Comment comment){
        try {
            String username = (String) client.sendMessageAndReceiveResponse(cookie, "roytuts");
            if(username == null) {
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "You are unauthorized for this action"), HttpStatus.UNAUTHORIZED);
            }
            comment.setUsername(username);
            Comment newComment = commentService.createComment(comment);
            return new ResponseEntity<>(new ResponseWithMessage<>(newComment, "Post successfully created"), HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Posts repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path="{id}")
    public ResponseEntity<ResponseWithMessage<Comment>> editComment(@RequestBody Comment comment, @PathVariable Long id){
        // TODO: Authorize the user who created the answer is the same as this one requesting this action & exists in the database
        try {
            if(commentService.exists(id)) {
                Comment updatedComment = commentService.updateComment(id, comment);
                return new ResponseEntity<>(new ResponseWithMessage<>(updatedComment, "Post successfully updated"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post not found"), HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path="{id}")
    public ResponseEntity<ResponseWithMessage<Comment>> deleteComment(@PathVariable Long id){
        // TODO: Authorize the user who created the answer is the same as this one requesting this action & exists in the database
        try {
            if(commentService.exists(id)) {
                commentService.deleteComment(id);
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post successfully deleted"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post not found"), HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Posts repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
