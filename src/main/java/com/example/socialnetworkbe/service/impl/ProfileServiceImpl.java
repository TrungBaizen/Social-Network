package com.example.socialnetworkbe.service.impl;


import com.example.socialnetworkbe.model.DTO.FriendDTO;
import com.example.socialnetworkbe.model.DTO.FriendshipStatusDTO;
import com.example.socialnetworkbe.model.DTO.ProfileDTO;
import com.example.socialnetworkbe.model.Friend;
import com.example.socialnetworkbe.model.Profile;
import com.example.socialnetworkbe.repository.ProfileRepository;
import com.example.socialnetworkbe.service.FriendService;
import com.example.socialnetworkbe.service.ProfileService;
import com.example.socialnetworkbe.validate.ExceptionHandlerControllerAdvice;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;



    private final FriendService friendService;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository, FriendService friendService) {
        this.profileRepository = profileRepository;
        this.friendService = friendService;
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
    public ProfileDTO update( Profile profile,Long id, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        Profile profileToUpdate = findById(id).get();
        profileToUpdate.setGender(profile.getGender());
        profileToUpdate.setFirstName(profile.getFirstName());
        profileToUpdate.setLastName(profile.getLastName());
        profileToUpdate.setBirthDate(profile.getBirthDate());
        profileToUpdate.setCurrentLocation(profile.getCurrentLocation());
        profileToUpdate.setDescription(profile.getDescription());
        profileToUpdate.setHometown(profile.getHometown());
        profileToUpdate.setOccupation(profile.getOccupation());
        profileRepository.save(profileToUpdate);
        List<Friend> friendList = friendService.getListFriend(profileToUpdate.getUser().getEmail());
        if (friendList.isEmpty()) {
            friendList = Collections.emptyList();
        }
        List<FriendDTO> friendDTOList = new ArrayList<>();
        for (Friend friend : friendList) {
            Profile friendProfile = profileRepository.findByEmail(friend.getUser().getEmail());
            FriendDTO friendDTO = new FriendDTO(friend,friendProfile);
            friendDTOList.add(friendDTO);
        }
        ProfileDTO profileDTO = new ProfileDTO(profileToUpdate,friendDTOList);
        return profileDTO;
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
    public List<Profile> searchProfile(String name) {
        if (profileRepository.findAllByFirstNameContainingOrLastNameContaining(name,name).isEmpty()) {
            return new ArrayList<>();
        }
        return profileRepository.findAllByFirstNameContainingOrLastNameContaining(name,name);
    }

    @Override
    public Profile findByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Override
    public ProfileDTO getProfile(String email) {
       Profile profile = profileRepository.findByEmail(email);
       if (profile == null) {
           throw new IllegalArgumentException("Tài khoản hiện chưa tồn tại.");
       }
       List<Friend> friendList = friendService.getListFriend(profile.getUser().getEmail());
       if (friendList.isEmpty()) {
           friendList = Collections.emptyList();
       }
       List<FriendDTO> friendDTOList = new ArrayList<>();
       for (Friend friend : friendList) {
           Profile friendProfile = profileRepository.findByEmail(friend.getUser().getEmail());
           FriendDTO friendDTO = new FriendDTO(friend,friendProfile);
           friendDTOList.add(friendDTO);
       }
        ProfileDTO profileDTO = new ProfileDTO(profile,friendDTOList);
        return profileDTO;
    }


    @Override
    public Profile updateImageAvatar(Long id ,String image, BindingResult bindingResult ) {
        Profile profile = findById(id).get();
        profile.setImageAvatar(image);
        return save(profile,bindingResult);
    }

    @Override
    public Profile updateImageCover(Long id ,String image, BindingResult bindingResult ) {
        Profile profile = findById(id).get();
        profile.setImageCover(image);
        return save(profile,bindingResult);
    }
}
