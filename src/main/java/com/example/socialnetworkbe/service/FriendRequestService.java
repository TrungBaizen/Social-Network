package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.DTO.FriendRequestDTO;
import com.example.socialnetworkbe.model.FriendRequest;

import java.util.List;
import java.util.Optional;

public interface FriendRequestService {
    FriendRequest sendFriendRequest(Long senderId, Long receiverId);

    FriendRequest acceptFriendRequest(Long requestId);

    FriendRequest rejectFriendRequest(Long requestId);

    List<FriendRequestDTO> getPendingRequestsForUser(Long userId);

    void cancelFriendRequest(Long senderId, Long receiverId);
}
