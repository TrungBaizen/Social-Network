package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.model.FriendRequest;
import com.example.socialnetworkbe.service.FriendRequestService;
import com.example.socialnetworkbe.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;
    private final FriendRequestService friendRequestService;

    @Autowired
    public FriendController(FriendService friendService, FriendRequestService friendRequestService) {
        this.friendService = friendService;
        this.friendRequestService = friendRequestService;
    }

    // gui loi moi ket ban cho nguoi khac
    @PostMapping("/send-request")
    public ResponseEntity<FriendRequest> sendFriendRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        try {
            FriendRequest request = friendRequestService.sendFriendRequest(senderId, receiverId);
            return ResponseEntity.status(HttpStatus.CREATED).body(request);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // dong y loi moi ket ban
    @PostMapping("/accept-request")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam Long requestId) {
        try {
            FriendRequest request = friendRequestService.acceptFriendRequest(requestId);
            if (request != null) {
                return ResponseEntity.ok(request);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // tu choi loi moi ket ban
    @PostMapping("/reject-request")
    public ResponseEntity<FriendRequest> rejectFriendRequest(@RequestParam Long requestId) {
        try {
            FriendRequest request = friendRequestService.rejectFriendRequest(requestId);
            if (request != null) {
                return ResponseEntity.ok(request);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // list lời mời kết bạn
    @GetMapping("/pending-requests")
    public ResponseEntity<List<FriendRequest>> getPendingRequestsForUser(@RequestParam Long userId) {
        List<FriendRequest> requests = friendRequestService.getPendingRequestsForUser(userId);
        return ResponseEntity.ok(requests);
    }


    // unfriend sau khi da ket ban
    @DeleteMapping("/unfriend")
    public ResponseEntity<String> unfriend(@RequestParam Long userId, @RequestParam Long friendId) {
        try {
            friendService.unfriend(userId, friendId);
            return ResponseEntity.ok("Unfriend successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    // API để hủy follow
    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollow(@RequestParam Long userId, @RequestParam Long followedId) {
        try {
            friendService.unfollow(userId, followedId);
            return ResponseEntity.ok("Unfollowed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Hủy loi moi ket ban mà mình vừa gửi
    @DeleteMapping("/cancel-request")
    public ResponseEntity<String> cancelFriendRequest(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        try {
            friendRequestService.cancelFriendRequest(senderId, receiverId);
            return new ResponseEntity<>("Friend request cancelled successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error cancelling friend request", HttpStatus.BAD_REQUEST);
        }
    }
}
