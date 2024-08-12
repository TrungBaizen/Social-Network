package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Like;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface LikeService{
    Like save(Like like , BindingResult bindingResult);
    void delete(Long id);
    List<Like> findAllByPostId(Long postId);
    void deleteAllByPostId(Long id);
    Optional<Like> findById(Long id);
    List<Like> findAllByPostIdIn(List<Long> postIds);
    boolean checkLike(Long userId, Long postId);
}