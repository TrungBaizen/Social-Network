package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.model.DTO.FriendDTO;
import com.example.socialnetworkbe.model.DTO.FriendRequestDTO;
import com.example.socialnetworkbe.model.DTO.FriendshipStatusDTO;
import com.example.socialnetworkbe.model.Follow;
import com.example.socialnetworkbe.model.Friend;
import com.example.socialnetworkbe.model.FriendRequest;
import com.example.socialnetworkbe.repository.FriendRequestRepository;
import com.example.socialnetworkbe.service.FriendRequestService;
import com.example.socialnetworkbe.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;
    private final FriendRequestService friendRequestService;
    private final FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendController(FriendService friendService, FriendRequestService friendRequestService, FriendRequestRepository friendRequestRepository) {
        this.friendService = friendService;
        this.friendRequestService = friendRequestService;
        this.friendRequestRepository = friendRequestRepository;

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
//    @GetMapping("/pending-requests")
//    public ResponseEntity<List<FriendRequest>> getPendingRequestsForUser(@RequestParam Long userId) {
//        List<FriendRequest> requests = friendRequestService.getPendingRequestsForUser(userId);
//        return ResponseEntity.ok(requests);
//    }

    @GetMapping("/pending-requests")
    public ResponseEntity<List<FriendRequestDTO>> getPendingRequestsForUser(@RequestParam Long userId) {
        // Lấy danh sách FriendRequestDTO từ service
        List<FriendRequestDTO> requests = friendRequestService.getPendingRequestsForUser(userId);
        // Trả về danh sách DTO
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

    @GetMapping("/list")
    public ResponseEntity<List<FriendDTO>> getListFriend(@RequestParam String email) {
        try {
            List<FriendDTO> friends = friendService.getListFriendDTO(email);
            return ResponseEntity.ok(friends);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // API để lấy danh sách bạn bè chung của hai người dùng
    @GetMapping("/common-friends")
    public ResponseEntity<List<Friend>> getCommonFriends(
            @RequestParam Long userId,
            @RequestParam Long otherUserId) {
        try {
            List<Friend> commonFriends = friendService.getCommonFriends(userId, otherUserId);
            return ResponseEntity.ok(commonFriends);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    // Follow một người dùng
    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestParam Long userId, @RequestParam Long friendUserId) {
        try {
            friendService.followUser(userId, friendUserId);
            return ResponseEntity.ok("Followed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }



    @GetMapping("/check-friendship")
    public boolean checkFriendship(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return friendService.areFriends(senderId, receiverId);
    }

    @GetMapping("/check-follow")
    public boolean checkFollow(@RequestParam Long userId, @RequestParam Long friendUserId) {
        return friendService.isFollowing(userId, friendUserId);
    }
}
