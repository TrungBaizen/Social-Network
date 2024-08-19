package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Friend;

import java.util.List;

public interface FriendService {

    List<Friend> getListFriend(String email);

    void addFriend(Long userId, Long friendId);

    void unfriend(Long userId, Long friendId);

    void unfollow(Long userId, Long followedId);

}
