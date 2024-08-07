package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Like;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface LikeService{
    Like save(Like like , BindingResult bindingResult);
    Like update(Like like,Long id , BindingResult bindingResult);
    Like delete(Long id);
    Optional<Like> findById(Long id);
    List<Like> findAll();
}
