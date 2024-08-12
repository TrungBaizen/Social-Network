package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;


public interface ProfileService{
    Profile save(Profile profile , BindingResult bindingResult);
    Profile update(Profile profile, Long id , BindingResult bindingResult, UserDetails userDetails);
    Optional<Profile> findById(Long id);
    List<Profile> findAll();
    List<Profile> searchProfile(String name);
}
