package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Post;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface PostService{
    Post save(Post post , BindingResult bindingResult);
    Post update(Post post,Long id , BindingResult bindingResult, UserDetails userDetails);
    Post delete(Long id);
    Optional<Post> findById(Long id);
    List<Post> findAll();
    List<Post> searchPost(String content);
    List<Post> findAllByUserId(Long userId);
}
