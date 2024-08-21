package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.Friend;
import com.example.socialnetworkbe.repository.FriendRepository;
import com.example.socialnetworkbe.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;

    @Autowired
    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    @Override
    public List<Friend> getListFriend(String email) {
        return friendRepository.findAllByEmail(email);
    }

}
