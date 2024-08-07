package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.model.Post;
import com.example.socialnetworkbe.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostList(){
        return new ResponseEntity<>(postService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(postService.findAllByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return new ResponseEntity<>(postService.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, BindingResult bindingResult) {
        return new ResponseEntity<>(postService.save(post,bindingResult), HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post, BindingResult bindingResult,@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(postService.update(post, id, bindingResult,userDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id) {
        return new ResponseEntity<>(postService.delete(id),HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPost(@RequestParam String content) {
        return new ResponseEntity<>(postService.searchPost(content), HttpStatus.OK);
    }
}
