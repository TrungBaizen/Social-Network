package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.repository.LikeRepository;
import com.example.socialnetworkbe.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@CrossOrigin("*")
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @DeleteMapping("/id")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        likeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
