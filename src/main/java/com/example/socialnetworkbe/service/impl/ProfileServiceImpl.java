package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.Profile;
import com.example.socialnetworkbe.repository.ProfileRepository;
import com.example.socialnetworkbe.service.ProfileService;
import com.example.socialnetworkbe.validate.ExceptionHandlerControllerAdvice;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile save(Profile profile, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        return profileRepository.save(profile);
    }

    @Override
    public Profile update( Profile profile,Long id, BindingResult bindingResult, UserDetails userDetails) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        Profile existingProfile = findById(id).get();

        if (!existingProfile.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new AccessDeniedException("Bạn không phải là chủ sở hữu của profile này");
        }
        profile.setId(id);
        return profileRepository.save(profile);
    }


    @Override
    public Optional<Profile> findById(Long id) {
        Optional<Profile> profileOptional = profileRepository.findById(id);
        if (profileOptional.isPresent()) {
            return profileRepository.findById(id);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public List<Profile> findAll() {
        if (profileRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return profileRepository.findAll();
    }

    @Override
    public List<Profile> searchProfile(String name) {
        if (profileRepository.findAllByFirstNameContainingOrLastNameContaining(name,name).isEmpty()) {
            throw new IllegalArgumentException();
        }
        return profileRepository.findAllByFirstNameContainingOrLastNameContaining(name,name);
    }

    @Override
    public Profile findByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }


}
