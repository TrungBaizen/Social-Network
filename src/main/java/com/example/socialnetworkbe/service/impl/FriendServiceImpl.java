package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.Follow;
import com.example.socialnetworkbe.model.Friend;
import com.example.socialnetworkbe.model.FriendRequest;
import com.example.socialnetworkbe.model.User;
import com.example.socialnetworkbe.repository.FollowRepository;
import com.example.socialnetworkbe.repository.FriendRepository;
import com.example.socialnetworkbe.repository.FriendRequestRepository;
import com.example.socialnetworkbe.repository.UserRepository;
import com.example.socialnetworkbe.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FollowRepository followRepository;



    @Autowired
    public FriendServiceImpl(FriendRepository friendRepository,
                             UserRepository userRepository,
                             FriendRequestRepository friendRequestRepository,
                             FollowRepository followRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.followRepository = followRepository;

    }


    @Override
    public List<Friend> getListFriend(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return friendRepository.findAllByEmail(email);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found with id: " + friendId));

        if (friendRepository.existsByUserAndFriendUser(user, friend)) {
            throw new RuntimeException("Friendship already exists");
        }

         Friend newFriend = new Friend();
        newFriend.setUser(user);
        newFriend.setFriendUser(friend);

        friendRepository.save(newFriend);
    }

    public void unfriend(Long userId, Long friendId) {
        // Tìm yêu cầu kết bạn từ userId đến friendId
        Optional<FriendRequest> requestA = friendRequestRepository.findBySenderIdAndReceiverId(userId, friendId);
        // Tìm yêu cầu kết bạn từ friendId đến userId
        Optional<FriendRequest> requestB = friendRequestRepository.findBySenderIdAndReceiverId(friendId, userId);

        // Xóa các bản ghi kết bạn
        Optional<Friend> friendA = friendRepository.findByUserIdAndFriendUserId(userId, friendId);
        Optional<Friend> friendB = friendRepository.findByUserIdAndFriendUserId(friendId, userId);

        if (friendA.isPresent()) {
            friendRepository.delete(friendA.get());
        }

        if (friendB.isPresent()) {
            friendRepository.delete(friendB.get());
        }

        // Xóa các bản ghi follow
        Optional<Follow> followA = followRepository.findByFollowerIdAndFollowedId(userId, friendId);
        Optional<Follow> followB = followRepository.findByFollowerIdAndFollowedId(friendId, userId);

        if (followA.isPresent()) {
            followRepository.delete(followA.get());
        }

        if (followB.isPresent()) {
            followRepository.delete(followB.get());
        }

        // Cập nhật trạng thái yêu cầu kết bạn nếu tồn tại
        if (requestA.isPresent()) {
            FriendRequest friendRequestA = requestA.get();
            friendRequestA.setAccepted(false);
            friendRequestRepository.save(friendRequestA);
        }

        if (requestB.isPresent()) {
            FriendRequest friendRequestB = requestB.get();
            friendRequestB.setAccepted(false);
            friendRequestRepository.save(friendRequestB);
        }
    }



    @Override
    @Transactional
    public void unfollow(Long userId, Long followedId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<User> followedOpt = userRepository.findById(followedId);

        // Tìm bản ghi follow từ userId đến followedId
        Optional<Follow> followToDelete = followRepository.findByFollowerIdAndFollowedId(userId, followedId);

        if (userOpt.isPresent() && followedOpt.isPresent()) {
            User user = userOpt.get();
            User followed = followedOpt.get();


            followRepository.delete(followToDelete.get()); // xóa khoi bảng follows
            System.out.println("Deleted follow from " + userId + " to " + followedId);

            // xoa mqh follow giữa 2 người dùng
            Optional<Friend> followOpt = friendRepository.findByUserAndFriendUser(user, followed);

            followOpt.ifPresent(follow -> {
                follow.setFollow(false); // set lại trường is_follow thành false trong bảng friends
                friendRepository.save(follow);
            });
        } else {
            throw new RuntimeException("User or followed user not found");
        }
    }

}
