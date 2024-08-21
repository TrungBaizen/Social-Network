package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.DTO.ProfileDTO;
import com.example.socialnetworkbe.model.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;


public interface ProfileService{
    Profile save(Profile profile , BindingResult bindingResult);
    ProfileDTO update(Profile profile, Long id , BindingResult bindingResult);
    Optional<Profile> findById(Long id);
    List<Profile> searchProfile(String name);
    Profile findByUserId(Long userId);
    ProfileDTO getProfile(String email);
    Profile updateImageAvatar(Long id ,String image, BindingResult bindingResult);
    Profile updateImageCover(Long id ,String image, BindingResult bindingResult);

}
