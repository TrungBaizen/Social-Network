package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.model.DTO.*;
import com.example.socialnetworkbe.model.Post;
import com.example.socialnetworkbe.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping
    public ResponseEntity<PostLikeCommentDTO> createPost(@RequestBody PostDTO postDTO, BindingResult bindingResult) {
        return new ResponseEntity<>(postService.save(postDTO, bindingResult), HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<PostLikeCommentDTO> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO, BindingResult bindingResult) {
        return new ResponseEntity<>(postService.update(postDTO, id, bindingResult), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id) {
        return new ResponseEntity<>(postService.delete(id), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<PostLikeCommentDTO>> getAllPostByFollower(@PathVariable Long id) {
        return new ResponseEntity<>(postService.findAllByFollowing(id), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<PostLikeCommentDTO>> getPostByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(postService.findAllByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostLikeCommentDTO>> searchPost(@RequestParam String content) {
        return new ResponseEntity<>(postService.searchPost(content), HttpStatus.OK);
    }

    @PostMapping("/likes")
    public ResponseEntity<LikeDTO> likePost(@Validated @RequestBody LikeDTO likeDTO, BindingResult bindingResult) {
        return new ResponseEntity<>(postService.likePost(likeDTO, bindingResult),HttpStatus.OK);
    }

    @DeleteMapping("/likes/{likeId}")
    public ResponseEntity<LikeDTO> unlikePost(@PathVariable Long likeId) {
        return new ResponseEntity<>(postService.deleteLikePost(likeId),HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<?> commentPost(@Validated @RequestBody CommentDTO commentDTO, BindingResult bindingResult) {
        postService.commentPost(commentDTO, bindingResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @Validated @RequestBody CommentUpdateDTO commentUpdateDTO, BindingResult bindingResult) {
        postService.updateCommentPost(commentId,commentUpdateDTO, bindingResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        postService.deleteCommentPost(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
