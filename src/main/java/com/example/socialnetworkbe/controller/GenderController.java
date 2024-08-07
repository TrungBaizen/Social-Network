package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.enums.Gender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genders")
@CrossOrigin("*")
public class GenderController {
    @GetMapping
    public ResponseEntity<Gender[]> getGender(){
        return new ResponseEntity<>(Gender.values(), HttpStatus.OK);
    }
}
