package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Comment;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface CommentService{
    Comment save(Comment comment , BindingResult bindingResult);
    Comment update(Comment comment,Long id , BindingResult bindingResult);
    Comment delete(Long id);
    Optional<Comment> findById(Long id);
    List<Comment> findAll();
}
