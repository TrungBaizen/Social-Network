package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.ProfileImage;
import com.example.socialnetworkbe.repository.ProfileImageRepository;
import com.example.socialnetworkbe.service.ProfileImageService;
import com.example.socialnetworkbe.validate.ExceptionHandlerControllerAdvice;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileImageServiceImpl implements ProfileImageService {
    private final ProfileImageRepository profileImageRepository;

    @Autowired
    public ProfileImageServiceImpl(ProfileImageRepository profileImageRepository) {
        this.profileImageRepository = profileImageRepository;
    }

    @Override
    public ProfileImage save(ProfileImage profileImage, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        return profileImageRepository.save(profileImage);
    }
}
