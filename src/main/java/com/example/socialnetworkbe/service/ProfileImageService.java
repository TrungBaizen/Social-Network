package com.example.socialnetworkbe.service;


import com.example.socialnetworkbe.model.ProfileImage;
import org.springframework.validation.BindingResult;

public interface ProfileImageService {
    ProfileImage save(ProfileImage profileImage , BindingResult bindingResult);
}
