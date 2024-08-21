package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.*;
import com.example.socialnetworkbe.model.DTO.FriendDTO;
import com.example.socialnetworkbe.model.DTO.FriendshipStatusDTO;
import com.example.socialnetworkbe.repository.*;
import com.example.socialnetworkbe.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl implements FriendService {


    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;



    @Autowired
    public FriendServiceImpl(FriendRepository friendRepository,
                             UserRepository userRepository,
                             FriendRequestRepository friendRequestRepository,
                             FollowRepository followRepository,
                             ProfileRepository profileRepository
                             ) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.followRepository = followRepository;
        this.profileRepository = profileRepository;

    }

    @Override
    public List<Friend> getListFriend(String email) {
        return List.of();
    }

    @Override
    public List<FriendDTO> getListFriendDTO(String email) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Friend> friends = friendRepository.findByUser(user);

            return friends.stream().map(friend -> {
                String friendEmail = friend.getFriendUser().getEmail();
                Profile friendProfile = profileRepository.findByEmail(friendEmail);
                return new FriendDTO(friend, friendProfile);
            }).collect(Collectors.toList());
        }
        throw new RuntimeException("User not found with email: " + email);
    }

    @Override
    public List<Friend> getCommonFriends(Long userId, Long otherUserId) {
        List<Friend> userFriends = friendRepository.findByUserId(userId);
        List<Friend> otherUserFriends = friendRepository.findByUserId(otherUserId);

        return userFriends.stream()
                .filter(friend -> otherUserFriends.stream()
                        .anyMatch(OF -> OF.getFriendUser().equals(friend.getFriendUser())))
                .collect(Collectors.toList());
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

    @Override
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
        Optional<Follow> followToDelete = followRepository.findByFollowerIdAndFollowedId(userId, followedId);

        if (userOpt.isPresent() && followedOpt.isPresent()) {
            User user = userOpt.get();
            User followed = followedOpt.get();


            followRepository.delete(followToDelete.get());
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


    @Override
    @Transactional
    public void followUser(Long userId, Long friendUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        User friendUser = userRepository.findById(friendUserId)
                .orElseThrow(() -> new RuntimeException("Friend user not found with id: " + friendUserId));

        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowed(user, friendUser);

        if (existingFollow.isEmpty()) {
            // Tạo mới mối quan hệ follow
            Follow follow = new Follow();
            follow.setFollower(user);
            follow.setFollowed(friendUser);
            followRepository.save(follow);

            // Thiết lập lại trường is_follow thành true trong bảng friends
            Optional<Friend> friendOpt = friendRepository.findByUserAndFriendUser(user, friendUser);

            if (friendOpt.isPresent()) {
                Friend friend = friendOpt.get();
                friend.setFollow(true); // Set lại trường is_follow thành true
                friendRepository.save(friend);
            } else {
                // Nếu không có bản ghi Friend tồn tại, tạo mới một bản ghi
                Friend newFriend = new Friend();
                newFriend.setUser(user);
                newFriend.setFriendUser(friendUser);
                newFriend.setFollow(true);
                friendRepository.save(newFriend);
            }
        }
    }



    @Override
    public boolean areFriends(Long senderId, Long receiverId) {
        return friendRepository.areFriends(senderId, receiverId);
    }

    @Override
    public boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId) {
        return friendRepository.existsBySenderIdAndReceiverId(senderId, receiverId);
    }

    @Override
    public boolean isFollowing(Long userId, Long friendUserId) {
        return friendRepository.isFollowing(userId, friendUserId);
    }
}
