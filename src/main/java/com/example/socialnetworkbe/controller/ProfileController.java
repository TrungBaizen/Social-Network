package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.model.Profile;
import com.example.socialnetworkbe.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<List<Profile>> getProfiles() {
        return new ResponseEntity<>(profileService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        return new ResponseEntity<>(profileService.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Profile>> searchProfiles(@RequestParam String name) {
        return new ResponseEntity<>(profileService.searchProfile(name), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long id, @Validated @RequestBody Profile profile, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        Profile updatedProfile = profileService.update(profile, id, bindingResult, userDetails);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }
}
