package com.example.socialnetworkbe.service;
import com.example.socialnetworkbe.model.DTO.FriendDTO;
import com.example.socialnetworkbe.model.Friend;

import java.util.List;

public interface FriendService {

    List<Friend> getListFriend(String email);

    List<FriendDTO> getListFriendDTO(String email);


    List<Friend> getCommonFriends(Long userId, Long otherUserId);

    void addFriend(Long userId, Long friendId);

    void unfriend(Long userId, Long friendId);

    void unfollow(Long userId, Long followedId);

    void followUser(Long userId, Long friendUserId);

}
