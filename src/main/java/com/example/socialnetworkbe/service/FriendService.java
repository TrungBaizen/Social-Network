package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Friend;

import java.util.List;

public interface FriendService {
    List<Friend> getListFriend(String email);
}
