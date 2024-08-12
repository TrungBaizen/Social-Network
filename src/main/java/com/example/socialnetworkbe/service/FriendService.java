package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.Friend;

public interface FriendService {
    Friend save(Friend friend);
    Friend update(Friend friend,Long id);
    Friend delete(Long id);
}
