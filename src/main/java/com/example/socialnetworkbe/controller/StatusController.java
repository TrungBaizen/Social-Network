package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.enums.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/statuses")
public class StatusController {
    @GetMapping
    public ResponseEntity<Status[]> getStatuses() {
        return new ResponseEntity<>(Status.values(), HttpStatus.OK);
    }
}
