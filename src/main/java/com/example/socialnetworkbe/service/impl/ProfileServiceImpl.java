package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.Profile;
import com.example.socialnetworkbe.repository.ProfileRepository;
import com.example.socialnetworkbe.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;
@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile save(Profile profile, BindingResult bindingResult) {
        return profileRepository.save(profile);
    }

    @Override
    public Profile update(Profile profile, Long id, BindingResult bindingResult) {
        findById(id);
        profile.setId(id);
        return profileRepository.save(profile);
    }

    @Override
    public Profile delete(Long id) {
        Optional<Profile> profileOptional = findById(id);
        profileRepository.deleteById(id);
        return profileOptional.get();
    }

    @Override
    public Optional<Profile> findById(Long id) {
        Optional<Profile> profileOptional = profileRepository.findById(id);
        if (profileOptional.isPresent()) {
            return profileRepository.findById(id);
        }
        throw new IllegalArgumentException();
    }
}
