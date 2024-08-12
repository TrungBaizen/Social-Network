package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Comment;
import com.example.socialnetworkbe.model.CommentImage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment save(Comment comment,List<CommentImage> commentImage ,BindingResult bindingResult);

    Comment update(Comment comment, Long id, BindingResult bindingResult, UserDetails userDetails);

    List<Comment> findAllByPostId(Long postId);

    List<CommentImage> findImageAllByPostId(Long postId);

    void deleteAllByPostId(Long postId);

    Comment delete(Long id);

    Optional<Comment> findById(Long id);

    List<Comment> findAll();

    List<CommentImage> findImageAllByPostIdIn(List<Long> postIds);

    List<Comment> findAllByPostIdIn(List<Long> postId);


}
