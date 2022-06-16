package com.careerjumpstart.admin_ms.controllers;

import com.careerjumpstart.admin_ms.Client;
import com.careerjumpstart.admin_ms.models.Post;
import com.careerjumpstart.admin_ms.payload.response.ResponseWithMessage;
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

@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

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
    public ResponseEntity<ResponseWithMessage<List<Post>>> getAll(){
        List<Post> posts;
        try {
            posts = postService.findAll();
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Posts repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
            return new ResponseEntity<>(new ResponseWithMessage<>(posts, null), HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ResponseWithMessage<Optional<Post>>> getById(@PathVariable Long id){
        Optional<Post> result;
        try {
            result = postService.findById(id);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseWithMessage<>(result, null), HttpStatus.OK);
    }

    @GetMapping(params = "username")
    public ResponseEntity<ResponseWithMessage<List<Post>>> getByUsername(@RequestParam String username){
        // TODO: Change this request to take the username from the cookie (case: corresponding user) or user has role admin
        List<Post> results;
        try {
            results = postService.findByUsername(username);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseWithMessage<>(results, null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWithMessage<Post>> postPost(@CookieValue(name="bezkoder") String cookie, @RequestBody Post post){
        try {
            String username = (String) client.sendMessageAndReceiveResponse(cookie, "roytuts");
            if(username == null) {
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "You are unauthorized for this action"), HttpStatus.UNAUTHORIZED);
            }
            post.setUsername(username);
            Post newPost = postService.createPost(post);
            return new ResponseEntity<>(new ResponseWithMessage<>(newPost, "Post successfully created"), HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Posts repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path="{id}")
    public ResponseEntity<ResponseWithMessage<Post>> editPost(@RequestBody Post post, @PathVariable Long id){
        // TODO: Authorize the user who created the answer is the same as this one requesting this action & exists in the database
        try {
            if(postService.exists(id)) {
                Post updatedPost = postService.updatePost(id, post);
                return new ResponseEntity<>(new ResponseWithMessage<>(updatedPost, "Post successfully updated"), HttpStatus.OK);
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
    public ResponseEntity<ResponseWithMessage<Post>> deletePost(@PathVariable Long id){
        // TODO: Authorize the user who created the answer is the same as this one requesting this action & exists in the database
        try {
            if(postService.exists(id)) {
                postService.deletePost(id);
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post successfully deleted"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post not found"), HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Post repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
