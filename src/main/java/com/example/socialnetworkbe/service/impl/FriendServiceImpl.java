package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.Friend;
import com.example.socialnetworkbe.model.FriendRequest;
import com.example.socialnetworkbe.model.User;
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




    @Autowired
    public FriendServiceImpl(FriendRepository friendRepository, UserRepository userRepository, FriendRequestRepository friendRequestRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
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

        // Create and save new friendship
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

}
