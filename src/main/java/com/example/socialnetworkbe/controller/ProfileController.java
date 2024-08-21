package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.model.DTO.ProfileDTO;
import com.example.socialnetworkbe.model.Profile;
import com.example.socialnetworkbe.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
@CrossOrigin("*")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileDTO> getProfile(@RequestParam String email) {
        return new ResponseEntity<>(profileService.getProfile(email), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Profile>> searchProfiles(@RequestParam String name) {
        return new ResponseEntity<>(profileService.searchProfile(name), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ProfileDTO> updateProfile(@PathVariable Long id, @Validated @RequestBody Profile profile, BindingResult bindingResult) {
        ProfileDTO updatedProfile = profileService.update(profile, id, bindingResult);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @PostMapping("/imageavatar/{id}")
    public ResponseEntity<Void> uploadImageAvatar(@PathVariable Long id, @Validated @RequestBody String image, BindingResult bindingResult) {
        profileService.updateImageAvatar(id, image, bindingResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/imagecover/{id}")
    public ResponseEntity<Void> uploadImageCover(@PathVariable Long id, @Validated @RequestBody String image, BindingResult bindingResult ) {
        profileService.updateImageCover(id, image, bindingResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
