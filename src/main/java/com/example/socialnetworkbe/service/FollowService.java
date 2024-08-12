package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Follow;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface FollowService{
    Follow save(Follow follow , BindingResult bindingResult);
    Follow update(Follow follow,Long id , BindingResult bindingResult);
    Follow delete(Long id);
    Optional<Follow> findById(Long id);
    List<Follow> findAll();
}
