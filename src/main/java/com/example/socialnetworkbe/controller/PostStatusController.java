package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.enums.PostStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/post_statuses")
public class PostStatusController {
    @GetMapping
    public ResponseEntity<PostStatus[]> getStatuses() {
        return new ResponseEntity<>(PostStatus.values(), HttpStatus.OK);
    }
}
