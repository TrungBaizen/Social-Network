package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Follow;
import com.example.socialnetworkbe.model.Friend;

import java.util.List;
import java.util.Optional;

public interface FriendService {

    List<Friend> getListFriend(String email);

    List<Friend> getCommonFriends(Long userId, Long otherUserId);

    void addFriend(Long userId, Long friendId);

    void unfriend(Long userId, Long friendId);

    void unfollow(Long userId, Long followedId);

    // Theo dõi người dùng
    void followUser(Long userId, Long friendUserId);

}
